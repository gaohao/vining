import storm

from bs4 import BeautifulSoup
from pprint import pprint
import requests
import urllib2
import urllib
import datetime


class ViningSpout(storm.Spout):  
    def initialize(self, conf, context):
        pass
    
    def nextTuple(self):
        try:
            url = "https://twitter.com/search/realtime?q=vine.co%2Fv%2F+%2B+"+"cat" + "&src=typd"
            html = urllib2.urlopen(url).read()
            soup = BeautifulSoup(html)
            vine_url_array=[]
            vine_dict={}
        
            for instance in soup.find_all('span',{'class' : 'js-display-url'}):
                vine_url = instance.get_text()
                vine_url_array.append(vine_url)
                #print vine_url_array    
            
            for i in vine_url_array:
                i='http://'+i
                soupe = BeautifulSoup(urllib2.urlopen(i).read())
                link = soupe.source['src']
                title = soupe.p.get_text()
                vine_dict[title]=link
                storm.emit([link])
                
        except StopIteration:
            pass
ViningSpout().run()




