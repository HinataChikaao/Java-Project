package test;


import util.EncoderUtil;

public class Test {

    public static void main(String[] args) throws Exception {


        //String aesEncrypt = EncoderUtil.getAESEncrypt("capital", "Ramin Hoobkaht");

        byte[] bytes = "99618A3F797C67C0CD9DC98AA843B417".getBytes();

        String result = EncoderUtil.getAESDecrypt("capital", String.valueOf(bytes));
        //System.out.println(aesEncrypt);
        System.out.println(result);

/*
        Navigate navigate = new Navigate();
        navigate.setName("Ramin1");
        navigate.setName("Ramin2");
        navigate.setName("Ramin3");
        navigate.setName("Ramin4");
        navigate.setName("Ramin5");
        navigate.setName("Ramin6");
        navigate.setName("Ramin7");
        navigate.setName("Ramin8");
        navigate.setName("Ramin9");
        navigate.setName("Ramin10");

        for (Object aNavigate : navigate) {
            System.out.println(String.valueOf(aNavigate));
        }
*/


    }

}
