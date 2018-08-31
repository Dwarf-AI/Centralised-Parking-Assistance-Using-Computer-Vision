import matplotlib.pyplot as plt
from matplotlib.pyplot import imread,imshow
import matplotlib.image as mpimg
import numpy as np
from PIL import Image
from keras.models import load_model
from keras.engine.topology import Layer, InputSpec
from custom import LocalResponseNormalization
import cv2   
import os
from os.path import isfile, join, exists
import socket
import time
import sys
import shutil
import requests
import numpy as np
# from google.colab import files

url = "https://api.dropboxapi.com/2/files/list_folder"

#************assign new directory with new_spot_idx in dropbox and upload image to this directory*************

new_spot_idx = 2 #update it

payload = f"{{\"path\": \"/Apps/IP Webcam/{new_spot_idx}\"}}"

headers = {
    'authorization': "Bearer ICLv-EbdL4AAAAAAAAAKAk5IlZiYC5Utddov30v9c0-C_0lrtMY0Y9iZFmsfmK8J",
    'content-type': "application/json",
    'cache-control': "no-cache"
    }

response = requests.request("POST", url, data=payload, headers=headers)

response = response.json()

photos = set()

for dic in response['entries']:
  photos.add(dic['name'])

photos = list(photos)


url = "https://content.dropboxapi.com/2/files/download"

headers = {
    'authorization': "Bearer ICLv-EbdL4AAAAAAAAAKAk5IlZiYC5Utddov30v9c0-C_0lrtMY0Y9iZFmsfmK8J",
    'dropbox-api-arg': f"{{\"path\": \"/Apps/IP Webcam/{new_spot_idx}/{photos[-1]}\"}}",
    'cache-control': "no-cache",
    'postman-token': "07aafc5e-9960-4da2-e328-66f0aa434802"
    }

response = requests.request("POST", url, headers=headers, stream = True)

IMAGE_DIR = 'preprocess_spot_image/new_dropbox_image.JPEG'

with open(IMAGE_DIR, 'wb') as f:
      shutil.copyfileobj(response.raw, f)


im = plt.imread(IMAGE_DIR)
#     imshow(im)
    
im = Image.fromarray(im)

im = im.resize((500, 400))

im.save(IMAGE_DIR)

imshow(im)

#################### download this image using ########################################################

# files.download(IMAGE_DIR)

######## now put downloaded/saved image from preprocess_image drectory to BBox-Label-Tool-master/Images/001 ###########
########   now update information in database for the registered parking spot ##############################