package com.cjump.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 *
 * @author Cuckoo
 * @date 2016-12-15
 * @description
 *      跳转工具类
 */

public class JumpUtil {
    /**
     * 跳转到指定activity
     * @param context
     * @param intent
     * @param activityClass
     */
    public static boolean jump(Activity context, Intent intent, Class activityClass){
        if(NullUtil.check(context,intent,activityClass)){
            return false;
        }
        intent.setClass(context,activityClass);
        context.startActivity(intent);
        return true ;
    }
}
