package com.andrjhf.storage.encrypt;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.andrjhf.storage.encrypt.anns.Encrypt;
import com.andrjhf.storage.encrypt.anns.EncryptPojo;
import com.andrjhf.storage.encrypt.anns.EncryptString;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;

import static com.andrjhf.storage.encrypt.Constant.AES_KEY;

/**
 * @author :  jiahongfei
 * @email : jiahongfeinew@163.com
 * @date : 2018/1/25
 * @desc : 用AES256对自定义的JavaBean中用注释EncryptString修饰的字段进行加密之后序列化到本地
 */
public class AES256SerializableObject {

    private static final String TAG = "AES256SerObj";

    /**
     * 保存对象
     *
     * @param ser
     * @param file 文件名不用路径
     * @throws IOException
     */
    public static boolean saveObject(Context context, Object ser, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            long start = System.currentTimeMillis();
            Object tmpObj = deepClone(ser);
            Logger.e(TAG, "deppClone时间 : " + (System.currentTimeMillis() - start) + "毫秒");
            Object tmpSer = encrypt(tmpObj);
            fos = context.openFileOutput(file, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(tmpSer);
            oos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
            }
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public static Object readObject(Context context, String file) {
        if (!isExistDataCache(context, file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            Object t = ois.readObject();
            decrypt(t);
            return t;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            // 反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException) {
                File data = context.getFileStreamPath(file);
                data.delete();
            }
        } finally {
            try {
                ois.close();
            } catch (Exception e) {
            }
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 判断缓存是否存在
     *
     * @param cachefile 文件名不用路径
     * @return
     */
    private static boolean isExistDataCache(Context context, String cachefile) {
        boolean exist = false;
        File data = context.getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 删除序列化缓存
     *
     * @param context
     * @return
     */
    public static boolean removeObject(Context context, String cacheFile) {
        boolean exist = false;
        File data = context.getFileStreamPath(cacheFile);
        if (data.exists()) {
            exist = data.delete();
        }
        return exist;
    }

    private static Object encrypt(Object obj) throws IllegalAccessException, InstantiationException {
        if (obj == null) return null;
//        Object tmpObj = deepClone(obj);
        Object tmpObj = obj;
        Field[] fields = tmpObj.getClass().getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            Field field = fields[j];
            field.setAccessible(true);
            // 字段名
            Annotation[] anns = field.getDeclaredAnnotations();
            if (anns.length < 1)
                continue; // Not a db table column
            // 字段值
            if (anns[0] instanceof Encrypt || anns[0] instanceof EncryptString) {
                if (field.getType().toString().equals("class java.lang.String")) {
                    try {
                        String value = (String) field.get(tmpObj);
                        field.set(tmpObj, AES256.encrypt(AES_KEY, value));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                }
            } else if (anns[0] instanceof EncryptPojo) {
                Object fieldObj = field.get(tmpObj);
                Object enObj = encrypt(fieldObj);
                field.set(tmpObj, enObj);
            }
        }
        return tmpObj;
    }

    private static void decrypt(Object t) {
        if (null == t) {
            return;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (int j = 0; j < fields.length; j++) {
            Field field = fields[j];
            field.setAccessible(true);
            // 字段名
            Annotation[] anns = field.getDeclaredAnnotations();
            if (anns.length < 1)
                continue; // Not a db table column
            // 字段值
            if (anns[0] instanceof Encrypt || anns[0] instanceof EncryptString) {
                if (field.getType().toString().equals("class java.lang.String"))
                    try {
                        String value = (String) field.get(t);
                        field.set(t, AES256.decrypt(AES_KEY, value));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
            } else if (anns[0] instanceof EncryptPojo) {
                try {
                    Object fieldObj = field.get(t);
                    decrypt(fieldObj);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 用序列化与反序列化实现深克隆
    public static Object deepClone(Object src) {
        if (src instanceof Parcelable) {
            Logger.e(TAG, "deepClone Parcelable");
            Parcel parcel = null;
            try {
                parcel = Parcel.obtain();
                parcel.writeParcelable((Parcelable) src, 0);

                parcel.setDataPosition(0);
                return parcel.readParcelable(src.getClass().getClassLoader());
            } finally {
                parcel.recycle();
            }
        } else if (src instanceof Serializable) {
            Logger.e(TAG, "deepClone Serializable");
            Object o = null;
            try {
                if (src != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(src);
                    oos.close();
                    ByteArrayInputStream bais = new ByteArrayInputStream(
                            baos.toByteArray());
                    ObjectInputStream ois = null;
                    ois = new ObjectInputStream(bais);
                    o = ois.readObject();
                    ois.close();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
            return o;
        }
        return null;
    }

}
