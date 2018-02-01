package com.andrjhf.storage.encrypt;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.andrjhf.storage.encrypt.Constant.AES_KEY;

/**
 * @author :  jiahongfei
 * @email : jiahongfeinew@163.com
 * @date : 2018/1/25
 * @desc : 用AES256加密的SharedPreferences
 */

public class AES256SharedPreferences implements SharedPreferences {

    private SharedPreferences mSharedPreferences;

    public AES256SharedPreferences(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }

    @Override
    public Map<String, ?> getAll() {
        try {
            throw new Exception("这个方法没有覆盖");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        String aesKey = AES256Encrypt(key);
        String value = mSharedPreferences.getString(aesKey, defValue);
        if(value.equals(defValue)){
            return defValue;
        }
        return AES256Decrypt(value);
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        String aesKey = AES256Encrypt(key);
        Set<String> stringSet = mSharedPreferences.getStringSet(aesKey, defValues);
        if(stringSet.equals(defValues)){
            return defValues;
        }
        Set<String> reSet = new HashSet<>();
        for (String value : stringSet){
            reSet.add(AES256Decrypt(value));
        }
        return reSet;
    }

    @Override
    public int getInt(String key, int defValue) {
        String aesKey = AES256Encrypt(key);
        String value = mSharedPreferences.getString(aesKey, String.valueOf(defValue));
        if(value.equals(String.valueOf(defValue))){
            return defValue;
        }
        return Integer.parseInt(AES256Decrypt(value));
    }

    @Override
    public long getLong(String key, long defValue) {
        String aesKey = AES256Encrypt(key);
        String value = mSharedPreferences.getString(aesKey, String.valueOf(defValue));
        if(value.equals(String.valueOf(defValue))){
            return defValue;
        }
        return Long.parseLong(AES256Decrypt(value));
    }

    @Override
    public float getFloat(String key, float defValue) {
        String aesKey = AES256Encrypt(key);
        String value = mSharedPreferences.getString(aesKey, String.valueOf(defValue));
        if(value.equals(String.valueOf(defValue))){
            return defValue;
        }
        return Float.parseFloat(AES256Decrypt(value));
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        String aesKey = AES256Encrypt(key);
        String value = mSharedPreferences.getString(aesKey, String.valueOf(defValue));
        if(value.equals(String.valueOf(defValue))){
            return defValue;
        }
        return Boolean.parseBoolean(AES256Decrypt(value));
    }

    @Override
    public boolean contains(String key) {
        String aesKey = AES256Encrypt(key);
        return mSharedPreferences.contains(aesKey);
    }

    @Override
    public Editor edit() {
        return new AES256Editor(mSharedPreferences.edit());
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public static class AES256Editor implements Editor {

        private Editor mEditor;

        public AES256Editor(Editor editor) {
            this.mEditor = editor;
        }

        @Override
        public Editor putString(String key, @Nullable String value) {
            String aesKey = AES256Encrypt(key);
            String aesValue = AES256Encrypt(value);
            return mEditor.putString(aesKey, aesValue);
        }

        @Override
        public Editor putStringSet(String key, @Nullable Set<String> values) {
            String aesKey = AES256Encrypt(key);
            Set<String> stringSet = new HashSet<>();
            for (String value : values) {
                stringSet.add(AES256Encrypt(value));
            }
            return mEditor.putStringSet(aesKey, stringSet);
        }

        @Override
        public Editor putInt(String key, int value) {
            //将int型转换成字符串进行加密
            String aesKey = AES256Encrypt(key);
            String aesValue = AES256Encrypt(String.valueOf(value));
            return mEditor.putString(aesKey, aesValue);
        }

        @Override
        public Editor putLong(String key, long value) {

            //将long型转换成字符串进行加密
            String aesKey = AES256Encrypt(key);
            String aesValue = AES256Encrypt(String.valueOf(value));
            return mEditor.putString(aesKey, aesValue);
        }

        @Override
        public Editor putFloat(String key, float value) {
            //将float型转换成字符串进行加密
            String aesKey = AES256Encrypt(key);
            String aesValue = AES256Encrypt(String.valueOf(value));
            return mEditor.putString(aesKey, aesValue);
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            //将boolean型转换成字符串进行加密
            String aesKey = AES256Encrypt(key);
            String aesValue = AES256Encrypt(String.valueOf(value));
            return mEditor.putString(aesKey, aesValue);
        }

        @Override
        public Editor remove(String key) {
            String aesKey = AES256Encrypt(key);
            return mEditor.remove(aesKey);
        }

        @Override
        public Editor clear() {
            return mEditor.clear();
        }

        @Override
        public boolean commit() {
            return mEditor.commit();
        }

        @Override
        public void apply() {
            mEditor.apply();
        }
    }

    private static String AES256Encrypt(String content) {
        try {
            return AES256.encrypt(AES_KEY, content);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String AES256Decrypt(String content) {
        try {
            return AES256.decrypt(AES_KEY, content);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

}
