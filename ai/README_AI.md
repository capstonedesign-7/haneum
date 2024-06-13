-Haneum/AI Description
AI폴더는FastAPI & Uvicorn(Backend), OpenAI API + Langchain, Azure API로 구성되어있음.
시스템 환경변수에 AZURE_API_KEY와 OPENAI_API_KEY 등록이 필요함.

fast api와 Uvicorn 및 통합은 main.py,
Openai API와 Langchian을 사용하는 openAI_chains.py,
Azure API를 사용하는 pronounce_assess.py,
그리고 prototype용 테스트 컨텐츠가 저장되어있는 temp_prompts.py로 구성되어있다.

#Requirements.txt
Preprocess and test 폴더를 제외한 모든 코드에 필요한 라이브러리 모음이다.
pip install requirementst.txt로  환경을 구성할 수 있다.
가볍게 서버를 작동시키기 위해 fastapi와함께 uvicorn을 이용하였다.
프로토타입 환경처럼 Uvicorn을 이용하고싶다면 requirements.txt에 밑 줄을 추가하면 된다.
uvicorn==0.29.0

추가로 오디오처리를 위해 ffmepg설치가 필요하다.
본 프로젝트에서는 로컬환경에서 따로 설치 및 환경변수 지정하여 사용하였음
https://ffmpeg.org/

#Preproces and tests
데이터 전처리와 콘텐츠TTS를 미리 제작할 때 사용한 코드 폴더
본 폴더의 코드는 Haneum application을 작동하는 것과 상관이 없다

 - script parser.py
이미지파일(교과서 스캔본)으로부터 상황별 문답 데이터를 추출하기 위한 코드
cv2(이미지처리)와 ocr 코드로 구성되어있다.

-op_az_tts_maker.py
OpenAI와 Azure의 tts를 미리 작성해보고 가장 성능이 높은것을 사용하기위한 테스트코드
