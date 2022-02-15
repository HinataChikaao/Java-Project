package test;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class DeleteCatch {


    public static void main(String[] args) {

        File outFolder = new File("D:/Projects/LethbridgeUniversity/out");
        File tempFolder = new File("D:/Root/AS/EAP-7.1.0/standalone/tmp");
        File dataFolder = new File("D:/Root/AS/EAP-7.1.0/standalone/data");

        try {
            FileUtils.deleteDirectory(outFolder);
            FileUtils.deleteDirectory(tempFolder);
            FileUtils.deleteDirectory(dataFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
