import serial
#from firebase import firebase
import time
import requests
import json

#firebase = firebase.FirebaseApplication('https://projectgreenthumb-8cdc5.firebaseio.com/', None)

firebase_url = 'https://greenthumbhackathon.firebaseio.com/'

#Connect to Serial Port for communication
ser = serial.Serial('/dev/cu.usbmodem1411', 9600, timeout=1)

#Setup a loop to send Temperature values at fixed intervals
#in seconds
fixed_interval = 20
while 1:
  try:
    #temperature value obtained from Arduino + LM35 Temp Sensor

    sensor_reading = ser.readline();

    print sensor_reading
    #current time and date
    if sensor_reading:

            readings =  sensor_reading.split(';');

            if readings[0]:
             sunlight = readings[0];
            if readings[1]:
             humidity = readings[1];
            if readings[2]:
             temperature = readings[2];
             temperature = temperature.replace('\r\n',"");


            #current location name
            farm_location = 'Bloomington-Farm-Co';
            print sunlight + ',' + humidity + ',' + temperature;

            #insert record
            data = {'sunlight':sunlight,'humidity':humidity,'temperature':temperature}

            #result = firebase.post('/tempsensor',data,{'print': 'pretty'}, {'X_FANCY_HEADER': 'VERY FANCY'});
            result = requests.post(firebase_url + '/' + farm_location + '/sensoreadings.json', data=json.dumps(data))

            print 'Record inserted. Result Code = ' + str(result.status_code) + ',' + result.text
            time.sleep(fixed_interval)
    else:

            print 'Hello There!! No Sensor Reading recieved'

  except IOError:
    print('Error! Something went wrong.')
  time.sleep(fixed_interval)
