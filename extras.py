import numpy as np
import pandas as pd
from math import sin, cos, sqrt, atan2, radians
from threading import Timer
import time

# approximate radius of earth in km
R = 6373.0

def calc_dist_km(lat1, lon1, lat2, lon2):
    """
    Returns distance in km if latitude and longitude of two points are given.
    """
    # lat1, lon1 = lat1.values, lon1.values
    print('something')
    dlon = lon2 - lon1
    dlat = lat2 - lat1

    print('some')
    a = np.sin(dlat / 2)**2 + np.cos(lat1) * np.cos(lat2) * np.sin(dlon / 2)**2
    c = 2 * np.arctan2(np.sqrt(a), np.sqrt(1 - a))

    return R * c
