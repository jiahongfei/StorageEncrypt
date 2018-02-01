package com.andrjhf.storage.encrypt;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Map;
import java.util.Set;

/**
 * @author :  jiahongfei
 * @email : jiahongfeinew@163.com
 * @date : 2018/1/25
 * @desc :
 */
@RunWith(MyRobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class AES256SharedPreferencesTest {

    private AES256SharedPreferences mAES256SharedPreferences;

    @Before
    public void setup(){
        mAES256SharedPreferences = new AES256SharedPreferences(RuntimeEnvironment.application.getSharedPreferences("test", Context.MODE_PRIVATE));
    }

//    @Test
//    public void getAll() {
//    }

    @Test
    public void setString() {

        String key = "key_jiahongfei";
        String value = "key_jiahongfei";
        mAES256SharedPreferences.edit().putString(key,value).commit();

        String getValue = mAES256SharedPreferences.getString(key,"");

        System.out.println("getValue : " + getValue);

        Assert.assertEquals(value,getValue);
    }

//    @Test
//    public void getStringSet(String key, @Nullable Set<String> defValues) {
//    }
//
//    @Test
//    public void getInt(String key, int defValue) {
//    }
//
//    @Test
//    public void getLong(String key, long defValue) {
//    }
//
//    @Test
//    public void getFloat(String key, float defValue) {
//    }
//
//    @Test
//    public void getBoolean(String key, boolean defValue) {
//    }
//
//    @Test
//    public void contains(String key) {
//    }
//
//    @Test
//    public SharedPreferences.Editor edit() {
//        return null;
//    }
//
//    @Test
//    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
//
//    }
//
//    @Test
//    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
//
//    }

}
