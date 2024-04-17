import os
from typing import Union, List

from pydantic import BaseModel
import pydantic as pyd1
from fastapi import FastAPI, UploadFile, File, Form

from pronounce_assess import *
from audio_convert import *


#추후 rText 묶음으로 변형
r_texts = {
    0:"내가 핸드폰으로 한 녹음",
    1:"김서방네 지붕위에 콩깍지가 깐 콩깍지냐"
}
app = FastAPI()


if __name__ == '__main__':
    import uvicorn
    print("fine")
    uvicorn.run("main:app",reload=True)
    
@app.post("/assessment")
async def transcribe_audio(audio_file: UploadFile=File(...), text_idx: int=Form(...), file_name: str=Form(...)):
    try:
        content = await audio_file.read()
        with open(os.path.join(file_name),"wb") as aud:
            aud.write(content)
        speechfile = wav_converter(file_name)
        result_dict = pronunciation_assessment_from_file(speechfile, r_texts[text_idx])
        os.remove(speechfile)
    except Exception as e:
        print(e)
        text = f"오디오 처리를 실패했습니다. {e}"
        return {"status": "fail", "result": text}
    
    #print(result_dict)
    return result_dict

@app.get("/texts/{text_idx}")
def read_text(text_idx: int):

    if text_idx not in r_texts:
        return {"error": f"there is no item id: {text_idx}"}

    item = r_texts[text_idx]
    return item
