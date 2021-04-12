import random
from random import choices
from random import choice
import numpy as np
import pandas as pd
import re
from google.colab import drive
drive.mount('/content/drive')

##file = open("stop_times3.txt", "r")
##with open("stop_times3.txt", "r") as stopFile, open("stopstest.txt", "r", encoding="utf8") as busFile: 


stopFile = open("/content/drive/My Drive/Colab Notebooks/stop_times.txt", "r")
busFile = open("/content/drive/My Drive/Colab Notebooks/stopstest.txt", "r", encoding="utf8")

tripid = []
route = []
##refId = ""
bus_stops = []
stop_dict = {}
route_with_stops = {}
busstop = {}
##print(refId)


def generate_stopname(id, busFile):

    result = ""

    for line in busFile:
        entry = re.split(r',(?=")', line)
        stopid =  str(entry[0])
        stopIdString = stopid[1:13]
        busstop[stopIdString] = entry[1]
       

    a = "700000015376" in busstop
   # print(a)

    tempid =  str(id)
    tempIdString = tempid[1:13] #change from 3 to 1 and from 15 to 13
       
#    print(busstop) # this prints the entire dictionary

   # print(tempIdString)
  #  print(busstop[tempIdString])
    try:
      result = busstop[tempIdString]
    except KeyError:
      print("Key Error Occured", tempIdString)
    return result


def generate_table(route_with_stops):

    route_data_list = []


    route = route_with_stops.keys()

    for r in route:
        
        stops = route_with_stops[r]

        route_row = [r]*60 
        route_row = np.array(route_row).flatten()
        days = ["mon", "tues", "wed", "thurs", "fri", "sat", "sun"]
        days = [[d]*12 for d in days]
        days = np.array(days).flatten()
        times = ["07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00","14:00","15:00","16:00","17:00","18:00"]

        times = [[t] for t in times]*5
        times = np.array(times).flatten()
        #times = list(np.arange(1,13))*5 #should 5 or 12 instead?

        #bus_occupant = [choices(outcomes,weights) for _ in range(len(times))]

        def generate_occupancy(day_time):
            
            if day_time == '07:00' or day_time == '08:00':  #compress all using 'or'
                passholders = 10 # percentage of passholders
                    
            elif day_time == '09:00' or day_time == '10:00' or day_time == '11:00':        
                passholders = 78 # percentage of passholders


            elif day_time == '12:00' or day_time == '13:00' or day_time == '14:00' or day_time == '15:00':
                passholders = 65 # percentage of passholders
                    
            elif day_time == '16:00' or day_time == '17:00':
                passholders = 15 # percentage of passholders
                    
            elif day_time == '18:00': 
                passholders = 15 # percentage of passholders
                    
            disabled_percent = (10/100)*passholders # 10 percentage of passholders are disabled (from transport from london report) 
            disabled_probability = disabled_percent/100    # conversion from percentage to non-percentage              
            outcomes = [0, 1]
            weights = [1-disabled_probability, disabled_probability]
            occupancy = np.random.choice(outcomes, p=weights)   
            #occupancy = choice(outcomes, p=weights)    

            
            return occupancy
                    

        bus_occupant = [generate_occupancy(t) for t in times]


        bus_occupant = np.array(bus_occupant).flatten()

        dupl = bus_occupant
        departure = np.where(bus_occupant==1, [random.choice(stops) for _ in range(len(times))],"NA")

        departure = np.array(departure).flatten()

        destination = np.where(dupl==1, [random.choice(stops) for _ in range(len(times))],"NA")


        destination = np.where((dupl==1) & (destination==departure), [random.choice(stops) for _ in range(len(times))],destination)

        destination = np.array(destination).flatten()


        df=pd.DataFrame(zip(route_row,times,days,bus_occupant,departure,destination), columns=["route", "times", "days", "occupancy","departure","destination"])

        print(df.shape)
        route_data_list.append(df)

    full_data = pd.concat(route_data_list)

    print(full_data)
    
    #exports dataframe to csv
    #full_data.to_csv(r'C:\Users\UCD\Desktop\YEAR 4\wheelchair_occupancy.csv', index = False, header=True)
    full_data.to_csv('/content/drive/My Drive/Colab Notebooks/wheelchair_occupancy.csv', index = False, header=True )

        

def process_stops(stopFile):
    refId = ""
    
    for line in stopFile:
        splitLine = line.split(",")
      #  x = re.findall("[6][0][-]",splitLine[0])
        x = ".60-" in splitLine[0]
        y = re.findall("[-][d]",splitLine[0])
        y = "-d" in splitLine[0]

        print(x)
        print(y)

        

        if x:
            print("Yes")

            splitLine[0].find("\.60-")
            stringRoute = str(splitLine[0])

            if not refId:
                refId = stringRoute
            #    print("not entered")
            else:
                print("value is entered")

            posDigit = stringRoute.find("60-")
          #  print(posDigit)
            posHyphen = stringRoute.find("-d")
         #   print(posHyphen)
            queryNo = posDigit + 3
            result = stringRoute[queryNo:posHyphen]

            stop_dict.setdefault(result,[])
            stop_dict[result].append(splitLine[3])
                
        else:
            print("No")
            
#    print(route)

#    print(len(bus_stops))

#    print(stop_dict.keys() )

#    print(stop_dict["13"])

#    print(stop_dict["123"])

    first_items = []

    id_stops = stop_dict["123"]

    print('Test')

#    print( id_stops)

    for key in stop_dict:
        id_stops = stop_dict[key]      
        for i in id_stops:
            name_stop = generate_stopname(i, busFile)
            #print(name_stop)
            route_with_stops.setdefault(key,[])
            route_with_stops[key].append(name_stop)

  #  print(route_with_stops)
    generate_table(route_with_stops)
        
#        i

#    name_stop = generate_stopname(id_stops, busFile)

#    print(name_stop)




process_stops(stopFile)
    

stopFile.close()
busFile.close()