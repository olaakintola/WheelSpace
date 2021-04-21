package com.example.wheelspace;

public class MLAPIUtility {
    private MLAPIUtility(){}

    static String url = "http://10.0.2.2:5000/";
//    static String url = "https://localhost:5000/";


    public static ClassifierModelAPIService getClassifierModelAPIService(){
        return RetrofitClient.getClient(url).create(ClassifierModelAPIService.class);
    }

}
