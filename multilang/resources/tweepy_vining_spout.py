import storm
from bs4 import BeautifulSoup
from pprint import pprint
import requests
import urllib2
import urllib
import datetime
import tweepy
import json

class ViningSpout(storm.Spout):  
    def initialize(self, conf, context):
        self.api = tweepy.API()
    
    def nextTuple(self):
        try:       
            for tweet in tweepy.Cursor(self.api.search,
                           q="vine.co/v",
                           rpp=100,
                           result_type="recent",
                           include_entities=True).items():
                vine_url = tweet.entities['urls'][0]['expanded_url']
                soupe = BeautifulSoup(urllib2.urlopen(vine_url).read())
                link = soupe.source['src']
                
                storm.emit([tweet.id_str, tweet.text, tweet.created_at.isoformat(), link])
            
            time.sleep(1)
        except StopIteration:
            pass
        except:
            pass
ViningSpout().run()




