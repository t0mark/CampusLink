from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

from settings import FILE_PATH

def check_last_scraped_posts(site_name, url):
    # 형식: site 이름 | url | 게시물 번호
    with open(FILE_PATH, "r", encoding='utf-8') as file:
        for line in file:
            tmp = line.split("|")
            if tmp[0] == site_name and tmp[1] == url:
                return int(tmp[2])  # 찾은 경우 즉시 반환

    # 찾지 못한 경우 기본값 반환
    return -1

def record_last_scraped_posts(site_name, url, old_num, new_num):
    target_text = f"{site_name}|{url}|{old_num}"
    new_text = f"{site_name}|{url}|{new_num}"

    with open(FILE_PATH, "r+", encoding="utf-8") as file:
        content = file.read()  # 파일 전체 내용 읽기

        # target_text 찾기
        if target_text in content:
            updated_content = content.replace(target_text, new_text)
            file.seek(0)
            file.write(updated_content)
            file.truncate()
        else:
            # 찾지 못한 경우 새 줄 추가
            file.write(f"\n{new_text}")


def access_post(site_name, url, type, driver):
    old_num = check_last_scraped_posts(site_name, url)
    new_num = old_num
    
    post_num = None
    post_url = None

    driver.get(url)
    try:
        # 페이지 로드 완료 대기
        WebDriverWait(driver, 10).until(
            lambda d: d.execute_script("return document.readyState") == "complete"
        )
    except Exception as e:
        print(site_name, "url 연결 오류:", e)

    urls = []

    # 게시물 목록에서 게시물 추출
    if type == "JBNU1" or type == "EMPLOY":
        posts = driver.find_elements(By.CSS_SELECTOR, "tr[class='_artclEven'], tr[class='_artclOdd']")
    elif type == "JBNU2":
        posts = driver.find_elements(By.CSS_SELECTOR, "tr.tr-normal")
    elif type == "Link1" or type == "Link2":
        posts = driver.find_elements(By.CSS_SELECTOR, "a.image-link")
    elif type == "SW1":
        posts = driver.find_elements(By.CSS_SELECTOR, "tr:not([class])")[1:]
    elif type == "SW2":
        posts = driver.find_elements(By.CSS_SELECTOR, "ul.content_wrap a")
    elif type == "SW3":
        posts = driver.find_elements(By.CSS_SELECTOR, "ul.class_list_wrap li > a")
    elif type == "BIGDATA":
        posts = driver.find_elements(By.CSS_SELECTOR, "div.board_info")
    
    # 게시물 번호 추출
    # 게시물 URL 추출
    for post in posts:
        if type == "JBNU1" or type == "EMPLOY":
            try:
                post_num = int(post.find_element(By.CSS_SELECTOR, "td._artclTdNum").text)
                post_url = post.find_element(By.CSS_SELECTOR, "td._artclTdTitle a").get_attribute("href")
            except Exception as e:
                if type == "JBNU1":
                    print("오류발생 JBNU1:", e)
                elif type == "EMPLOY":
                    print("오류발생 EMPLOY:", e)
                continue

        elif type == "JBNU2":
            try:
                post_num = int(post.find_element(By.CSS_SELECTOR, "p.brd-num").text)
                post_url_element = post.find_element(By.CSS_SELECTOR, "td.td-title a").get_attribute("onclick").split("'")[1]
                post_url = f"https://www.jbnu.ac.kr/web/Board/{ post_url_element}/detailView.do?pageIndex=1&menu=2377"

            except Exception as e:
                print("오류발생 JBNU2:", e)
            
        elif type == "Link1" or type == "Link2":
            try:
                post_url = post.get_attribute("href")
                post_num = int(post_url.split('/')[-1])
            except Exception as e:
                print("오류발생 Link:", e)
        
        elif type == "SW1":
            try:
                post_url = post.find_element(By.CSS_SELECTOR, "td.article_title a").get_attribute("href")
                post_num = int(post.find_elements(By.CSS_SELECTOR, "td")[0].get_attribute("innerHTML"))
            except Exception as e:
                print("오류발생 SW1:", e)

        elif type == "SW2":
            try:
                post_url = post.get_attribute("href")
                post_num = int(post_url.split("=")[-1])
            except Exception as e:
                print("오류발생 SW2:", e)

        elif type == "SW3":
            try:
                post_num = None
                post_url = post.get_attribute("href")
                post_can = post.find_element(By.CSS_SELECTOR, "div.status").text

                if post_can == "신청하기":
                    urls.append((post_num, post_url))
                continue
            except Exception as e:
                print("오류발생 SW3:", e)
        
        if type == "BIGDATA":
            try:
                post_num = int(post.find_element(By.CSS_SELECTOR, "li div.board_info div.board_date").text.replace(".", ""))
                post_url = post.find_element(By.CSS_SELECTOR, "li div.board_info a").get_attribute("href")
            except Exception as e:
                print("오류발생 BIGDATA:", e)

        if type != "SW3" and post_num > old_num:
            # 반환 urls
            urls.append((post_num, post_url))
            # 저장할 새로운 게시물 번호
            new_num = max(new_num, post_num)

    if not (type == "SW3"):
        record_last_scraped_posts(site_name, url, old_num, new_num)

    return urls


