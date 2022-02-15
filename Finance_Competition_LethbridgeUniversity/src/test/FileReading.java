package test;

import java.io.*;

public class FileReading {

    public static void main(String[] args) {


        File file = new File("");
        try (FileInputStream fis = new FileInputStream(file)) {
            try(BufferedInputStream bis = new BufferedInputStream(fis)){
                bis.readAllBytes();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
