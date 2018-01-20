package com.hjx.android.customviewset.util;

import android.content.Context;

/**
 * Created by hjx on 2017/11/24.
 * You can make it better
 */

public class UiUtil {
    public static float dp2px(Context context,int dp){
        return context.getResources().getDisplayMetrics().density*dp;
    }

    public static float sp2px(Context context,int sp){
        return context.getResources().getDisplayMetrics().scaledDensity*sp;
    }
}
