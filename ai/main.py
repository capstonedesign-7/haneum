import os
from typing import Union, List

from pydantic import BaseModel
import pydantic as pyd1
from fastapi import FastAPI, UploadFile, File, Form

from pronounce_assess import *
from audio_convert import *
from temp_prompts import *

app = FastAPI()

if __name__ == '__main__':
    import uvicorn
    print("fine")
    uvicorn.run("main:app",reload=True)
    
@app.post("/assessment")
async def transcribe_audio(audio_file: UploadFile=File(...), text_idx: int=Form(...)):
    print(audio_file.filename)
    try:
        content = await audio_file.read()
        with open(os.path.join(audio_file.filename),"wb") as aud:
            aud.write(content)
        speechfile = wav_converter(audio_file.filename)
        result_dict = pronunciation_assessment_from_file(speechfile, temp_texts[text_idx])
        os.remove(speechfile)
    except Exception as e:
        print(e)
        text = f"오디오 처리를 실패했습니다. {e}"
        return {"status": "fail", "result": text}
    
    #print(result_dict)
    return result_dict

@app.get("/texts/{text_idx}")
def read_text(text_idx: int):

    if text_idx not in temp_texts:
        return {"error": f"there is no item id: {text_idx}"}

    item = temp_texts[text_idx]
    return item
