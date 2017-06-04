package com.douban.malfurion;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by linwei on 2017/6/3.
 */

public class Monitor2 {

    static String currentActivityName;

    public static void onViewClick(View view) {
        String path = getViewPath(view);
        currentActivityName = getActivityName(view);
        System.out.println(currentActivityName + ":" + path);
    }

    static boolean isSystemView(View view) {
        int id = view.getId();
        return id >> (4 * 6) == 0x01 ||
                id >> (4 * 6) == 0x00;
    }

    static String getViewPath(View v) {
        StringBuilder builder = new StringBuilder();
        View view = v;
        while (view != null) {
            String name = view.getClass().getSimpleName();
            ViewParent parent = view.getParent();
            if (parent instanceof ViewGroup && !isSystemView(view)) {
                if (builder.length() > 0) {
                    builder.insert(0, '/');
                }
                builder.insert(0, String.format("%1$s[%2$s]", name, ((ViewGroup) parent).indexOfChild(view)));
                view = (View) parent;
            } else {
                view = null;
            }
        }
        return builder.toString();
    }

    static String getActivityName(View view) {
        Context context = view.getContext();
        if (context instanceof Activity) {
            return context.getClass().getSimpleName();
        } else if (context instanceof ContextWrapper){
            context = ((ContextWrapper) context).getBaseContext();
            if (context instanceof Activity) {
                return context.getClass().getSimpleName();
            }
        }
        return currentActivityName;
    }
}
