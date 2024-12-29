import os

BASE_DIR = os.path.dirname(os.path.abspath(__file__))
CHROME_DRIVER_PATH = os.path.join(BASE_DIR, "chromedriver-win64\\chromedriver.exe")
FILE_PATH = os.path.join(BASE_DIR, "last_scraped_posts.txt")

# 학교 URL
JBNU1 = {
    "단과대학-공과대학-정보광장-공지사항": ["https://eng.jbnu.ac.kr/eng/8113/subview.do"],
    "단과대학-공과대학-학부/학과-전자공": ["https://eei.chonbuk.ac.kr/eei/10634/subview.do", 
                                            "https://eei.chonbuk.ac.kr/eei/24655/subview.do",
                                            "https://eei.chonbuk.ac.kr/eei/10635/subview.do"],
    "단과대학-공과대학-학부/학과-컴인지": ["https://csai.jbnu.ac.kr/csai/29107/subview.do", 
                                            "https://csai.jbnu.ac.kr/csai/29049/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGY3NhaSUyRjQ5MjYlMkZhcnRjbExpc3QuZG8lM0Y%3D"],
    "단과대학-공과대학-학부/학과-기계설계": ["https://mdesign.jbnu.ac.kr/mdesign/8365/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGbWRlc2lnbiUyRjEyMDIlMkZhcnRjbExpc3QuZG8lM0Y%3D", 
                                            "https://mdesign.jbnu.ac.kr/mdesign/8361/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGbWRlc2lnbiUyRjEyMDAlMkZhcnRjbExpc3QuZG8lM0Y%3D", 
                                            "https://mdesign.jbnu.ac.kr/mdesign/14078/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGbWRlc2lnbiUyRjIxNzUlMkZhcnRjbExpc3QuZG8lM0Y%3D"],
    "단과대학-자연과학대학-정보광장-공지사항": ["https://natural.jbnu.ac.kr/natural/7453/subview.do"],
    "단과대학-자연과학대학-학부/학과-물리학과": ["https://physics.jbnu.ac.kr/physics/5950/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGcGh5c2ljcyUyRjcyMyUyRmFydGNsTGlzdC5kbyUzRg%3D%3D",
                                                "https://physics.jbnu.ac.kr/physics/5949/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGcGh5c2ljcyUyRjcwMiUyRmFydGNsTGlzdC5kbyUzRg%3D%3D",
                                                "https://physics.jbnu.ac.kr/physics/5955/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGcGh5c2ljcyUyRjcyNyUyRmFydGNsTGlzdC5kbyUzRg%3D%3D"],
    "단과대학-자연과학대학-학부/학과-수학과": ["https://math.jbnu.ac.kr/math/6133/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGbWF0aCUyRjc0MiUyRmFydGNsTGlzdC5kbyUzRg%3D%3D"],
    "단과대학-자연과학대학-학부/학과-통계학과": ["https://stat.jbnu.ac.kr/stat/6442/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGc3RhdCUyRjgzNCUyRmFydGNsTGlzdC5kbyUzRg%3D%3D",
                                                "https://stat.jbnu.ac.kr/stat/6439/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGc3RhdCUyRjgzNSUyRmFydGNsTGlzdC5kbyUzRg%3D%3D"]
}

JBNU2 = {
    "전북대포털-교내공지": "https://www.jbnu.ac.kr/web/news/notice/sub01.do",
    "전북대포털-학생공지": "https://www.jbnu.ac.kr/web/news/notice/sub02.do",
    "전북대포털-교내채용": "https://www.jbnu.ac.kr/web/news/notice/sub03.do"
}

# 링커리어 URL
Link1 = {
    "대외활동-활동분야-서포터즈" : "https://linkareer.com/list/activity?filterBy_categoryIDs=1&filterType=CATEGORY&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-활동분야-해외탐방(무료)" : "https://linkareer.com/list/activity?filterBy_categoryIDs=2&filterType=CATEGORY&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-활동분야-해외탐방(유료)" : "https://linkareer.com/list/activity?filterBy_categoryIDs=3&filterType=CATEGORY&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-활동분야-봉사단(해외)" : "https://linkareer.com/list/activity?filterBy_categoryIDs=4&filterType=CATEGORY&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-활동분야-봉사단(국내)" : "https://linkareer.com/list/activity?filterBy_categoryIDs=5&filterType=CATEGORY&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-활동분야-마케터" : "https://linkareer.com/list/activity?filterBy_categoryIDs=6&filterType=CATEGORY&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-활동분야-기자단" : "https://linkareer.com/list/activity?filterBy_categoryIDs=7&filterType=CATEGORY&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-활동분야-강연" : "https://linkareer.com/list/activity?filterBy_categoryIDs=9&filterType=CATEGORY&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-활동분야-멘토링" : "https://linkareer.com/list/activity?filterBy_categoryIDs=10&filterType=CATEGORY&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-활동분야-기타" : "https://linkareer.com/list/activity?filterBy_categoryIDs=11&filterType=CATEGORY&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-관심분야-언론/미디어" : "https://linkareer.com/list/activity?filterBy_interestIDs=2&filterType=INTEREST&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-관심분야-문화/역사" : "https://linkareer.com/list/activity?filterBy_interestIDs=3&filterType=INTEREST&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-관심분야-경제/금융" : "https://linkareer.com/list/activity?filterBy_interestIDs=7&filterType=INTEREST&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-관심분야-과학/공학/기술/IT" : "https://linkareer.com/list/activity?filterBy_interestIDs=13&filterType=INTEREST&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-관심분야-요리/식품" : "https://linkareer.com/list/activity?filterBy_interestIDs=14&filterType=INTEREST&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-관심분야-창업/자기계발" : "https://linkareer.com/list/activity?filterBy_interestIDs=15&filterType=INTEREST&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-활동기간-3개월이하" : "https://linkareer.com/list/activity?filterBy_period_to=90&filterType=PERIOD&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-활동기간-3개월~6개월" : "https://linkareer.com/list/activity?filterBy_period_from=90&filterBy_period_to=180&filterType=PERIOD&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-활동기간-6개월~1년" : "https://linkareer.com/list/activity?filterBy_period_from=180&filterBy_period_to=365&filterType=PERIOD&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "대외활동-활동기간-1년이상" : "https://linkareer.com/list/activity?filterBy_period_from=365&filterType=PERIOD&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1"
}

Link2 = {
    "공모전-공모분야-기획/아이디어" : "https://linkareer.com/list/contest?filterBy_categoryIDs=28&filterType=CATEGORY&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "공모전-공모분야-광고/마케팅" : "https://linkareer.com/list/contest?filterBy_categoryIDs=29&filterType=CATEGORY&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "공모전-공모분야-사진/영상/UCC" : "https://linkareer.com/list/contest?filterBy_categoryIDs=30&filterType=CATEGORY&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "공모전-공모분야-과학/공학" : "https://linkareer.com/list/contest?filterBy_categoryIDs=34&filterType=CATEGORY&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "공모전-수상혜택-입사가산점" : "https://linkareer.com/list/contest?filterBy_benefitIDs=12&filterType=BENEFIT&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "공모전-수상혜택-인턴/정규직채용" : "https://linkareer.com/list/contest?filterBy_benefitIDs=13&filterType=BENEFIT&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1",
    "공모전-수상혜택-해외연수/전시기회" : "https://linkareer.com/list/contest?filterBy_benefitIDs=14&filterType=BENEFIT&orderBy_direction=DESC&orderBy_field=CREATED_AT&page=1"
}

# SW중심대학 사업단
SW1 = {
    "SW중심대학-알림마당-공지사항" : "https://swuniv.jbnu.ac.kr/main/jbnusw?gc=605XOAS",
    "SW중심대학-알림마당-자료실" : "https://swuniv.jbnu.ac.kr/main/jbnusw?gc=831TMDN"
}

SW2 = {
    "SW중심대학-알림마당-사업단소식" : "https://swuniv.jbnu.ac.kr/main/jbnusw?gc=483ZJFR"
}

SW3 = {
    "SW중심대학-온라인 신청-프로그램 신청" : "https://swuniv.jbnu.ac.kr/main/jbnusw?gc=Program"
}

BIGDATA = {
    "빅데이터-커뮤니티-공지사항" : "https://bigdatahub.ac.kr/information/notice",
    "빅데이터-커뮤니티-뉴스" : "https://bigdatahub.ac.kr/information/news"
}

EMPLOY = {
    "취업진로처-취업정보-채용정보" : "https://career.jbnu.ac.kr/career/21893/subview.do",
    "취업진로처-취업정보-해외채용정보" : "https://career.jbnu.ac.kr/career/21894/subview.do",
    "취업진로처-취업정보-인턴정보" : "https://career.jbnu.ac.kr/career/21934/subview.do",
    "취업진로처-취업정보-공모전정보" : "https://career.jbnu.ac.kr/career/21935/subview.do",
    "취업진로처-커뮤니티-교내취업프로그램" : "https://career.jbnu.ac.kr/career/21946/subview.do",
    "취업진로처-커뮤니티-교외취업프로그램" : "https://career.jbnu.ac.kr/career/21947/subview.do"
}