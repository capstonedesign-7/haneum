from typing import List

from openai import OpenAI
from langchain_openai import ChatOpenAI
from langchain.schema import HumanMessage, AIMessage, SystemMessage
from langchain.prompts import ChatPromptTemplate
from langchain_core.output_parsers import JsonOutputParser
from langchain.schema import StrOutputParser
import langchain_core.pydantic_v1 as pyd1
import pydantic as pyd2

client = OpenAI()
llm = ChatOpenAI(model="gpt-4o")

class Turn(pyd2.BaseModel):
    role: str = pyd2.Field(description="role")
    content: str = pyd2.Field(description="content")

class Messages(pyd2.BaseModel):
    messages: List[Turn] = pyd2.Field(description="message", default=[])

class Goal(pyd1.BaseModel):
    goal: str = pyd1.Field(description="goal.")
    goal_number: int = pyd1.Field(description="goal number.")
    accomplished: bool = pyd1.Field(description="true if goal is accomplished else false.")

class Goals(pyd1.BaseModel):
    goal_list: List[Goal] = pyd1.Field(default=[])

parser = JsonOutputParser(pydantic_object=Goals)
format_instruction = parser.get_format_instructions()

type_to_msg_class_map = {
        "system":  SystemMessage,
        "user":  HumanMessage,
        "assistant":  AIMessage,
        }

correction_instruction = """
-너는 외국인의 한국어 학습을 돕는 어시스턴트다.
-"{transcript}"은 외국인의 음성을 STT처리 한 결과이다.
-"{transcript}"문장을 통해 발음을 평가할 것이므로, 잘못 발음되었을 수 있는 부분을 수정하라, 수정할 부분이 없다면 하지 않아도 좋다.
-답변은 추가되는 내용 없이 수정된 내용을 그대로 답변해야한다.
"""

assessment_instruction = ["""
-결과에 대하여 한국어로 대답해라.
-한국어를 제외한 언어는 결과에 사용하지 않는다.
-너는 외국인 유학생의 한국어 학습을 돕는 어시스턴트다.
-질문에 대해 대답이 알맞은지 평가하고, 대답이 적절하지 않다면 그 이유를 설명하라
-의사와 환자간의 대화 상황을 가정하고 질문과 대답을 진행한다.
-의사의 질문은 다음과 같다 : {lv2_question}
-환자의 답변은 다음과 같다 : {user_input}
""",
"""
- Trả lời bằng tiếng Việt về kết quả.
- Vì người nói tiếng Việt sẽ kiểm tra kết quả nên ngoại trừ tiếng Việt thì ngôn ngữ không được sử dụng cho kết quả.
- Bạn là trợ lý giúp du học sinh Việt Nam học tiếng Hàn.
- Hãy đánh giá xem câu trả lời cho câu hỏi có đúng hay không và giải thích tại sao nếu câu trả lời không đúng.
- Giả định tình huống trò chuyện giữa bác sĩ và bệnh nhân, tiến hành đặt câu hỏi và trả lời.
- Các câu hỏi của bác sĩ như sau: {lv2_question}
- Câu trả lời của bệnh nhân như sau: {user_input}
"""]

roleplay_to_system_prompt_map = {
        "situation1": """\
- 너는 병원 접수원이다.
- 상대는 아파서 병원에 왔으므로, 기쁘게 반응하지 않는다.
- 아래의 단계로 질문을 한다.
1. 어디가 아파서 왔는지 묻기
2. 이름과 생년월일 묻기
3. 전화로 진료예약을 하고왔는지 묻기
4. 주문이 완료되면 앉아서 기다려달라는 안내와 함께 [END] 라고 이야기한다.\
""",
        "situation2": """\
- 너는 감기약을 처방하는 의사이다.
- 아래의 단계로 질문을 한다.
1. 환자에게 인사하기
2. 밥을 잘 먹는지 묻기
3. 알레르기가 있는지 묻기
4. 먹고있는 약이 있는지 묻기
5. 모든 질문에 답이 끝났으면 처방전을 드린다는 안내와 함께 [END] 라고 이야기한다.\
"""
        }

roleplay_to_goal_map = {
        "situation1": ["아픈곳 말하기",
                      "이름과 생년월일 말하기",
                      "진료예약 확인하기"],
        "situation2": ["밥을 잘 먹고있는지 말하기",
                       "나의 알레르기에 대해 말하기",
                       "내가 지금 먹고있는 약이 있는지 말하기"]
        }

async def stt_with_correction(audio_path):
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
    corrected_stt = await chain.ainvoke({"transcript": str(transcript)})
    
    return str(corrected_stt)

async def contents_feedback(corrected_stt, lv2_question, lang_idx):
    #Contents Assessment
    chat_prompt = ChatPromptTemplate.from_messages([
        ("system", assessment_instruction[lang_idx]),
        ("user", "{user_input}")
    ])
    chain = chat_prompt | llm | StrOutputParser()
    assessment = await chain.ainvoke({"lv2_question": lv2_question,
                                      "user_input": str(corrected_stt)})
    return assessment
    

async def detect_goal_completion(messages, roleplay):
    global parser, format_instruction

    # Conversation
    conversation ="\n".join([ f"{msg['role']}: {msg['content']}" for msg in messages]) 

    # Goals
    goal_list = roleplay_to_goal_map[roleplay]
    goals = "\n".join([f"- Goal Number {i}: {goal} " for i, goal in enumerate(goal_list)])

    
    prompt_template = """
# 대화
{conversation}
---
# 유저의 목표
{goals}
---
위 대화를 보고 유저가 goal들을 달성했는지 확인해서 아래 포맷으로 응답해.
{format_instruction}
"""

    chat_prompt_template = ChatPromptTemplate.from_messages([("user", prompt_template)])
    goal_check_chain = chat_prompt_template | llm | parser

    outputs = await goal_check_chain.ainvoke({"conversation": conversation,
                                             "goals": goals,
                                             "format_instruction": format_instruction})
    return outputs


async def chat(messages):
    messages_lc = []
    for msg in messages:
        msg_class = type_to_msg_class_map[msg["role"]]
        msg_lc = msg_class(content=msg["content"])

        messages_lc.append(msg_lc)
        
    resp = await llm.ainvoke(messages_lc)
    return {"role": "assistant", "content": resp.content}

async def chat_tts(content, file_path):
    with client.audio.speech.with_streaming_response.create(
        model = "tts-1",
        voice = "nova",
        speed = 0.9,
        input = content
        )as response:
        response.stream_to_file(file_path)
    return None