import time
from openai import OpenAI

import azure.cognitiveservices.speech as speechsdk

client = OpenAI()
voices = ["alloy", "fable", "onyx", "nova", "shimmer"] #echo는 너무 영어화자임

speech_file_path = "optts"
content_texts = {
    0:"내가 핸드폰으로 한 녹음",
    
    1:"코가막히고 목이 따끔거리고 기침이 나요.",
    2:"언제쯤 나아서 학교에 갈 수 있나요?",
    3:"전에도 갑자기 아팠던 적이 있어요.",
    4:"발목이 시큰거리고 빨갛게 부었어요.",
    5:"눈이 건조하고 자주 충혈되는 것 같아요.",
    6:"약은 매일 식후 삼십 분 후에 드세요.",
    7:"치아 스케일링을 하러 왔어요.",
    8:"어제 저녁부터 머리가 울리고 열이 나요",
    9:"땀이 날 정도로 운동을 하면 안됩니다.",
    10:"영양제나 비타민을 먹어도 되나요?",

    11:"밥은 잘 드시고 계신가요?",
    12:"혹시 알레르기가 있나요?",
    13:"가족과 함께 생활하시나요?",
    14:"언제 가장 많이 아프세요?",
    15:"언제부터 증상이 있으셨던 거에요?",
    16:"본인 혈액형이 어떻게 되세요?",
    17:"최근 잠은 평균 몇 시간 정도 주무셨나요?",
    18:"꾸준히 하는 운동이 있으신가요?",
    19:"혹시 알략을 못 드시나요?",
    20:"또다른-데-, 아픈 곳 있으신가요?"
}
text = content_texts[20]


for i in range(5):
    start= time.time()
    nowpath = speech_file_path+str(voices[i])+".mp3"
    with client.audio.speech.with_streaming_response.create(
        model = "tts-1",
        voice = str(voices[i]),
        input = text
        )as response:
        response.stream_to_file(nowpath)
    end = time.time()
    print(f"{end - start:.5f} sec")

#AZ
    
speech_key = "a8495ff907e94c0cb4a82ecd5434b58e"
service_region = "koreacentral"

speech_config = speechsdk.SpeechConfig(subscription=speech_key, region=service_region)

speech_config.speech_synthesis_voice_name = "ko-KR-HyunsuNeural"
speech_synthesizer = speechsdk.SpeechSynthesizer(speech_config=speech_config)

result = speech_synthesizer.speak_text_async(text).get()

if result.reason == speechsdk.ResultReason.SynthesizingAudioCompleted:
    start= time.time()
    print("Speech synthesized for text [{}]".format(text))
    audio_data_stream = speechsdk.AudioDataStream(result)
    audio_data_stream.save_to_wav_file("aztts.wav")
    end = time.time()
    print(f"{end - start:.5f} sec")
    
elif result.reason == speechsdk.ResultReason.Canceled:
    cancellation_details = result.cancellation_details
    print("Speech synthesis canceled: {}".format(cancellation_details.reason))
    if cancellation_details.reason == speechsdk.CancellationReason.Error:
        print("Error details: {}".format(cancellation_details.error_details))
