package com.andrjhf.storage.encrypt;

import android.text.TextUtils;
import android.util.Log;

/**AES128
 * 日志打印工具类，封装到一起，是为了调用时方便
 *
 * @author Administrator
 */
class Logger {
    private static final String TAG = "PAH";

    public static void v(String message) {
        if (BuildConfig.DEBUG)
            Log.v(TAG, message);
    }

    public static void v(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.v(tag, message);
    }

    public static void d(String message) {
        if (BuildConfig.DEBUG)
            Log.d(TAG, message);
    }

    public static void i(String message) {
        if (BuildConfig.DEBUG)
            Log.i(TAG, message);
    }

    public static void i(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.i(tag, message);
    }

    public static void w(String message) {
        if (BuildConfig.DEBUG)
            Log.w(TAG, message);
    }

    public static void w(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.w(tag, message);
    }

    public static void e(String message) {
        if (BuildConfig.DEBUG)
            Log.e(TAG, message);
    }

    public static void e(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.e(tag, message);
    }

    public static void d(String tag, String message) {
        if (!TextUtils.isEmpty(message) && BuildConfig.DEBUG) {
            Log.d(TextUtils.isEmpty(tag) ? TAG : tag, message);
        }
    }
}
