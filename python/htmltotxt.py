import argparse
import sys
from bs4 import BeautifulSoup
import requests
import re
def main(args):
    parser = argparse.ArgumentParser()
    parser.add_argument('url', type=str)
    args = parser.parse_args(args)
    USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:65.0) Gecko/20100101 Firefox/65.0"
    headers = {"user-agent": USER_AGENT}
    req = requests.get(args.url,headers=headers)
    soup = BeautifulSoup(req.text, "html.parser")
    summary=""		
    rm=[]
    class_names=["header", "footer", "nav", "side", "aside", "menu", "layout","caption","figure"]
    classes=[]    
    
    for i in range(7):
        try:
            soup.footer.decompose()
        except:
            pass

        try:
            soup.header.decompose() 
        except:
            pass
    
        try:
            soup.nav.decompose()
        except:
            pass
    
        try:
            soup.aside.decompose()
        except:
            pass

        try:
            soup.figure.decompose()
        except:
            pass


    body=soup.body.text
    for class_name in class_names:
        all_classes=soup.find_all(class_=re.compile(class_name))
        for item in all_classes:
            body=soup.body.text
            cont=str(item.text)
            ext=body.replace(cont,"")            
            if len(ext)<300:
                all_classes.remove(item)

        classes+=all_classes
    for cont in classes:
        rm+=cont.find_all("p")
    pclass=""
    temp2=0
    for cont in soup.find_all("p"):
        temp2=0
        try:
            pclass=cont['class']
            pclass=pclass[0]
            for class_name in class_names:
                if class_name in pclass:
                    temp2=1
                    break    
        except:
            pass
        if cont not in rm and temp2==0:
            temp=cont.text
            try:
                if temp[-1]!=".":
                    temp+=". "
                else:
                    temp+="  "
            except:
                pass
            summary+=temp
            summary=summary.replace("\n"," ")
        pclass=""
    print(summary)
if __name__ == "__main__":
    main(sys.argv[1:])

