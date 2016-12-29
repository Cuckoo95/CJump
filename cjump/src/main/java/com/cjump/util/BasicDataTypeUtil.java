package com.cjump.util;

import android.util.Log;

import java.util.HashMap;

/**
 *
 * @author Cuckoo
 * @date 2016-12-16
 * @description
 *      基本数据类型工具类
 */

public class BasicDataTypeUtil {
    private static HashMap<Class,Class> basicDataMap = null ;

    /**
     * 判断是否为基本数据类型，如果基本数据类型或者基本数据类型的数组，更换返回的Class。<br>
     * 如：boolean类型的变量，其class不是Boolean.class
     * @param type
     * @return
     */
    public static Class filter(Class type){
        Class newType = getBasicDataMap().get(type);
        if( newType == null ){
            newType = type;
        }
        return newType;
    }

    /**
     * 建立基本数据类型之间的对应关系
     * @return
     */
    private static HashMap<Class,Class> getBasicDataMap(){
        if(basicDataMap == null ){
            basicDataMap = new HashMap<>();
            basicDataMap.put(int.class,Integer.class);
            basicDataMap.put(int[].class,Integer[].class);
            basicDataMap.put(long.class,Long.class);
            basicDataMap.put(long[].class,Long[].class);
            basicDataMap.put(float.class,Float.class);
            basicDataMap.put(float[].class,Float[].class);
            basicDataMap.put(double.class,Double.class);
            basicDataMap.put(double[].class,Double[].class);
            basicDataMap.put(byte.class,Byte.class);
            basicDataMap.put(byte[].class,Byte[].class);
            basicDataMap.put(short.class,Short.class);
            basicDataMap.put(short[].class,Short[].class);
            basicDataMap.put(char.class,Character.class);
            basicDataMap.put(char[].class,Character[].class);
            basicDataMap.put(boolean.class,Boolean.class);
            basicDataMap.put(boolean[].class,Boolean[].class);
        }
        return basicDataMap;
    }

}
