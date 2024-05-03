import os
import time
from openai import OpenAI

os.environ["OPENAI_API_KEY"] = YOURKEY

client = OpenAI()
voices = ["alloy","echo", "fable", "onyx", "nova", "shimmer"]
speech_file_path = "optts"
iptext = """여러분, 휴일 잘 보내셨습니까? 5월 15일 월요일 스승
의 날 뉴스 광장입니다. 미국이 내일 단기금리를 0.5% 포인트
올릴 것이 확실시되고 있습니다."""


for i in range(3):
    start= time.time()
    nowpath = speech_file_path+str(voices[i])+".mp3"
    with client.audio.speech.with_streaming_response.create(
        model = "tts-1",
        voice = str(voices[i]),
        input = iptext
        )as response:
        response.stream_to_file(nowpath)
    end = time.time()
    print(f"{end - start:.5f} sec")

