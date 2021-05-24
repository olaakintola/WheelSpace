package com.example.wheelspace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StopTimeFile {

    public String returnFile(String  route){
        String fileName = null;

        if(route.equals("1") || route.equals("11") || route.equals("116") || route.equals("118") || route.equals("120") || route.equals("122")
                || route.equals("123") || route.equals("13") || route.equals("130") ){
            fileName = "xaa.txt";
        }
        else if(route.equals("130") || route.equals("14") || route.equals("140") || route.equals("142") || route.equals("145") || route.equals("15")){
            fileName = "xab.txt";
        }
        else if(route.equals("15") || route.equals("150") || route.equals("151") || route.equals("155") || route.equals("15A") || route.equals("15B")
                || route.equals("15D") || route.equals("16") ){
            fileName = "xac.txt";
        }
        else if(route.equals("16") || route.equals("16D") || route.equals("25") || route.equals("25A") || route.equals("25B") || route.equals("25X")
                || route.equals("26") || route.equals("27") || route.equals("27A") || route.equals("27B") || route.equals("27X") || route.equals("29A")
                || route.equals("31") ){
            fileName = "xad.txt";
        }
        else if(route.equals("31") || route.equals("31A") || route.equals("31B") || route.equals("32") || route.equals("32X") || route.equals("33")
                || route.equals("33D") || route.equals("33E") || route.equals("33X") || route.equals("37") || route.equals("38") || route.equals("38A")
                || route.equals("38B") || route.equals("38D") || route.equals("39") || route.equals("39A")){
            fileName = "xae.txt";

        }
        else if(route.equals("39A") || route.equals("4") || route.equals("40") || route.equals("40B") || route.equals("40D") || route.equals("40E")
                || route.equals("41") || route.equals("41B") || route.equals("41C")){
            fileName = "xaf.txt";
        }
        else if(route.equals("41C") || route.equals("41D") || route.equals("41X") || route.equals("42") || route.equals("43") || route.equals("44")
                || route.equals("44B") || route.equals("46A") || route.equals("46E") || route.equals("47") || route.equals("49") || route.equals("51D")
                || route.equals("51X") || route.equals("53") || route.equals("54A") || route.equals("56A") || route.equals("61") || route.equals("65")
                || route.equals("66") ){
            fileName = "xag.txt";
        }
        else if(route.equals("66") || route.equals("66A") || route.equals("66B") || route.equals("66E") || route.equals("66X") || route.equals("67")
                || route.equals("67X") || route.equals("68") || route.equals("68A") || route.equals("68X") || route.equals("69") || route.equals("69X")
                || route.equals("7") || route.equals("70") || route.equals("77A") || route.equals("77X") || route.equals("79") || route.equals("79A")
                || route.equals("7A") || route.equals("7B") || route.equals("7D") || route.equals("83") ){
            fileName = "xah.txt";
        }
        else if(route.equals("83") || route.equals("83A") || route.equals("84") || route.equals("84A") || route.equals("84X") || route.equals("9")
                || route.equals("1") || route.equals("11") || route.equals("116") || route.equals("118") || route.equals("120") || route.equals("122") ){
            fileName = "xai.txt";
        }
        else if(route.equals("122") || route.equals("123") || route.equals("13") || route.equals("130") || route.equals("14") ){
            fileName = "xaj.txt";
        }
        else if(route.equals("14") || route.equals("140") || route.equals("142") || route.equals("145") || route.equals("15") ){
            fileName = "xak.txt";
        }
        else if(route.equals("15") || route.equals("150") || route.equals("151") || route.equals("155") || route.equals("15A") || route.equals("15B") ){
            fileName = "xal.txt";
        }
        else if(route.equals("15B") || route.equals("15D") || route.equals("16") || route.equals("16D") || route.equals("25") || route.equals("25A")
                || route.equals("25B") || route.equals("25X") || route.equals("26") || route.equals("27") ){
            fileName = "xam.txt";
        }
        else if(route.equals("27") || route.equals("27A") || route.equals("27B") || route.equals("27X") || route.equals("29A") || route.equals("31")
                || route.equals("31A") || route.equals("31B") ){
            fileName = "xan.txt";
        }
        else if(route.equals("31B") || route.equals("32") || route.equals("32X") || route.equals("33") || route.equals("33D") || route.equals("33E")
                || route.equals("33X") || route.equals("37") || route.equals("38") || route.equals("38A") || route.equals("38B") || route.equals("38D")
                || route.equals("39") || route.equals("39A") ){
            fileName = "xao.txt";
        }
        else if(route.equals("39A") || route.equals("4") || route.equals("40") ){
            fileName = "xap.txt";
        }
        else if(route.equals("40") || route.equals("40B") || route.equals("40D") || route.equals("40E") || route.equals("41") || route.equals("41B")
                || route.equals("41C") || route.equals("41D") || route.equals("41X") || route.equals("42") || route.equals("43") || route.equals("44") ){
            fileName = "xaq.txt";
        }
        else if(route.equals("44") || route.equals("44B") || route.equals("46A") || route.equals("46E") || route.equals("47") || route.equals("49")
                || route.equals("51D") || route.equals("51X") || route.equals("53") || route.equals("54A") || route.equals("56A") || route.equals("61")
                || route.equals("65") || route.equals("65B") || route.equals("66") ){
            fileName = "xar.txt";
        }
        else if(route.equals("66") || route.equals("66A") || route.equals("66B") || route.equals("66E") || route.equals("66X") || route.equals("67")
                || route.equals("67X") || route.equals("68") || route.equals("68A") || route.equals("68X") || route.equals("69") || route.equals("69X")
                || route.equals("7") || route.equals("70") || route.equals("77A") || route.equals("77X") || route.equals("79") ){
            fileName = "xas.txt";
        }
        else if(route.equals("79") || route.equals("79A") || route.equals("7A") || route.equals("7B") || route.equals("7D") || route.equals("83")
        || route.equals("83A") || route.equals("84") || route.equals("84A") || route.equals("84X") || route.equals("9") ){
            fileName = "xat.txt";
        }

        return fileName;
    }

}

