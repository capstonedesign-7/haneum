import os
from typing import List
from fastapi import FastAPI, UploadFile, File
from openai.resources.beta.threads import messages
from openai import OpenAI


client = OpenAI()
#model = ChatOpenAI(model="gpt-3.5-turbo")
app = FastAPI()

easy_instruction = """너는 병원 접수원이야
방문한 사람의 이름과 아픈 곳을 물어보고 맞는지 확인하면 돼
방문자 이름과 아픈 곳이 맞는지 다시 물어본 후, 맞다면 앉으라고 하면 돼
"""
messages = [{"role": "system", "content": easy_instruction}]

def ask(text):
    user_input = {"role": "user", "content": text}
    messages.append(user_input)

    response = client.chat.completions.create(
        model="gpt-3.5-turbo",
        messages=messages)

    bot_text = response.choices[0].message.content
    bot_resp = {"role": "assistant", "content": bot_text}
    
    messages.append(bot_resp)
    return bot_text

while True:
    user_input = input("user: ")
    bot_resp = ask(user_input)
    print(f"bot: {bot_resp}")
