package top.xfunny.mod.util;

import java.util.ArrayList;

public class ArrayListToString {


    public static String arrayListToString(ArrayList<Object> list) {
        StringBuilder temp = new StringBuilder();
        for (Object obj : list) {
            temp.append(obj.toString());
        }
        return temp.toString();
    }
}


