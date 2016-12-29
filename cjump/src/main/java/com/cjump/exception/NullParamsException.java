package com.cjump.exception;

/**
 *
 * @author Cuckoo
 * @date 2016-12-15
 * @description
 *      传入的参数为null
 */

public class NullParamsException extends Exception{
    public NullParamsException(String msg){
        super(msg);
    }
}
