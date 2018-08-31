import numpy as np
import pandas as pd
from math import sin, cos, sqrt, atan2, radians
from threading import Timer

# approximate radius of earth in km
R = 6373.0


class RepeatedTimer(object):
    def __init__(self, interval, function, *args, **kwargs):
        self._timer     = None
        self.interval   = interval
        self.function   = function
        self.args       = args
        self.kwargs     = kwargs
        self.is_running = False
        self.start()

    def _run(self):
        self.is_running = False
        self.start()
        self.function(*self.args, **self.kwargs)

    def start(self):
        if not self.is_running:
            self._timer = Timer(self.interval, self._run)
            self._timer.start()
            self.is_running = True

    def stop(self):
        self._timer.cancel()
        self.is_running = False


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
