package com.douban.malfurion

import android.app.Activity
import android.app.Fragment
import android.content.ContextWrapper
import android.view.View
import android.view.ViewGroup

/**
 * Created by linwei on 2017/5/22.
 */

object Monitor {

    var currentActivityName: String? = null

    fun onViewClick(view: View) {
        val path = getViewPath(view)
        currentActivityName = getActivityName(view)
        println("$currentActivityName:$path")
    }

    fun getViewPath(v: View): String {
        val builder = StringBuilder()
        var view: View? = v

        fun isSystemView(v: View): Boolean {
            return v.id.ushr(4 * 6) == 0x01 ||
                    v.id.ushr(4 * 6) == 0x00
        }

        while (view != null) {
            val name = view.javaClass.simpleName
            val parent = view.parent
            if (parent is ViewGroup && !isSystemView(view)) {
                if (builder.isNotEmpty()) {
                    builder.insert(0, "/")
                }
                builder.insert(0, "$name[${parent.indexOfChild(view)}]")
                view = parent
            } else {
                view = null
            }
        }
        return builder.toString()
    }

    fun getActivityName(view: View): String? {
        var context = view.context
        if (context is Activity) {
            return context.javaClass.simpleName;
        } else if (context is ContextWrapper) {
            context = context.baseContext
            if (context is Activity) {
                return context.javaClass.simpleName
            }
        }
        return currentActivityName
    }

    fun onFragmentResumed(fragment: Fragment) {
        val a = Integer.parseInt("hello")
        println(a)
    }

    fun onFragmentPaused(fragment: Fragment) {

    }

    fun onHiddenChanged(fragment: Fragment) {

    }

    fun setUserVisibleHint(fragment: Fragment) {

    }

}
