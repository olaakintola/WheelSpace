package com.example.wheelspace;

import java.util.ArrayList;

public class RouteGenerator {

    public ArrayList<String> generatorBusRouteNuumber(String ftrDeparture, String ftrDestination){
        ArrayList<String> routeList = new ArrayList<>();

        ftrDeparture = ftrDeparture.replaceAll(" ","_");
        ftrDestination = ftrDestination.replaceAll(" ", "_");


        return routeList;
    }

}
