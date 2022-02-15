package test;


import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Test2 {


    private static boolean checkDouble(double dblNum) {
        return Double.isNaN(dblNum) || Double.isInfinite(dblNum);
    }

    public static void main(String[] args) {

        double dbNum = 12363213215.3253216549876d;
        //System.out.println(Double.isFinite(dbNum));


        Set<String> names = new HashSet<>();
        StringBuilder sb = new StringBuilder();



        sb.append(" - ").append(names);




        System.out.println(sb.toString()
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                );

    }


}
