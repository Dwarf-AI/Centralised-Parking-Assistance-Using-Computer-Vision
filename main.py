import threading
from extras import calc_dist_km as dist
from flask import Flask, request, make_response, jsonify
import pandas as pd
from updater import updater
import json

app = Flask(__name__)
log = app.logger
data = pd.DataFrame(columns=[
    "spot_id",
    "latitude",
    "longitude",
    "rows",
    "cols",
    "slot_available",
    "coordinates",
    "available",
    "price",
    "name",
    "contact",
])
data.loc[0] = [
    0,
    28.6190702,
    77.0250407,
    2,
    4,
    1,
    [
        [11, 60, 75, 198],
        [109, 59, 181, 200],
        [207, 60, 279, 205],
        [308, 58, 379, 195],
        [11, 215, 70, 350],
        [105, 217, 172, 353],
        [208, 232, 278, 355],
        [304, 229, 378, 354],
    ],
    [0, 0, 0, 0, 0, 0, 1, 0],
    100,
    "DeepakPark",
    "8802490936"
]

data.loc[1] = [
    1,
    28.3652058,
    77.31994569999999,
    4,
    2,
    3,
    [
        [95,40,194,111],
        [95,140,195,198],
        [95,236,195,295],
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


data.loc[2] = [
    2,
    28.5946,
    77.0184,
    3,
    3,
    9,
    [
        [80,87,177,153],
        [198,85,297,148],
        [317,84,445,150],
        [79,184,178,254],
        [192,185,297,256],
        [317,187,448,253],
        [79,271,182,344],
        [193,272,300,343],
        [316,275,449,340]
    ],
    [1, 1, 1, 1, 1, 1, 1, 1,1],
    200,
    "AbhishekPark",
    "1234567890"
]

updater(data)


@app.route("/", methods=["GET"])
def main():
    lat = float(request.args.get("lat"))
    lon = float(request.args.get("lon"))
    req_data = data[dist(data['latitude'], data['longitude'], lat, lon) < 5000]
    res = [{"id": str(d['spot_id']),
            "latitude": str(d['latitude']),
            "longitude": str(d['longitude']),
            "slot_available": str(d['slot_available']),
            "price": str(d['price'])} for index, d in req_data.iterrows()]
    return make_response(jsonify(res))


@app.route("/detail/", methods=["GET"])
def details():
    idx = int(request.args.get("id"))
    print('something')
    res = {
        "id": str(idx),
        "latitute": str(data[data['spot_id']==idx].latitude.values[0]),
        "longitude": str(data[data['spot_id']==idx].longitude.values[0]),
        "rows": str(data[data['spot_id'] == idx].rows.values[0]),
        "cols": str(data[data['spot_id'] == idx].cols.values[0]),
        "slot_available": str(data[data['spot_id']==idx].slot_available.values[0]),
        "available": list(map(str, data[data['spot_id'] == idx].available.values[0])),
        "price": str(data[data['spot_id'] == idx].price.values[0]),
        "name": str(data[data['spot_id']==idx].name.values[0]),
        "contact": str(data[data['spot_id']==idx].contact.values[0])
        }

    return make_response(jsonify(res))


if __name__ == "__main__":
    app.run(debug=True, port=4700)
