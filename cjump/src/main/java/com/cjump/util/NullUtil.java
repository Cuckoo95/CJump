package com.cjump.util;

/**
 *
 * @author Cuckoo
 * @date 2016-12-15
 * @description
 *      判空工具类
 */

public class NullUtil {
    /**
     * 检查是否为null
     * @param objs
     * @return
     *  true：空
     *  false：非空
     */
    public static boolean check(Object... objs){
        if( objs != null ){
            for(Object obj: objs){
                if(obj == null ){
                    return true ;
                }
            }
        }
        return false;
    }
}
