import matplotlib.pyplot as plt
from matplotlib.pyplot import imread,imshow
import pandas as pd
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

data = pd.DataFrame(columns=['spot_id','latitude','longitute','rows','cols','slot_available','coordinates','available','price','name','contact'])

data.loc[0] = [
    1,
    28.5946,
    77.31994569999999,
    4,
    2,
    3,
    [
        [95,40,194,111],
        [95,140,195,198],
        [95,216,195,265],
        [93,309,195,380],
        [238,35,329,108],
        [231,131,334,201],
        [231,240,335,299],
        [228,309,338,380]
    ],
    [1, 1, 0, 0, 0, 0, 1, 0],
    1000,
    "KunalPark",
    "999468758"
]

IMAGE_DIR = 'test_image/dropbox_image.jpeg'
CHECKPOINT_DIR = '../weights/checkpoint-07-0.07.hdf5'

if not exists(IMAGE_DIR):
    print('Image file missing... Exiting!!')
    sys.exit(0)

if not exists(CHECKPOINT_DIR):
    print('Checkpoint file missing... Exiting!!')
    sys.exit(0)

model = load_model(CHECKPOINT_DIR, custom_objects={'LocalResponseNormalization': LocalResponseNormalization})

while True:
    f = plt.figure(figsize=(100,150))
    f.add_subplot(1,2, 1)
    im = imread(IMAGE_DIR)
    plt.imshow(im)
    im = Image.fromarray(im)
    im = im.resize((500, 400))
    im = np.array(im)

    images = []
    for cord in data['coordinates'][0]:
      im_ = Image.fromarray(im[cord[1]:cord[3],cord[0]:cord[2]])
      im_ = im_.resize((54, 32))
      im_ = np.array(im_)
      im_ = im_.transpose(1,0,2)
      images.append(im_) 

    images = np.array(images)

    predictions = model.predict(images, verbose=1)
    predictions = np.hstack(predictions < 0.5).astype(int)

    i = 0
    im_ = np.copy(im)
    for cord in data['coordinates'][0]:
      im_ = cv2.rectangle(im_,(cord[0],cord[1]),(cord[2],cord[3]),(255*int(predictions[i]),255*int(1-predictions[i]),0),2)
      i += 1
    f.add_subplot(1,2, 2)
    imshow(im_)
    break

