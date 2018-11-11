import csv
import requests

with open('houses.csv') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    line_count = 0
    result = []
    for row in csv_reader:
        if line_count == 0:
            line_count += 1
        else:
            delminatedAddress = row[1].replace(" ", "")
            url = ('https://maps.googleapis.com/maps/api/geocode/json?address=%s,+Waltham,+MA&key=AIzaSyDJmPTuIR8t4ykM3XPswmTR1Ak4aHWpmyg' % delminatedAddress)
            response = requests.get(url)
            responseAsJson = response.json()
            lat = responseAsJson["results"][0]["geometry"]["location"]["lat"]
            lng = responseAsJson["results"][0]["geometry"]["location"]["lng"]
            weight = int(row[4]) / 10
            print("{location: new google.maps.LatLng(%s, %s), weight: %s}," %
                  (lat, lng, weight))
