package com.andrjhf.storage.encrypt.demo;

import com.andrjhf.storage.encrypt.anns.Encrypt;
import com.andrjhf.storage.encrypt.anns.EncryptPojo;

import java.io.Serializable;

/**
 * @author :  jiahongfei
 * @email : jiahongfeinew@163.com
 * @date : 2018/1/25
 * @desc :
 */

public class User implements Serializable {

    @Encrypt
    private String id;
    private String name;
    private String age;
    @EncryptPojo
    private Login login;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", login=" + login +
                '}';
    }
}
