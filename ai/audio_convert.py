from pydub import AudioSegment

def wav_converter(speechfile):
    if speechfile.split('.')[1] != "wav":
        sound = AudioSegment.from_file(speechfile)
        speechfile = speechfile.split('.')[0]+".wav"
        sound.export(speechfile, format="wav", bitrate="128k")
        return speechfile
