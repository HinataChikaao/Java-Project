package test;


import org.apache.poi.hmef.attribute.MAPIAttribute;

import java.util.HashMap;
import java.util.Map;

public class UsingThreadLocal {

    public static void main(String[] args) {

        Map<String,String> strs = new HashMap<>();
        ThreadLocal<Byte> byteNumber = new ThreadLocal<>();


        String ramin = strs.get("ramin");
        System.out.println(ramin);


    }
}
