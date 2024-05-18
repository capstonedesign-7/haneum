import os
from typing import Union, List

from fastapi import FastAPI, UploadFile, File, Form

from pronounce_assess import *
from audio_convert import *
from temp_prompts import *
from openAI_chains import *

app = FastAPI()

if __name__ == '__main__':
    import uvicorn
    print("\nMain RUN OK\n")
    uvicorn.run("main:app",reload=True)

  
@app.post("/lev1_assessment")
async def transcribe_audio(audio_file: UploadFile=File(...), content_idx: int=Form(...)):
    try:
        content = await audio_file.read()
        with open(os.path.join(audio_file.filename),"wb") as aud:
            aud.write(content)
        speechfile = wav_converter(audio_file.filename)
        result_dict = pronunciation_assessment_from_file(speechfile, content_texts[content_idx])
        os.remove(speechfile)
        
    except Exception as e:
        print(e)
        text = f"오디오 처리를 실패했습니다. {e}"
        return {"status": "fail", "result": text}
    return result_dict


@app.post("/lev2_assessment")
async def transcribe_audio(audio_file: UploadFile=File(...), content_idx: int=Form(...)):
    try:
        content = await audio_file.read()
        with open(os.path.join(audio_file.filename),"wb") as aud:
            aud.write(content)
        speechfile = wav_converter(audio_file.filename)

        #contents assessment
        open_ai_result = stt_with_correction(speechfile, content_texts[content_idx])
        print(open_ai_result)
        #pronounce assessment
        result_dict = pronunciation_assessment_from_file(speechfile, open_ai_result[0])
        os.remove(speechfile)
        result_dict["stt_result"] = open_ai_result[0]
        result_dict["contents_feedback"] = open_ai_result[1]
        
    except Exception as e:
        print(e)
        text = f"오디오 처리를 실패했습니다. {e}"
        return {"status": "fail", "result": text}
    return result_dict


@app.get("/texts/{content_idx}")
def read_text(content_idx: int):
    if content_idx not in content_texts:
        return {"error": f"there is no item id: {content_texts}"}
    item = content_texts[content_idx]
    return item
