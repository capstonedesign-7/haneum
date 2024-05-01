import os
import azure.cognitiveservices.speech as speechsdk

speech_key = YOURKEY
service_region = "koreacentral"

speech_config = speechsdk.SpeechConfig(subscription=speech_key, region=service_region)
speech_config.speech_synthesis_voice_name = "ko-KR-HyunsuNeural"

text = "여러분, 휴일 잘 보내셨습니까? 5월 15일 월요일 스승의 날 뉴스 광장입니다. 미국이 내일 단기금리를 0.5% 포인트올릴 것이 확실시되고 있습니다."

speech_synthesizer = speechsdk.SpeechSynthesizer(speech_config=speech_config)

result = speech_synthesizer.speak_text_async(text).get()

print(type(result))

# Check result
if result.reason == speechsdk.ResultReason.SynthesizingAudioCompleted:
    print("Speech synthesized for text [{}]".format(text))
    audio_data_stream = speechsdk.AudioDataStream(result)
    audio_data_stream.save_to_wav_file("aztts.wav")
    
elif result.reason == speechsdk.ResultReason.Canceled:
    cancellation_details = result.cancellation_details
    print("Speech synthesis canceled: {}".format(cancellation_details.reason))
    if cancellation_details.reason == speechsdk.CancellationReason.Error:
        print("Error details: {}".format(cancellation_details.error_details))

