package ru.feryafox.FeryaASM.utils;

public class StringUtils {
    static public String deleteComment(String str){
        int index = str.indexOf(';');
        String rStr = str;
        if (index != -1) {
            rStr = str.substring(0, index);
        }
        return rStr;
    }
}
