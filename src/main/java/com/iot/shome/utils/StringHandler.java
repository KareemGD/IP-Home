package com.iot.shome.utils;

/**
 * Created by Kareem Diab on 3/29/2016.
 */
public class StringHandler {
    public static String handle (String str) {
        String result = "";
        if(str.equals("foo/bar")){
            result = "Gas Sensor";
        }

        return result;
    }
}
