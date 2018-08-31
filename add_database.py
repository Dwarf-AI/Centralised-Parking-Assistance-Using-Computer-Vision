import pandas as pd
from os.path import exists 
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

data.loc[0] = None
data.loc[0].spot_id = input('what is spot_id?\n')
data.loc[0].latitude = input('what is latitude?\n')
data.loc[0].longitude = input('what is longitude?\n')
data.loc[0].rows = int(input('No. of rows?\n'))
data.loc[0].cols = int(input('No. of cols?\n'))
data.loc[0].available = [1 for i in range(data.loc[0].rows*data.loc[0].cols)]
data.loc[0].slot_available = data.loc[0].rows*data.loc[0].cols
data.loc[0].coordinates = []

with open('bounding_boxes.txt') as f:
    texts = f.read()

texts = texts.split('\n')

for text in texts:
    data.loc[0].coordinates.append([int(i) for i in text.split()])

data.loc[0].price = input('What is price?\n')
data.loc[0]['name'] = input('What is name?\n')
data.loc[0].contact = input('What is contact?\n')

if exists('add_database.csv'):
    new_data = pd.read_csv('add_database.csv')
    data = data.append(new_data, ignore_index=True)

data.to_csv('add_database.csv',index=False)