package com.api.isswhu.demo.util;


public class NumUtil {
    /**
     * get 1000 ~ 8000
     * @return
     */
    public static int getRandomPort(){
        int result = (int) (Math.random() * (8000 - 1000) + 1000);
        return result;
    }
    
}
