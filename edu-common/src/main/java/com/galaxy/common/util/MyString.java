package com.galaxy.common.util;

public class MyString {
    /**
     * 实体转换字段  hiBug ---> hi_bug
     * @param a
     * @return
     */
    public static String convertColumn(String a) {
        char[] chars = a.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >='A' && chars[i] <='Z') {
                chars[i] += 32;
                sb.append("_"+chars[i]);
            }else {
                sb.append(chars[i]);
            }
        }
        return sb.toString();
    }


    /**
     * 比较俩字符串值
     */
    public static boolean stringValue(String a,String b){
          int n =a.length();
        if (a == null || b == null){
            return false;
        }
        if (a==b){
            return true;
        }
        if (n == b.length()){
            char[] ac = a.toCharArray();
            char[] bc = b.toCharArray();
            int i = 0;
            while (n-- !=0){
                if (ac[i] != bc[i]) {
                    return false;
                }
                i++;
            }
            return true;

        }

       return false;

    }
}
