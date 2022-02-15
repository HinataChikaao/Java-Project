package test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestDate {

    public static void main(String[] args) {

        String date = new SimpleDateFormat("dd/MM/YYYY").format(new Date());
        System.out.println("date = " + date);

    }
}
