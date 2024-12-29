from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support import expected_conditions as EC

import time

def format_date(date_input):
    # 입력받은 날짜를 '-'를 기준으로 나눔
    if '-' in date_input:
        return date_input.replace('-', '.')
    else:
        return date_input

def click_button_and_get_info(driver):
    wait = WebDriverWait(driver, 5)

    def safe_find_element(xpath, action=None):
        try:
            element = wait.until(EC.presence_of_element_located((By.XPATH, xpath)))
            if action == "click":
                ActionChains(driver).move_to_element(element).click(element).perform()
            elif action == "get_text":
                return element.text
            elif action == "get_href":
                return element.get_attribute("href")
            return element
        except Exception as e:
            print(f"XPath '{xpath}' 작업 실패: {e}")
            return None

    # 1. 버튼 클릭
    button_xpath = "//button[contains(text(), 'Apply') or contains(@class, 'apply-button')]"
    safe_find_element(button_xpath, action="click")

    # 2. URL 추출
    link_xpath = "//a[contains(text(), '지원하기') and contains(@class, 'jss')]"
    url = safe_find_element(link_xpath, action="get_href")
    if url:
        return url

    # 3. 이메일 추출
    email_xpath = "//div[contains(@class, 'jss11')]"
    email_text = safe_find_element(email_xpath, action="get_text")
    if email_text:
        return email_text

    # 4. 연락처 추출
    phone_xpath = "//div[contains(@class, 'jss295')]"
    phone_text = safe_find_element(phone_xpath, action="get_text")
    if phone_text:
        return phone_text

    # 모든 시도가 실패하면 None 반환
    return None

    
def scrape_data(site_name, post_num, post_url, type, driver):
    driver.get(post_url)
    time.sleep(2)

    # 제목, 작성자, 작성일, 접수기간, 활동기간, 모집인원, 활동지역, 내용, 첨부파일, 이미지 링크, 지원 링크, URL
    title = None
    writer = None
    write_date = None
    recept_date = None
    activity_date = None
    recept_num = None
    activity_region = None
    content = None
    files = dict()
    img_url = list()
    apply_url = None
    url = post_url

    # 제목
    try:
        if type == "JBNU1" or type == "EMPLOY":
            title = driver.find_element(By.CSS_SELECTOR, 'h2.artclViewTitle').text.strip()
        elif type == "JBNU2":
            title = driver.find_element(By.CSS_SELECTOR, 'h2.title').text.strip()
        elif type == "Link1" or type == "Link2":
            title = driver.find_element(By.CSS_SELECTOR, "header.ActivityInformationHeader__StyledWrapper-sc-7bdaebe9-0 h1").text
        elif type == "SW1" or type == "SW2" or type == "SW3":
            title = driver.find_element(By.CSS_SELECTOR, "div.tit").text
        elif type == "BIGDATA":
            title = driver.find_element(By.CSS_SELECTOR, "h3.board_tt").text
    except Exception as e:
        print("오류발생 제목:", e)
    
    # 작성자
    try:
        if type == "JBNU2":
            writer = driver.find_element(By.CSS_SELECTOR, '.com-post-hd-01 ul.etc-list li:nth-child(1)').text.strip()
        elif type == "Link1" or type == "Link2":
            writer = driver.find_element(By.CSS_SELECTOR, "header.organization-info h2.organization-name").text
        elif type == "BIGDATA":
            writer = title.split("]")[0][1:]
    except Exception as e:
        print("오류발생 작성자:", e)

    # 작성일
    try:
        if type == "JBNU1" or type == "EMPLOY":
            write_date = driver.find_element(By.XPATH, "//dt[contains(text(), '작성일')]/following-sibling::dd").text.strip()
        elif type == "JBNU2":
            write_date = driver.find_element(By.CSS_SELECTOR, '.com-post-hd-01 ul.etc-list li:nth-child(2)').text.strip()
        elif type == "SW1" or type == "SW2":
            write_date = driver.find_element(By.CSS_SELECTOR, "div.name span:nth-of-type(2)").text.strip()
        elif type == "BIGDATA":
            write_date = driver.find_element(By.CSS_SELECTOR, "p.view_date em").text
    except Exception as e:
        print("오류발생 작성일:", e)
    
    # 접수기간
    try:
        if type == "Link1" or type == "Link2":
            start_date = driver.find_element(By.CSS_SELECTOR, ".RecruitPeriodField__StyledWrapper-sc-aaa21d80-0 span.start-at + span").text
            end_date = driver.find_element(By.CSS_SELECTOR, ".RecruitPeriodField__StyledWrapper-sc-aaa21d80-0 span.end-at + span").text
            recept_date = f"{start_date} ~ {end_date}"
        elif type == "SW3":
            recept_date = driver.find_element(By.CSS_SELECTOR, "div.inner").find_element(By.CSS_SELECTOR, "dl:nth-of-type(2) dd").text
    except Exception as e:
        print("오류발생 접수기간:", e)
    
    # 활동기간
    try:
        if type == "Link1":
            activity_date = driver.find_element(By.XPATH, "//dt[text()='활동기간']/following-sibling::dd").text
        elif type == "SW3":
            activity_date = driver.find_element(By.CSS_SELECTOR, "div.inner").find_element(By.CSS_SELECTOR, "dl:nth-of-type(1) dd").text
    except Exception as e:
        print("오류발생 활동기간:", e)
    
    # 모집인원
    try:
        if type == "Link1":
            recept_num = driver.find_element(By.XPATH, "//dt[text()='모집인원']/following-sibling::dd").text
        elif type == "SW3":
            recept_num = driver.find_element(By.CSS_SELECTOR, "div.inner").find_element(By.CSS_SELECTOR, "dl:nth-of-type(4) dd").text
    except Exception as e:
        print("오류발생 모집인원:", e)
    
    # 활동지역
    try:
        if type == "Link1":
            activity_region = driver.find_element(By.XPATH, "//dt[text()='활동지역']/following-sibling::dd/p").text
    except Exception as e:
        print("오류발생 활동지역:", e)
    
    # 내용
    try:
        if type == "JBNU1" or type == "EMPLOY":
            content = driver.find_element(By.CSS_SELECTOR, 'div.artclView').text.strip()
        elif type == "JBNU2":
            content = driver.find_element(By.CSS_SELECTOR, 'div.com-post-content-01.no-border-bottom').text.strip()
        elif type == "Link1" or type == "Link2":
            content = driver.find_element(By.ID, "DETAIL").text
        elif type == "SW1" or type == "SW2":
            content = driver.find_element(By.CSS_SELECTOR, "div.content_wrap").text.strip()
        elif type == "SW3":
            content = driver.find_element(By.CSS_SELECTOR, "div.program_viewbox").find_element(By.CSS_SELECTOR, "div.cont").text.strip()
        elif type == "BIGDATA":
            content = driver.find_element(By.CSS_SELECTOR, "div.board_con_txt").text
    except Exception as e:
        print("오류발생 내용:", e)
    
    # 첨부파일
    try:
        if type == "JBNU1" or type == "EMPLOY":
            file_elements = driver.find_elements(By.CSS_SELECTOR, 'dl.artclForm dd.artclInsert ul li a')
            for file_element in file_elements:
                file_name = file_element.text.strip()
                file_link = file_element.get_attribute('href')
                files[file_name] = file_link
        elif type == "JBNU2":
            file_buttons = driver.find_elements(By.CSS_SELECTOR, 'ul.com-post-files-01 li button')
            for button in file_buttons:
                file_name = button.find_element(By.CSS_SELECTOR, 'p.file-name').text.strip()
                onclick_value = button.get_attribute('onclick')
                if onclick_value and "cf_download" in onclick_value:
                    download_id = onclick_value.split("'")[1]
                    files[file_name] = f"https://www.jbnu.ac.kr/async/MultiFile/download.do?file={download_id}"
        elif type == "SW1" or type == "SW2":
            file_elements = driver.find_elements(By.CSS_SELECTOR, ".file_wrap .list li")
            for file_element in file_elements:
                file_name = file_element.find_element(By.CSS_SELECTOR, ".fname").text.strip()
                file_url = file_element.get_attribute("data-url")
                files[file_name] = file_url
        elif type == "SW3":
            file_elements = driver.find_elements(By.CSS_SELECTOR, ".file li")
            for file_element in file_elements:
                file_name = file_element.find_element(By.CSS_SELECTOR, ".fname").text.strip()
                file_url = file_element.get_attribute("data-url")
                files[file_name] = file_url
    except Exception as e:
        print("오류발생 첨부파일:", e)
    
    # 이미지 링크
    try:
        if type == "JBNU1" or type == "EMPLOY":
            img_urls = driver.find_elements(By.CSS_SELECTOR, "div.artclView p img")
            for url_elements in img_urls:
                img_url.append(url_elements.get_attribute('src'))
        elif type == "JBNU2":
            img_elements = driver.find_element(By.CSS_SELECTOR, 'div.com-post-content-01.no-border-bottom').find_elements(By.CSS_SELECTOR, 'img')
            for img in img_elements:
                img_url.append(img.get_attribute('src'))
        elif type == "Link1" or type == "Link2":
            img_url.append(driver.find_element(By.CSS_SELECTOR, "img.card-image").get_attribute("src"))
        elif type == "SW1" or type == "SW2":
            img_url.append(driver.find_element(By.CSS_SELECTOR, ".content_wrap img").get_attribute("src"))
        elif type == "SW3":
            img_url.append(driver.find_element(By.CSS_SELECTOR, "div.program_viewbox div.cont p img").get_attribute("src"))
        elif type == "BIGDATA":
            img_urls = driver.find_elements(By.CSS_SELECTOR, "div.board_con_txt img")
            for url_elements in img_urls:
                img_url.append(url_elements.get_attribute("src"))
    except Exception as e:
        print("오류발생 이미지 링크:", e)
    
    # 지원 링크
    try:
        if type == "Link1" or type == "Link2":
            apply_url = click_button_and_get_info(driver)
    except Exception as e:
        print("오류발생 지원 링크:", e)

    if not files:
        files = None
    if not img_url:
        img_url = None

    data = {
        "제목":title, 
        "작성자":writer, 
        "작성일":format_date(write_date) if write_date else None,
        "접수기간":format_date(recept_date) if recept_date else None,
        "활동기간":format_date(activity_date) if activity_date else None,
        "모집인원":recept_num, 
        "활동지역":activity_region, 
        "내용":content, 
        "첨부파일":files, 
        "이미지 링크":img_url, 
        "지원 링크":apply_url, 
        "URL":url
    }
    
    return data