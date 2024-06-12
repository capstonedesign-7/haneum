import os
from datetime import datetime
from fastapi import FastAPI, UploadFile, File, Form, HTTPException
from fastapi.responses import FileResponse
from starlette.background import BackgroundTask

from pronounce_assess import *
from audio_convert import *
from temp_prompts import *
from openAI_chains import *
from openAI_chains import Messages

app = FastAPI()

if __name__ == '__main__':
    import uvicorn
    print("\nMain RUN OK\n")
    uvicorn.run("main:app",reload=True)

@app.get("/texts/{content_idx}")
async def read_text(content_idx: int):
    if content_idx not in content_texts:
        return {"error": f"there is no item id: {content_texts}"}
    item = content_texts[content_idx]
    return item

@app.get("/{roleplay}/goals")
async def get_roleplay_goals(roleplay: str):
    return roleplay_to_goal_map[roleplay]

@app.post("/lev1_assessment")
async def transcribe_audio(audio_file: UploadFile=File(...), content_idx: int=Form(...)):
    try:
        content = await audio_file.read()
        with open(os.path.join(audio_file.filename),"wb") as aud:
            aud.write(content)
        speechfile = wav_converter(audio_file.filename)
        print(speechfile)
        result_dict = pronunciation_assessment_from_file(speechfile, content_texts[content_idx])
        os.remove(speechfile)
        
    except Exception as e:
        text = f"오디오 처리를 실패했습니다. {e}"
        print(text)
        raise HTTPException(status_code=420, detail = text)
    return result_dict

@app.post("/lev2_assessment")
async def transcribe_audio(audio_file: UploadFile=File(...), content_idx: int=Form(...), language_idx: int=Form(...)):
    try:
        content = await audio_file.read()
        with open(os.path.join(audio_file.filename),"wb") as aud:
            aud.write(content)
        speechfile = wav_converter(audio_file.filename)

        #contents assessment
        stt_result = stt_with_correction(speechfile)
        print(stt_result)
        feedback_result = contents_feedback(stt_result, content_texts[content_idx], lang_option[language_idx])
        
        #pronounce assessment
        result_dict = pronunciation_assessment_from_file(speechfile, stt_result)
        os.remove(speechfile)
        result_dict["stt_result"] = stt_result
        result_dict["contents_feedback"] = feedback_result
        print(feedback_result)
        
    except Exception as e:
        text = f"오디오 처리를 실패했습니다. {e}"
        print(text)
        raise HTTPException(status_code=420, detail = text)
    return result_dict

@app.post("/chat/{roleplay}")
async def post_chat_role_play(roleplay: str, history_msg: str=Form(...), audio_file: UploadFile=File(...)):
    try:
        content = await audio_file.read()
        with open(os.path.join(audio_file.filename),"wb") as aud:
            aud.write(content)
        speechfile = wav_converter(audio_file.filename)
        stt_result = stt_with_correction(speechfile)
        
        #pronounce assessment
        result_dict = pronunciation_assessment_from_file(speechfile, stt_result)
        os.remove(speechfile)
        result_dict["stt_result"] = stt_result
        
    except Exception as e:
        text = f"오디오 처리를 실패했습니다. {e}"
        print(text)
        raise HTTPException(status_code=420, detail = text)
    
    print(history_msg)
    msgs = []
    temp_str = history_msg.split("/")
    for i in range(len(temp_str)):
        tempdict = dict()
        if(i%2==1):
            role = "assistant"
        else:
            role = "user"
        tempdict["role"] = role
        tempdict["content"] = temp_str[i]
        msgs.append(tempdict)

    system_prompt = roleplay_to_system_prompt_map[roleplay]
    msgs = [{"role": "system", "content": system_prompt}] + msgs + [{"role": "user", "content": stt_result}]
    resp = chat(messages=msgs)
    result_dict["ai_response"] = resp
    return result_dict

@app.post("/{roleplay}/check_goals")
async def post_roleplay_check_goal(roleplay: str, history_msg: str=Form(...)):
    print(history_msg)
    msgs = []
    temp_str = history_msg.split("/")
    for i in range(len(temp_str)):
        tempdict = dict()
        if(i%2==1):
            role = "assistant"
        else:
            role = "user"
        tempdict["role"] = role
        tempdict["content"] = temp_str[i]
        msgs.append(tempdict)
    print(msgs)
    goal_comp = detect_goal_completion(msgs, roleplay)
    print(goal_comp)
    return goal_comp

def cleanup(temp_file):
    os.remove(temp_file)

@app.post("/text_to_speech")#, response_class=FileResponse)
async def send_audio(response_txt: str=Form(...)):
    temp_path = datetime.strftime(datetime.now(),"%Y%m%d_%H%M%S")+"temptts.mp3"
    def cleanup():
        os.remove(temp_path)
    await chat_tts(response_txt,temp_path)
    return FileResponse(temp_path,background=BackgroundTask(cleanup))