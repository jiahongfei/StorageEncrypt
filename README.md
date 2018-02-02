# StorageEncrypt
Android敏感数据存储加密，实现了SharedPreferences和序列化对象存储的加密

> 开发App的同时肯定会涉及到敏感数据存储，例如：token，身份证，用户名，密码等。
这些信息如果明文存储在本地是非常危险的，手机被Root之后无论你存储在什么地方都会被看到。
本篇文章介绍我自己写的一个开源框架 **[StorageEncrypt](https://github.com/jiahongfei/StorageEncrypt)**，对存储在本地的数据进行AES256加密。
目前只实现了SharedPreferences和序列化对象存储的加密，未来还会根据业务进行更新。
GitHub地址：[请点击这里](https://github.com/jiahongfei/StorageEncrypt)

![图片源于网络.png](http://upload-images.jianshu.io/upload_images/4158487-e661ec7eee8649f7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


### AES加密简介
** 高级加密标准 **（英语：**Advanced Encryption Standard**，缩写：**AES**），在[密码学](https://baike.baidu.com/item/%E5%AF%86%E7%A0%81%E5%AD%A6)中又称**Rijndael加密法**，是[美国联邦政府](https://baike.baidu.com/item/%E7%BE%8E%E5%9B%BD%E8%81%94%E9%82%A6%E6%94%BF%E5%BA%9C)采用的一种区块加密标准。这个标准用来替代原先的[DES](https://baike.baidu.com/item/DES)，已经被多方分析且广为全世界所使用。经过五年的甄选流程，高级加密标准由[美国国家标准与技术研究院](https://baike.baidu.com/item/%E7%BE%8E%E5%9B%BD%E5%9B%BD%E5%AE%B6%E6%A0%87%E5%87%86%E4%B8%8E%E6%8A%80%E6%9C%AF%E7%A0%94%E7%A9%B6%E9%99%A2)（NIST）于2001年11月26日发布于FIPS PUB 197，并在2002年5月26日成为有效的标准。2006年，高级加密标准已然成为对称密钥加密中最流行的算法之一。
AES分为AES128、AES192、AES256秘钥长度分别是128bit、192bit、256bit三种。
本篇文章用的是计算量最大且最安全的AES256，AES256比128大概需要多花40%的时间，用于多出的4轮round key生成以及对应的SPN操作。另外，产生256-bit的密钥可能也需要比128位密钥多些开销，不过这部分开销应该可以忽略。

> 这些概念性的东西我就不在这里复制粘贴了，想要了解的请自行Google把。
下面我就介绍重头戏，我写的开源库**[StorageEncrypt](https://github.com/jiahongfei/StorageEncrypt)**。

### **[StorageEncrypt](https://github.com/jiahongfei/StorageEncrypt)**简介
这个库主要的功能：
1. 实现`SharedPreferences`接口对保存的`key`个`value`都进行`AES256`加密
2. 序列化自定义的`JavaBean`将用注释`EncryptString`标注的字段进行`AES256`加密

#### 1. AES256SharedPreferences 和 AES256Editor
AES256SharedPreferences和AES256Editor分别实现了SharedPreferences和Editor接口，其实就是SharedPreferences和Editor的包装器类。
如下代码就可以看出是包装器：
```
public class AES256SharedPreferences implements SharedPreferences {
    private SharedPreferences mSharedPreferences;
    public AES256SharedPreferences(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }
    ......
}

public static class AES256Editor implements Editor {
        private Editor mEditor;
        public AES256Editor(Editor editor) {
            this.mEditor = editor;
        }
        ......
}
```
##### AES256Editor
`AES256Editor`类中的每个方法都会对`key`和`value`进行`AES256`加密然后进行保存到本地。
其中基本类型，`int`、`long`、`float`、`boolean`等也是可以加密的，只不过我将他们转换成`String`类型进行加密存储。
如下示例代码：
```
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
        ......
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
        ......
    }
```

##### AES256SharedPreferences
`AES256SharedPreferences`类中的每个方法首先对`key`进行加密，然后通过加密后的`key`获取本地的`value`,然后对`value`进行解密返回。
其中基本类型，`int`、`long`、`float`、`boolean`等也是可以解密的。
如下示例代码：
```
public class AES256SharedPreferences implements SharedPreferences {
    private SharedPreferences mSharedPreferences;
    public AES256SharedPreferences(SharedPreferences sharedPreferences) {
        mSharedPreferences = sharedPreferences;
    }
   ......
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
    ......
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
    ......
}
```
##### 测试代码
```
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AES256SharedPreferences mAES256SharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAES256SharedPreferences = new AES256SharedPreferences(getSharedPreferences("test", Context.MODE_PRIVATE));
        String key = "key_SharedPreferences";
        String value = "value_SharedPreferences";
        mAES256SharedPreferences.edit().putString(key,value).commit();
        String getValue = mAES256SharedPreferences.getString(key,"");
        Log.e(TAG,"getValue : " + getValue);
    }
}
打印信息：
02-01 23:10:20.976 4156-4156/? E/MainActivity: getValue : value_SharedPreferences
```
如上代码我们发现`putString`和`getString`之后的`value`都是`value_SharedPreferences`,如果系统目录中保存的是加密过的那么证明这个代码是没问题的，如下代码：
```
<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
<map>
    <string name="ViJcid0p0zXvBc3hLvlSF3hOBMh/bP6dXgS/cfA6XjA=">6XwxdVncAcl5oa3Ja8JCkyqPVjaEDchSSJzVy9pg5c0=</string>
</map>
```
这段代码是系统目录中`AES256SharedPreferences `保存的`xml`文件，我们发现`key`和`value`都是加密过的。

#### 2. AES256SerializableObject
这个类是序列化对象的工具类，他可以将自定义的`JavaBean`中需要加密的字段进行`AES256`加密之后序列化到本地。
1. 可以用注解`EncryptString`来标注String字段需要加密。
2. 如果这个类中的字段是自定义的JavaBean，我们需要将这个JavaBean中的String字段进行加密，需要用EncryptPojo对自定义的JavaBean字段进行标注，然后用`EncryptString`来标注String字段，用语言表述太拗口了难以理解，看下面代码。

用代码进行讲解如下：
```
public class User implements Serializable {
    //表示序列化时需要将id进行AES256加密
    @EncryptString
    private String id;
    private String name;
    private String age;
    //表示自定义的Login类中有需要序列化时进行加密的字段
    @EncryptPojo
    private Login login;
    ......
}
public class Login implements Serializable {
    private String id;
    //这个字段序列化时需要加密
    @EncryptString
    private String token;
    @EncryptPojo
    private Member member;
    ......
}
public class Member implements Serializable {
    private String name;
    @EncryptString
    private String id;
    private String age;
    ......
}
```
##### 测试代码
```
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
//打印日志
02-01 21:12:07.177 9009-9009/com.andrjhf.storage.encrypt.demo E/MainActivity: AES save User : User{id='1234567890', name='jiahongfei', age='20', login=Login{id='29292929', token='token_5050505', member=Member{name='gaogaogao', id='10101010', age='25'}}}
02-01 21:12:07.177 9009-9009/com.andrjhf.storage.encrypt.demo E/MainActivity: 加密保存时间 : 6毫秒
02-01 21:12:07.178 9009-9009/com.andrjhf.storage.encrypt.demo E/MainActivity: AES read User : User{id='1234567890', name='jiahongfei', age='20', login=Login{id='29292929', token='token_5050505', member=Member{name='gaogaogao', id='10101010', age='25'}}}
02-01 21:12:07.178 9009-9009/com.andrjhf.storage.encrypt.demo E/MainActivity: 解密读取时间 : 1毫秒
02-01 21:12:07.179 9009-9009/com.andrjhf.storage.encrypt.demo E/MainActivity: Ser read User : User{id='YZ/uTPj54LnoewKLYMNeLg==', name='jiahongfei', age='20', login=Login{id='29292929', token='vu9wwwDUCMTJ8g9u2KIQ5A==', member=Member{name='gaogaogao', id='5M0yOju1oB28J5KiUVjfug==', age='25'}}}
02-01 21:12:07.179 9009-9009/com.andrjhf.storage.encrypt.demo E/MainActivity: 普通读取时间 : 1毫秒
```
我们看如上测试代码和打印日志，可以证明用`EncryptString`注释的字段都被AES256加密之后序列化到本地了。

### 总结：
目前对称加密方式AES、DES等都是公开的算法，加密最重要的就是**秘钥的生成以及秘钥存储的位置**，这才是根本，关于这点不在我们本篇文章的讨论范围后续在介绍。

**[StorageEncrypt github地址](https://github.com/jiahongfei/StorageEncrypt)**
