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
import jsonpickle
from sys import argv
class ViningSpout(storm.Spout):  
    def initialize(self, conf, context):
        self.api = tweepy.API()
    
    def nextTuple(self):
        try:
            script, key_word = argv       
            
            if key_word == " ":
                key_word = "" 
            for tweet in self.api.search(q ="vine.co/v" + key_word, rpp = 6, result_type="recent", include_entities=True):
                if tweet.entities != None and 'urls' in tweet.entities and \
                    len(tweet.entities['urls']) > 0 and 'expanded_url' in tweet.entities['urls'][0]:
                    vine_url = tweet.entities['urls'][0]['expanded_url']
                    soupe = BeautifulSoup(urllib2.urlopen(vine_url).read())
                    if soupe.source != None:
                        link = soupe.source['src']
                        pickled = jsonpickle.encode(tweet)
                        storm.emit([tweet.id_str, tweet.created_at.isoformat(), link, json.dumps(json.loads(pickled), indent=4, sort_keys=True)])
            time.sleep(2)
        except StopIteration:
            pass
        except urllib2.HTTPError, err:
            if err.code == 404:
                pass
        except tweepy.TweepError, err:
            pass
        except:
            pass
ViningSpout().run()




