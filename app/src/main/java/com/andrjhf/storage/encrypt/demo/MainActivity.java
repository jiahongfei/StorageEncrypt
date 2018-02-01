package com.andrjhf.storage.encrypt.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.andrjhf.storage.encrypt.AES256SerializableObject;
import com.andrjhf.storage.encrypt.AES256SharedPreferences;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private AES256SharedPreferences mAES256SharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mAES256SharedPreferences = new AES256SharedPreferences(getSharedPreferences("test", Context.MODE_PRIVATE));
//
//        String key = "key_jiahongfei";
//        String value = "value_jiahongfei";
//
//        mAES256SharedPreferences.edit().putString(key,value).commit();
//        String getValue = mAES256SharedPreferences.getString(key,"");
//        Log.e(TAG,"getValue : " + getValue);
//
//        key = "getBoolean";
//        mAES256SharedPreferences.edit().putBoolean(key,true).commit();
//        boolean getBoolean = mAES256SharedPreferences.getBoolean(key,false);
//        Log.e(TAG,"getBoolean : " + getBoolean);
//
//        key = "getInt";
//        mAES256SharedPreferences.edit().putInt(key,1000).commit();
//        int getInt = mAES256SharedPreferences.getInt(key,0);
//        Log.e(TAG,"getInt : " + getInt);
//
//        key = "getFloat";
//        mAES256SharedPreferences.edit().putFloat(key,100.45F).commit();
//        float getFloat = mAES256SharedPreferences.getFloat(key,0);
//        Log.e(TAG,"getFloat : " + getFloat);
//
//        Log.e(TAG,"contains : " + mAES256SharedPreferences.contains("getBoolean"));
//
//        mAES256SharedPreferences.edit().remove("getFloat").commit();
//
//        mAES256SharedPreferences.edit().clear().commit();
//
//        getFloat = mAES256SharedPreferences.getFloat(key,0);
//        Log.e(TAG,"getFloat : " + getFloat);


//        SerializableObject.removeObject(this,"s_user");
//        SerializableObject.removeObject(this,"newUser");
//        SerializableObject.removeObject(this,"user");





        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setId("1234567890");
                user.setName("jiahongfei");
                user.setAge("20");

                Login login = new Login();
                login.setId("29292929");
                login.setToken("token_5050505");

                Member member = new Member();
                member.setId("10101010");
                member.setAge("25");
                member.setName("gaogaogao");
                login.setMember(member);
                user.setLogin(login);


                long start = System.currentTimeMillis();
                AES256SerializableObject.saveObject(MainActivity.this,user,"User");
                Log.e(TAG, "AES save User : " + user.toString());
                Log.e(TAG, "加密保存时间 : " + (System.currentTimeMillis()-start) + "毫秒");

                start = System.currentTimeMillis();
                user = (User) AES256SerializableObject.readObject(MainActivity.this,"User");
                Log.e(TAG, "AES read User : " + user.toString());
                Log.e(TAG, "解密读取时间 : " + (System.currentTimeMillis()-start) + "毫秒");

                start = System.currentTimeMillis();
                user = (User) SerializableObject.readObject(MainActivity.this,"User");
                Log.e(TAG, "Ser read User : " + user.toString());
                Log.e(TAG, "普通读取时间 : " + (System.currentTimeMillis()-start) + "毫秒");


            }
        });


//        SerializableObject.saveObject(this, user, "s_user");
//
//        User sUser = (User) SerializableObject.readObject(this, "s_user");
//        Log.e(TAG, "sUser : " + sUser.toString());
//
//
//        AES256SerializableObject.saveObject(this,sUser,"s_user");
//
//        User newUser = AES256SerializableObject.readObject(this,"newUser",User.class);
//        Log.e(TAG, "newUser : " + newUser.toString());



//        AES256SerializableObject.saveObject(this,user,"user");
//
//        User tmpUser = AES256SerializableObject.readObject(this,"user", User.class);
//
//        Log.e(TAG,tmpUser.toString());

//        Object userObj = user;
//
//        reflect(userObj);
//
//        Log.e(TAG, userObj.toString());

    }

}
