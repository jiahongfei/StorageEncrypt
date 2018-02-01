package com.andrjhf.storage.encrypt.demo;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;

/**
 * @author jiahongfei jiahongfeinew@163.com
 * @version V1.0.0
 * @Title: SerializableObject.java
 * @Package com.android.support.framework.utils
 * @ClassName: SerializableObject
 * @Description: 序列化对象, 保存对象到本地
 * @date Nov 5, 2014 2:23:28 PM
 */
public class SerializableObject {

    /**
     * 保存对象
     *
     * @param ser
     * @param file 文件名不用路径
     * @throws IOException
     */
    public static boolean saveObject(Context context, Serializable ser, String file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(file, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
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

    public static Serializable readObject(Context context, String file) {
        return readObject(context, file, null);
    }

    /**
     * 读取对象
     *
     * @param file 文件名不用路径
     * @return
     * @throws IOException
     */
    public static Serializable readObject(Context context, String file, final Set<String> classNameSet) {
        if (!isExistDataCache(context, file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
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
}
