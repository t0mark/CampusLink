import os
import firebase_admin
from firebase_admin import credentials, firestore

# 현재 파일의 디렉토리를 기준으로 credentials.json 파일 경로 설정
BASE_DIR = os.path.dirname(os.path.abspath(__file__))  # 현재 파일 위치
CREDENTIALS_PATH = os.path.join(BASE_DIR, 'campuslink-6f52c-firebase-adminsdk-4oml8-c83af28b5c.json')  # credentials.json 경로 생성

# Firebase Admin SDK 초기화
cred = credentials.Certificate(CREDENTIALS_PATH)
firebase_admin.initialize_app(cred)

# Firestore 클라이언트 생성
db = firestore.client()


def escape_firestore_path(path):
    return path.replace('/', '\u202F')

def preprocess_None(data):
    return {key: value for key, value in data.items()}

def preprocess_files(data):
    if "첨부파일" in data and isinstance(data["첨부파일"], dict):
        data["첨부파일"] = [{"첨부파일이름": name, "첨부파일링크": link} for name, link in data["첨부파일"].items()]
    return data

def upload_to_firebase(collection_name, document_id, data):
    # 컬렉션 이름과 문서 ID 이스케이프 처리
    escaped_collection_name = escape_firestore_path(collection_name)
    escaped_document_id = escape_firestore_path(document_id)
    
    processed_data = preprocess_files(data)
    
    # 데이터의 None 값을 null로 변환
    processed_data = preprocess_None(data) if isinstance(data, dict) else data
    

    if isinstance(processed_data, dict):
        if escaped_document_id:
            db.collection(escaped_collection_name).document(escaped_document_id).set(processed_data)
        else:
            db.collection(escaped_collection_name).add(processed_data)

