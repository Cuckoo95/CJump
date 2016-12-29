package com.cjump.exception;

/**
 *
 * @author Cuckoo
 * @date 2016-12-15
 * @description
 *      没有找到对应的跳转字段
 */

public class NoSuchKeyException extends Exception{
    public NoSuchKeyException(String msg){
        super(msg);
    }
}
