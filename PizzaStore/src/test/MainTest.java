package test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class MainTest {

    public static void main(String[] args) {

        /*String str = "/ramin/mariam/yasamin";

        String substring = str.substring(str.lastIndexOf("/")).replaceAll("/","");
        System.out.println(substring);*/


      /*  try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update("1350".getBytes());
            byte[] digest = messageDigest.digest();
            byte[] encode = Base64.getEncoder().encode(digest);
            System.out.println(new String(encode));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/


     /*   LocalTime now = LocalTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();


        System.out.println(hour+":"+minute+":"+second);*/


        Map<Integer,String> names = new HashMap<>();
        names.put(1,"11");
        names.put(1,"12");
        names.put(1,"13");
        names.put(1,"14");


        System.out.println(names.values());



    }


}
