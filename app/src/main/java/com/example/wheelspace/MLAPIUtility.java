package com.example.wheelspace;

public class MLAPIUtility {
    private MLAPIUtility(){}

/*    Url that gets app connected the REST API created by deployed
    machine learning model in server*/
    static String url = "http://10.0.2.2:5000/";

    public static ClassifierModelAPIService getClassifierModelAPIService(){
        return RetrofitClient.getClient(url).create(ClassifierModelAPIService.class);
    }

}
