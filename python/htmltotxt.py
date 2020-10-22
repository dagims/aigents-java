import argparse
import sys
from bs4 import BeautifulSoup
import requests
def main(args):

	#url = "https://www.sciencenews.org/author/aimee-cunningham"
	parser = argparse.ArgumentParser()
        parser.add_argument('url', type=str)
        args = parser.parse_args(args)
        USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:65.0) Gecko/20100101 Firefox/65.0"
        headers = {"user-agent": USER_AGENT}

        req = requests.get(args.url,headers=headers)
	soup = BeautifulSoup(req.text, "html.parser")
	summary=""		
        #soup.p.decompose()
        
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

        for cont in soup.find_all("p"):
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


        #summary=summary.replace(".",". ")
	
        print(summary.encode('utf-8'))

if __name__ == "__main__":
    main(sys.argv[1:])

