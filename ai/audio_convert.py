import os
from pydub import AudioSegment

#추후 pytempfile을 이용하는것으로 수정
def wav_converter(speechfile):
    if speechfile.split('.')[1] != "wav":
        org_ext = speechfile.split('.')[1]
        sound = AudioSegment.from_file(speechfile)
        speechfile = speechfile.split('.')[0]+".wav"
        sound.export(speechfile, format="wav", bitrate="128k")
        os.remove(str(speechfile.split('.')[0]+'.'+org_ext))
        return speechfile
