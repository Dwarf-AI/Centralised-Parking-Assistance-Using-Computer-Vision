# import matplotlib.pyplot as plt
from scipy.misc import imread, imshow
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
import json

def updater(database):
  
  if exists('add_database.csv'):
    new_data = pd.read_csv('add_database.csv')
    new_data.coordinates = new_data.coordinates.apply(lambda s: json.loads(s))
    new_data.available = new_data.available.apply(lambda s:json.loads(s))
    database = database.append(new_data, ignore_index=True)
    os.remove('add_database.csv')
    database.to_csv('database.csv',index=False)
#   print(database)
  for index,row in database.iterrows():
    spot_id = str(row['spot_id'])
    url = "https://api.dropboxapi.com/2/files/list_folder"

    payload = f"{{\"path\": \"/Apps/IP Webcam/{spot_id}\"}}"
   
    headers = {
        'authorization': "Bearer ICLv-EbdL4AAAAAAAAAKAk5IlZiYC5Utddov30v9c0-C_0lrtMY0Y9iZFmsfmK8J",
        'content-type': "application/json",
        'cache-control': "no-cache"
        }

    response = requests.request("POST", url, data=payload, headers=headers)

    response = response.json()

    photos = []

    for dic in response['entries']:
      photos.append(dic['name'])

    photos = sorted(photos)

    print(photos)


    url = "https://content.dropboxapi.com/2/files/download"

    headers = {
        'authorization': "Bearer ICLv-EbdL4AAAAAAAAAKAk5IlZiYC5Utddov30v9c0-C_0lrtMY0Y9iZFmsfmK8J",
        'dropbox-api-arg': f"{{\"path\": \"/Apps/IP Webcam/{spot_id}/{photos[-1]}\"}}",
        'cache-control': "no-cache",
        'postman-token': "07aafc5e-9960-4da2-e328-66f0aa434802"
        }

    response = requests.request("POST", url, headers=headers, stream = True)

    IMAGE_DIR = 'images/dropbox_image.jpeg'

    with open(IMAGE_DIR, 'wb') as f:
          shutil.copyfileobj(response.raw, f)


    MAGIC = "face600d"

    counter = 0
    server_host = "192.168.43.20"
    server_port = 5000
    total_time = 0
    
    CHECKPOINT_DIR = 'weights/checkpoint-07-0.07.hdf5'

    if not exists(IMAGE_DIR):
        print('Image file missing... Exiting!!')
        sys.exit(0)

    if not exists(CHECKPOINT_DIR):
        print('Checkpoint file missing... Exiting!!')
        sys.exit(0)

    model = load_model(CHECKPOINT_DIR, custom_objects={'LocalResponseNormalization': LocalResponseNormalization})

    while True:
        im = imread(IMAGE_DIR)
    #     imshow(im)
        im = Image.fromarray(im)
        im = im.resize((500, 400))
        im = np.array(im)

        images = []
        for cord in row['coordinates']:
#           print(cord)
          im_ = Image.fromarray(im[cord[1]:cord[3],cord[0]:cord[2]])
          im_ = im_.resize((54, 32))
          im_ = np.array(im_)
          im_ = im_.transpose(1,0,2)
          images.append(im_) 

        images = np.array(images)

        predictions = model.predict(images, verbose=1)
#         print(predictions)
        predictions = np.hstack(predictions < 0.5).astype(int)
#         print(predictions)
        
        predictions = 1 - predictions
        database.at[index,'available'] = predictions
        database.at[index,'slot_available'] = np.sum(predictions)
#         i = 0
#         im_ = np.copy(im)
#         for cord in row['coordinates']:
#           im_ = cv2.rectangle(im_,(cord[0],cord[1]),(cord[2],cord[3]),(255*int(predictions[i]),255*int(1-predictions[i]),0),2)
#           i += 1
#         plt.imshow(im_)
        break
        