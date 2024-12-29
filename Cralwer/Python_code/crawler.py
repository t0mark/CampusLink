from login import login
from access_post import access_post
from scrape_data import scrape_data
from firebase_uploader import upload_to_firebase

def crawler(site_name, url, type):
    driver = login(type)
    urls = access_post(site_name, url, type, driver)
    for post_num, post_url in urls:
        data = scrape_data(site_name, post_num, post_url, type, driver)

        if type == "JBNU1" or type == "JBNU2" or type == "SW1" or type == "SW2" or type == "BIGDATA" or type == "EMPLOY":
            document_id = f"{data["작성일"]}_{data["제목"]}"
        elif type == "Link1" or type == "Link2" or type == "SW3":
            document_id = f"{data["접수기간"].split('~')[0].strip()}_{data["제목"]}"
        
        upload_to_firebase(site_name, document_id, data)
    
    driver.quit()
