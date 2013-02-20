import storm

from bs4 import BeautifulSoup
from pprint import pprint
import requests
import urllib2
import urllib
import datetime


class ViningSpout(storm.Spout):  
    def initialize(self, conf, context):
        self.query = None;
        if self.query != None:
            sefl.url = "https://twitter.com/search/realtime?q=vine.co%2Fv%2F+%2B+" \
                 + self.query + "&src=typd";
        else:
            self.url = "https://twitter.com/search/realtime?q=vine.co%2Fv%2F+%2B+&src=typd";
    
    def nextTuple(self):
        try:       
            html = urllib2.urlopen(self.url).read()
            soup = BeautifulSoup(html)
            vine_url_array=[]
            vine_dict={}
        
            for instance in soup.find_all('span',{'class' : 'js-display-url'}):
                vine_url = instance.get_text()
                vine_url_array.append(vine_url) 
            
            for i in vine_url_array:
                i='http://'+i
                soupe = BeautifulSoup(urllib2.urlopen(i).read())
                link = soupe.source['src']
                title = soupe.p.get_text()
                vine_dict[title]=link
                storm.emit([link])
            time.sleep(3)
        except StopIteration:
            pass
        except:
            pass
ViningSpout().run()




