from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from settings import CHROME_DRIVER_PATH

def login(type):
    ID = "ID"
    PW = "PW"
    time = 5
    if type == "SW1" or type == "SW3":
        login_url = "https://swuniv.jbnu.ac.kr/main/jbnusw?gc=LOGIN"

        # ChromeDriver 설정
        service = Service(CHROME_DRIVER_PATH)
        driver = webdriver.Chrome(service=service)

        driver.get(login_url)

        # 로그인 처리
        # 아이디 입력 필드
        username_field = WebDriverWait(driver, time).until(
            EC.presence_of_element_located((By.ID, "user_id"))
        )
        username_field.send_keys(ID)

        # 비밀번호 입력 필드
        password_field = driver.find_element(By.ID, "user_passwd")
        password_field.send_keys(PW)

        # 로그인 버튼 클릭
        login_button = driver.find_element(By.CSS_SELECTOR, ".member_btn01")  # 로그인 버튼 클래스
        login_button.click()
    
    elif type == "EMPLOY":
        login_url = "https://career.jbnu.ac.kr/career/21909/subview.do?enc=Zm5jdDF8QEB8JTJGc3ViTG9naW4lMkZjYXJlZXIlMkZ2aWV3LmRvJTNG"

        # ChromeDriver 설정
        service = Service(CHROME_DRIVER_PATH)
        driver = webdriver.Chrome(service=service)

        driver.get(login_url)

        # 로그인 처리
        # 아이디 입력 필드
        username_field = WebDriverWait(driver, time).until(
            EC.presence_of_element_located((By.ID, "userId"))
        )
        username_field.send_keys(ID)

        # 비밀번호 입력 필드
        password_field = driver.find_element(By.ID, "userPwd")
        password_field.send_keys(PW)

        # 로그인 버튼 클릭
        login_button = driver.find_element(By.CSS_SELECTOR, "._loginSubmit")  # 로그인 버튼 클래스
        login_button.click()
    else:
        service = Service(CHROME_DRIVER_PATH)
        driver = webdriver.Chrome(service=service)

    return driver
