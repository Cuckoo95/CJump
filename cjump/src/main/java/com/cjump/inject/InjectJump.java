package com.cjump.inject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cjump.annotations.SerializedJump;
import com.cjump.exception.NoSuchKeyException;
import com.cjump.exception.NullParamsException;
import com.cjump.exception.UnSupportClassException;
import com.cjump.util.NullUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * @author Cuckoo
 * @date 2016-12-15
 * @description
 *      绑定Activity间跳转的相关参数
 */

public class InjectJump {
    public static final String TAG = InjectJump.class.getSimpleName();
    /**
     * 解析从别的Activity传入的Intent，并将Intent中的值和Activity中绑定{@link SerializedJump#value()}的变量赋值
     * 默认只注入当前类的变量， 不注入父类
     * @param target
     *      需要接收Intent的Activity
     */
    public static boolean inject(Activity target){
        return inject(target,false);
    }

    /**
     * 解析从别的Activity传入的Intent，并将Intent中的值和Activity中绑定{@link SerializedJump#value()}的变量赋值
     * 通过isInjectParentAttrs来判断是否需要注入父类变量
     * @param target
     *      需要接收Intent的Activity
     */
    public static boolean inject(Activity target, boolean isInjectParentAttrs){
        if(NullUtil.check(target)){
            return false ;
        }
        return inject(target,target.getIntent(),isInjectParentAttrs);
    }

    /**
     * /**
     * 解析从别的Activity传入的Intent，并将Intent中的值和Activity中绑定{@link SerializedJump#value()}的变量赋值
     * 默认只注入当前类的变量， 不注入父类
     * @param target
     *      需要接收Intent的目标类，可以不是Activity
     * @param intent
     */
    public static boolean inject(Object target, Intent intent){
        return inject(target,intent,false);
    }

    /**
     * /**
     * 解析从别的Activity传入的Intent，并将Intent中的值和Activity中绑定{@link SerializedJump#value()}的变量赋值
     * 通过isInjectParentAttrs来判断是否需要注入父类变量
     * @param target
     *      需要接收Intent的目标类，可以不是Activity
     * @param intent
     * @param isInjectParentAttrs
     */
    public static boolean inject(Object target, Intent intent, boolean isInjectParentAttrs){
        if(NullUtil.check(target,intent)){
            return false;
        }
        Field[] fields = target.getClass().getDeclaredFields();
        injectFieldValues(fields,target,intent);
        if( isInjectParentAttrs ){
            fields = target.getClass().getFields();
            injectFieldValues(fields,target,intent);
        }
        return true ;
    }

    /**
     * 根据注解{@link SerializedJump#args()}以及传入的参数创建相对应的Intent
     * @param targetClass
     *      注解{@link SerializedJump}所在类
     * @param args
     *      用于创建Intent的所有参数，其个数以及参数类型和注解{@link SerializedJump#args()}的一致,<br>
     *      args中的每个值都不能为空， 需要根据args的值判断参数类型
     * @return
     */
    public static Intent getIntent(Class targetClass, Object... args){
        if(targetClass == null ){
            return null;
        }
        StackTraceElement[] elements  = Thread.currentThread().getStackTrace();
        Intent intent = null ;
        if(elements!= null && elements.length >= 4){
            //根据传入的参数以及方法名 获取对应的方法
            intent = getIntent(targetClass,elements[3],args);
        }
        return intent ;
    }

    /**
     * 根据方法名称，类名，以及传入的参数 生成Intent
     * @param targetClass
     * @param methodElement
     *      根据methodElement获取调用{@link #getIntent(Class, Object...)}和{@link #getIntent(Context, Class, Object...)}的方法信息
     * @param args
     * @return
     */
    private static Intent getIntent(Class targetClass,StackTraceElement methodElement, Object... args){
        if( methodElement == null ){
            return null ;
        }
        String methodName = methodElement.getMethodName();
        String className = methodElement.getClassName();
        className = InjectJumpHelper.filterClassNameFromStackTraceElement(className);
        Method method = null;
        try {
            method = InjectJumpHelper.getMethodByName(targetClass,methodName,className,args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if( method == null ){
            return null ;
        }
        /**根据方法获取方法上的{@link SerializedJump#args()}注解**/
        String[] argNames = InjectJumpHelper.getInjectArgNames(method);
        int argNamesSize = InjectJumpHelper.getArrayLength(argNames);
        Intent intent = null ;
        if( argNamesSize == InjectJumpHelper.getArraySize(args) ){
            //参数名称和参数个数正好对应
            intent = new Intent();
            if(argNamesSize > 0){
                for(int i = 0; i < argNamesSize; i++ ){
                    //将key/value放入Intent中
                    InjectJumpHelper.appendIntent(intent, argNames[i], args[i]);
                }
            }
        }
        return intent;
    }

    /**
     * 从Intent中根据key获取指定的值并放入field中
     * @param field
     * @param key
     * @param intent
     */
    private static void setFieldNewValue(Object target,Field field, String key, Intent intent){
        if(NullUtil.check(target,
                field,key,intent)){
            Log.w(TAG,"param is null of setFieldNewValue, target:" + target+" field:" + field +
                    " key:" + key + " intent:" + intent);
            return ;
        }

        try {
            Object value = InjectJumpHelper.getValueFromIntent(intent,key,field);
            field.setAccessible(true);
            field.set(target,value);
        } catch (NoSuchKeyException e) {
            Log.w(TAG,"[NoSuchKeyException]"+e.getLocalizedMessage());
        } catch (NullParamsException e) {
            Log.w(TAG,"[NullParamsException]"+e.getLocalizedMessage());
        } catch (UnSupportClassException e) {
            Log.w(TAG,"[UnSupportClassException]"+e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            Log.w(TAG,"[IllegalAccessException]"+e.getLocalizedMessage());
        }
    }

    /**
     * 通过{@link SerializedJump}给变量注入值
     * @param fields
     * @param target
     * @param intent
     */
    private static void injectFieldValues(Field[] fields,Object target, Intent intent){
        if( fields != null ){
            SerializedJump sJump = null ;
            String fieldAlias = null;
            for(Field field: fields){
                sJump = field.getAnnotation(SerializedJump.class);
                if(sJump != null ){
                    fieldAlias = sJump.value();
                    if(fieldAlias != null ){
                        setFieldNewValue(target,field,fieldAlias,intent);
                    }
                }
            }
        }
    }
}
