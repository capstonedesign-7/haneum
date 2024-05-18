import os
from openai import OpenAI

from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate
from langchain.schema import StrOutputParser

correction_instruction = """
-너는 외국인의 한국어 학습을 돕는 어시스턴트다.
-"{transcript}"은 외국인의 음성을 STT처리 한 결과이다.
-"{transcript}"문장을 통해 발음을 평가할 것이므로, 잘못 발음되었을 수 있는 부분을 수정하라, 수정할 부분이 없다면 하지 않아도 좋다.
-답변은 추가되는 내용 없이 수정된 내용을 그대로 답변해야한다.
"""

assessment_instruction = """
-너는 외국인의 한국어 학습을 돕는 어시스턴트다.
-질문에 대해 대답이 알맞은지 평가하고, 대답이 적절하지 않다면 그 이유를 설명하라
-의사와 환자간의 대화 상황을 가정하고 질문과 대답을 진행한다.
-의사의 질문은 다음과 같다 : {lv2_question}
"""

#llm = ChatOpenAI(model_name="gpt-3.5-turbo")
client = OpenAI()
llm = ChatOpenAI(model_name="gpt-4o")


def stt_with_correction(audio_path, lv2_question):
    result_str = []
    #Speech to Text
    audio_file = open(audio_path, "rb")
    transcript = client.audio.transcriptions.create(
        file=audio_file,
        model="whisper-1",
        language="ko",
        response_format="text",
        temperature=0.0,
        )

    #Misronouonce Correction with LLM
    chat_prompt = ChatPromptTemplate.from_messages([
        ("system", correction_instruction),
        ("user", "{transcript}")
    ])
    chain = chat_prompt | llm | StrOutputParser()
    corrected_stt = chain.invoke({"transcript": str(transcript)})
    result_str.append(str(corrected_stt))

    #Contents Assessment
    chat_prompt = ChatPromptTemplate.from_messages([
        ("system", assessment_instruction),
        ("user", "{user_input}")
    ])
    chain = chat_prompt | llm | StrOutputParser()
    assessment = chain.invoke({"lv2_question": lv2_question,
                               "user_input": str(corrected_stt)})
    result_str.append(str(assessment))
    
    return result_str


