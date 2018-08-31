import threading
from extras import calc_dist_km as dist, RepeatedTimer
from flask import Flask, request, make_response, jsonify
import pandas as pd
from updater import updater
import json

app = Flask(__name__)
log = app.logger

data = pd.read_csv('database.csv')


#rt = RepeatedTimer(300, updater, data) # it auto-starts, no need of rt.start()

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
