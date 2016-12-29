package com.cjump.inject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.cjump.annotations.SerializedJump;
import com.cjump.exception.NoSuchKeyException;
import com.cjump.exception.NullParamsException;
import com.cjump.exception.UnSupportClassException;
import com.cjump.util.BasicDataTypeUtil;
import com.cjump.util.NullUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 *
 * @author Cuckoo
 * @date 2016-12-14
 * @description
 *      主要用于创建Activity之间跳转时的Intent
 */

public class InjectJumpHelper {


    /**
     * 根据方法名称 找到相对应参数名称
     * @param method
     * @return
     */
    protected static String[] getInjectArgNames(Method method){
        if( method == null || !method.isAnnotationPresent(SerializedJump.class)){
            //方法不存在或者注解不存在
            return null ;
        }
        SerializedJump serializedJump = method.getAnnotation(SerializedJump.class);
        if(serializedJump != null ){
            String[] argNames = serializedJump.args() ;
            if( argNames == null ){
                argNames = new String[0];
            }
            return argNames;
        }
        return null ;
    }

    /**
     * 根据方法名称以及参数找出相对应的{@link Method}
     * @param targetClass
     * @param methodName
     * @param args
     * @return
     * @throws NoSuchMethodException
     */
    protected static Method getMethodByName(Class targetClass,
                                          String methodName,
                                          String specifyClassName,
                                          Object... args) throws NoSuchMethodException{
        if(NullUtil.check(targetClass,
                methodName,specifyClassName)){
            throw new NoSuchMethodException("Method name, target class or specify class name is null.");
        }
        String className = targetClass.getName();
        if( !specifyClassName.equals(className)){
            throw new NoSuchMethodException("The class name of target [" + className + "] is not speicyf class name["+
                    specifyClassName+"]" + methodName + " class name "+ className);
        }
        //获取当前类的所有方法
        Method[] methodArray = targetClass.getDeclaredMethods();
        if( methodArray != null ){
            int argCount = getArraySize(args);
            for(Method method: methodArray){
                if( !NullUtil.check(method,method.getName()) &&
                    method.getName().equals(methodName)){
                    if( getArrayLength(method.getParameterTypes()) == argCount){
                        //参数个数相同，比较参数类型
                        if( argCount == 0){
                            //不带参数
                            return method;
                        }else {
                            //带参数，比较每个参数
                            Object arg = null ;
                            boolean isMatchAll = true ;
                            for(int i = 0; i < argCount; i ++){
                                arg = args[i];
                                if(arg != null) {
                                    Class paramClass = method.getParameterTypes()[i];
                                    //将基本数据类型进行过滤
                                    paramClass = BasicDataTypeUtil.filter(paramClass);
                                    Class argClass = BasicDataTypeUtil.filter(arg.getClass());
                                    if (!(paramClass.equals(argClass) ||
                                            paramClass.isAssignableFrom(argClass))) {
                                        isMatchAll = false;
                                        break;
                                    }
                                }
                            }
                            if(isMatchAll){
                                return method;
                            }
                        }
                    }
                }
            }
        }
        //没有找到相关方法
        return null;
    }

    /**
     * 获取数组长度
     * @param array
     * @return
     */
    protected static int getArraySize(Object... array){
        if(array != null ){
            return array.length;
        }
        return 0 ;
    }

    protected static int getArrayLength(Object[] array){
        if(array != null ){
            return array.length;
        }
        return 0 ;
    }

    /**
     * 从StackTraceElement中获取的类名可能包含$符号， 如：
     * com.cjump.inject.InjectJumpHelper$overwrite
     * @param className
     * @return
     */
    protected static String filterClassNameFromStackTraceElement(String className){
        if( className != null ){
            int index = className.indexOf("$");
            if( index > 0 ){
                className = className.substring(0,index);
            }
        }
        return className;
    }

    /**
     * 将参数key和value塞入intent中
     * @param intent
     * @param key
     * @param value
     * @return
     */
    protected static Intent appendIntent(Intent intent, String key, Object value){
        if(!NullUtil.check(intent,key,value)){
            if( value instanceof Boolean){
                intent.putExtra(key, (Boolean)value);
            }else if(value instanceof Boolean[] ){
                intent.putExtra(key, (Boolean[])value);
            }else if(value instanceof Bundle){
                intent.putExtra(key, (Bundle)value);
            }else if(value instanceof Byte ){
                intent.putExtra(key, (Byte)value);
            }else if(value instanceof Byte[] ){
                intent.putExtra(key, (Byte[])value);
            }else if(value instanceof Character ){
                intent.putExtra(key, (Character)value);
            }else if(value instanceof Character[] ){
                intent.putExtra(key, (Character[])value);
            }else if(value instanceof CharSequence ){
                intent.putExtra(key, (CharSequence)value);
            }else if(value instanceof CharSequence[] ){
                intent.putExtra(key, (CharSequence[])value);
            }else if(value instanceof Double ){
                intent.putExtra(key, (Double)value);
            }else if(value instanceof Double[] ){
                intent.putExtra(key, (Double[])value);
            }else if(value instanceof Float ){
                intent.putExtra(key, (Float)value);
            }else if(value instanceof Float[] ){
                intent.putExtra(key, (Float[])value);
            }else if(value instanceof Integer ){
                intent.putExtra(key, (Integer)value);
            }else if(value instanceof Integer[] ){
                intent.putExtra(key, (Integer[])value);
            }else if(value instanceof Long ){
                intent.putExtra(key, (Long)value);
            }else if(value instanceof Long[] ){
                intent.putExtra(key, (Long[])value);
            }else if(value instanceof Parcelable){
                intent.putExtra(key, (Parcelable)value);
            }else if(value instanceof Parcelable[] ){
                intent.putExtra(key, (Parcelable[])value);
            }else if(value instanceof Serializable){
                intent.putExtra(key, (Serializable)value);
            }else if(value instanceof Short ){
                intent.putExtra(key, (Short)value);
            }else if(value instanceof Short[] ){
                intent.putExtra(key, (Short[])value);
            }else if(value instanceof String ){
                intent.putExtra(key, (String)value);
            }else if(value instanceof String[] ){
                intent.putExtra(key, (String[])value);
            }else if(value instanceof ArrayList){
                ArrayList list = (ArrayList)value;
                Class typeClass = list.getClass().getComponentType();
                if(typeClass == Integer.class ){
                    intent.putIntegerArrayListExtra(key,list);
                }else if(typeClass == CharSequence.class){
                    intent.putCharSequenceArrayListExtra(key,list);
                }else if(typeClass == Parcelable.class){
                    intent.putParcelableArrayListExtra(key,list);
                }else if(typeClass == String.class){
                    intent.putStringArrayListExtra(key,list);
                }
            }
        }
        return intent;
    }

    /**
     * 从Intent中获取指定的值
     * @param intent
     * @param key
     * @return
     */
    protected static Object getValueFromIntent(Intent intent,
                                            String key, Field field)
            throws NoSuchKeyException,
            NullParamsException,
            UnSupportClassException{
        if(NullUtil.check(intent,key,field,intent.getExtras())){
            throw new NullParamsException("Param is null, intent:" + intent +
                    " key:"+ key + " type:"+ field + " Extras:"+ intent.getExtras());
        }
        Bundle bundle = intent.getExtras();
        if(!bundle.containsKey(key)){
            throw new NoSuchKeyException("No such key:"+ key);
        }
        Class type = field.getType();
        Object res = null ;
        //将基本数据类型过滤为对象类型
        type = BasicDataTypeUtil.filter(type);
        if( type == Boolean.class ){
            res = intent.getBooleanExtra(key,false);
        }else if(type == Boolean[].class ){
            res = intent.getBooleanArrayExtra(key);
        }else if(type == Bundle.class ){
            res = intent.getBundleExtra(key);
        }else if(type == Byte.class  ){
            res = intent.getByteExtra(key,(byte)0);
        }else if(type == Byte[].class ){
            res = intent.getByteArrayExtra(key);
        }else if(type == Character.class ){
            res = intent.getCharExtra(key,' ');
        }else if(type == Character[].class ){
            res = intent.getCharArrayExtra(key);
        }else if(type == CharSequence.class ){
            res = intent.getCharSequenceExtra(key);
        }else if(type == CharSequence[].class){
            res = intent.getCharSequenceArrayExtra(key);
        }else if(type == Double.class ){
            res = intent.getDoubleExtra(key,0);
        }else if(type == Double[].class  ){
            res = intent.getDoubleArrayExtra(key);
        }else if(type == Float.class  ){
            res = intent.getFloatExtra(key,0);
        }else if(type == Float[].class ){
            res = intent.getFloatArrayExtra(key);
        }else if(type == Integer.class ){
            res = intent.getIntExtra(key,0);
        }else if( type == Integer[].class   ){
            res = intent.getIntArrayExtra(key);
        }else if(type == Long.class ){
            res = intent.getLongExtra(key,0);
        }else if(type == Long[].class ){
            res = intent.getLongArrayExtra(key);
        }else if(type == Parcelable.class ){
            res = intent.getParcelableExtra(key);
        }else if(type == Parcelable[].class ){
            res = intent.getParcelableArrayExtra(key);
        }else if(type == Serializable.class  ){
            res = intent.getSerializableExtra(key);
        }else if(type == Short.class  ){
            res = intent.getShortExtra(key,(short)0);
        }else if(type == Short[].class ){
            res = intent.getShortArrayExtra(key);
        }else if(type == String.class ){
            res = intent.getStringExtra(key);
        }else if(type == String[].class ){
            res = intent.getStringArrayExtra(key);
        }else if(type == ArrayList.class){
            //获取ArrayList中的泛型
            Class typeClass = null ;
            if( field.getGenericType() instanceof ParameterizedType){
                Type[] argTypes = ((ParameterizedType)field.getGenericType()).getActualTypeArguments();
                if(argTypes != null && argTypes.length >0){
                    typeClass  = (Class)argTypes[0];
                }
            }
            boolean isMatched = true ;
            if( typeClass != null ){
                if(  Integer.class == typeClass ){
                    res = intent.getIntegerArrayListExtra(key);
                }else if(CharSequence.class == typeClass ){
                    res = intent.getCharSequenceArrayListExtra(key);
                }else if(Parcelable.class == typeClass ){
                    res = intent.getParcelableArrayListExtra(key);
                }else if(String.class == typeClass ){
                    res = intent.getStringArrayListExtra(key);
                }else {
                    isMatched = false ;
                }
            }else {
                isMatched = false;
            }
            if( !isMatched ){
                throw new UnSupportClassException("UnSupported class:"+ type);
            }

        }else {
            throw new UnSupportClassException("UnSupported class:"+ type);
        }
        return res;
    }
}
