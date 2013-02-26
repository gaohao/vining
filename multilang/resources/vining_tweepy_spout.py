import storm
from bs4 import BeautifulSoup
from pprint import pprint
import requests
import urllib2
import urllib
import datetime
import tweepy
import json
import time

class ViningSpout(storm.Spout):  
    def initialize(self, conf, context):
        self.api = tweepy.API()
    
    def nextTuple(self):
        try:       
            for tweet in self.api.search(q ="vine.co/v", rpp = 6, result_type="recent", include_entities=True):
                if tweet.entities != None and 'urls' in tweet.entities and \
                    len(tweet.entities['urls']) > 0 and 'expanded_url' in tweet.entities['urls'][0]:
                    vine_url = tweet.entities['urls'][0]['expanded_url']
                    soupe = BeautifulSoup(urllib2.urlopen(vine_url).read())
                    if soupe.source != None:
                        link = soupe.source['src']
                        storm.emit([tweet.id_str, tweet.text, tweet.created_at.isoformat(), link])
            time.sleep(2)
        except StopIteration:
            pass
        except urllib2.HTTPError, err:
            if err.code == 404:
                pass
        except tweepy.TweepError, err:
            pass
ViningSpout().run()




