package com.cjump.exception;

/**
 *
 * @author Cuckoo
 * @date 2016-12-15
 * @description
 *      Intent不支持的Class类型
 */

public class UnSupportClassException extends Exception{
    public UnSupportClassException(String msg){
        super(msg);
    }
}
