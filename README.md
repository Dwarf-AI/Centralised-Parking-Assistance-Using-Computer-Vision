
[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://forthebadge.com)
[![forthebadge](https://forthebadge.com/images/badges/made-with-python.svg)](https://forthebadge.com)

[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=shields)](http://makeapullrequest.com)
![Python Version](https://img.shields.io/badge/pypi-python3.6-blue.svg)

# Centralised Parking Assistance Using Computer Vision

<p align="center">
<img src="https://github.com/i-amgeek/MoveHack/blob/master/images/app_logo.png" width = 380 height = 300/>
</p>

## Problem statement
Artificial Intelligence for Indian Transport Infrastructure

## Inspiration
The high demand of automobiles has also exploded the need of parking space. Over 30% of traffic slowdown is caused by parking difficulties taking around 20-25 minutes on average per person. If we could help them, the driving would reduce dramatically, releasing less Carbon Dioxide and clearing up the roads from parking spot hunters. We believe in solutions that are available for all cities to become smart cities.

## Idea
We will build a system using computer vision that will parse parking space data in the cloud and will retrieve to a consumer app that will help the driver to find a parking spot around. All of this will be automatic, without any human intervention.

## Implementation
* A camera at parking space will take images after regular interval.

<p align="center">
<img src="https://raw.githubusercontent.com/kunalgoyal9/MoveHack/master/images/read_me_image.jpeg" width= "300" height = "300"/>
</p>

* Feed those images to a deep learning architecture to detect available parking spots.
<p align="center">
<img src="https://raw.githubusercontent.com/kunalgoyal9/MoveHack/master/images/KunalPark2.png" width = "300" height = "300"/>
</p>


* Data from all such parking spaces in city will be parsed to centralized cloud database.
* Any driver in the city can see nearest available parking spot for their vehicle on their mobile devices.

<p align="center">
<img src="https://github.com/i-amgeek/MoveHack/blob/master/images/Screenshot1.jpg" width = "250" height = "400"/>
</p>

* Driver can select parking spot to view details, available slots and book them.

## Model Architecture
We choose to fine-tune the pre-trained VGGNet (specifically its F-variant). The pre-trained weights can be obtained from [here](http://www.vlfeat.org/matconvnet/models/imagenet-vgg-f.mat).
We fix the convolutional layers, i.e., we don't fine-tune the convolutional layers, only the dense layers above it. You can download checkpoint for testing the image directly from [here](https://drive.google.com/open?id=0B76BuJcKjuxqYXRmSzd2R3U4S2c)

## Dataset
We used the PKLot dataset, which can be found [here](http://www.inf.ufpr.br/lesoliveira/download/pklot-readme.pdf). This database contains 12,417 images (1280X720) captured 
from two different parking lots (parking1 and parking2) in sunny, cloudy and rainy days. On using the annotations to get the
image of each parking lot, we end up with ~ 695600 images of size 54x32.

## Dependencies
- [Keras](http://keras.io/)
- [scipy](https://www.scipy.org/)
- [matplotlib](https://matplotlib.org/)
- [numpy](www.numpy.org/)
- [PIL](www.pythonware.com/products/pil/)
- [OpenCV](http://opencv.org/)
- [Tensorflow](https://tensorflow.org)

Use [pip](https://pypi.python.org/pypi/pip) to install them.

## Testing
```sh
$ git clone --recursive https://github.com/i-amgeek/MoveHack
$ python3.6 -m pip3 install -r requirements.txt
$ cd test\ model
$ wget -P weights/ 'https://drive.google.com/open?id=0B76BuJcKjuxqYXRmSzd2R3U4S2c'
$ python3.6 test.py
```

## Todo
* Add payment gateway.
* Use Model Compression to make object detection faster.
