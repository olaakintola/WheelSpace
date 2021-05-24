from flask import Flask, request, jsonify
import json
from joblib import load
import pandas as pd
import numpy as np
from collections import OrderedDict


app = Flask(__name__)


@app.route('/predict',methods=['POST'])
def predict():
    temp_input = json.loads(request.data)

    print(temp_input)
    
    route_input = temp_input[0]['route']
    times_input = temp_input[0]['times']
    days_input = temp_input[0]['days']
    
    print(route_input)
    print(times_input)
    print(days_input)
    
    k = ['route', 'times', 'days']
    v = [route_input, times_input, days_input ]
    
    user_input = OrderedDict(zip(k,v))
    
    user_output = json.loads(json.dumps(user_input))
    
    #list
    dictList = []
    
    print(user_input)
    print(user_output)
    
    d = json.dumps( dictList.append(user_output) )
    print(dictList)
    print(d)
    
    input_df = pd.DataFrame(dictList )
    
    print(input_df)
    
    input_one_hot = ohe.transform(input_df)
    preds_input = clf.predict(input_one_hot)
    prediction = list(preds_input)
    return jsonify({'prediction': str(prediction)})
   

if __name__== "__main__":

    clf = load("busmodel.pkl")
    print("Model loaded")
    ohe = load("onehot_data")
    print("One Hot Encoding loaded")
    
    app.run(debug=True)
