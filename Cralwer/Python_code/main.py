# main.py
from settings import JBNU1, JBNU2, Link1, Link2, SW1, SW2, SW3, BIGDATA, EMPLOY
from crawler import crawler

if __name__ == "__main__":
   for site_name, urls in JBNU1.items():
      for url in urls:
         crawler(site_name, url, "JBNU1")
   for site_name, url in JBNU2.items():
      crawler(site_name, url, "JBNU2")
   for site_name, url in Link1.items():
      crawler(site_name, url, "Link1")
   for site_name, url in Link2.items():
      crawler(site_name, url, "Link2")
   for site_name, url in SW1.items():
      crawler(site_name, url, "SW1")
   for site_name, url in SW2.items():
      crawler(site_name, url, "SW2")
   for site_name, url in SW3.items():
      crawler(site_name, url, "SW3")
   for site_name, url in BIGDATA.items():
      crawler(site_name, url, "BIGDATA")
   for site_name, url in EMPLOY.items():
      crawler(site_name, url, "EMPLOY")
