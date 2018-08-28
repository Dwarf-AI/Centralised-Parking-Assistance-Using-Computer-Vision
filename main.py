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
# updater(data)


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
        "rows": str(data[data['spot_id'] == idx].rows[0]),
        "cols": str(data[data['spot_id'] == idx].cols[0]),
        "available": list(map(str, data[data['spot_id'] == idx].available.values[0])),
        "price": str(data[data['spot_id'] == idx].price[0])
        }

    return make_response(jsonify(res))


if __name__ == "__main__":
    app.run(debug=True, port=4700)
