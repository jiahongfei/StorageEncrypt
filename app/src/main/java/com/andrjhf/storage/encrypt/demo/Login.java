package com.andrjhf.storage.encrypt.demo;

import android.os.Parcel;
import android.os.Parcelable;

import com.andrjhf.storage.encrypt.anns.Encrypt;
import com.andrjhf.storage.encrypt.anns.EncryptPojo;

import java.io.Serializable;

/**
 * @author :  jiahongfei
 * @email : jiahongfeinew@163.com
 * @date : 2018/1/31
 * @desc :
 */

public class Login implements Serializable, Parcelable {

    private String id;
    @Encrypt
    private String token;
    @EncryptPojo
    private Member member;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "Login{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", member=" + member +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.token);
        dest.writeSerializable(this.member);
    }

    public Login() {
    }

    protected Login(Parcel in) {
        this.id = in.readString();
        this.token = in.readString();
        this.member = (Member) in.readSerializable();
    }

    public static final Parcelable.Creator<Login> CREATOR = new Parcelable.Creator<Login>() {
        @Override
        public Login createFromParcel(Parcel source) {
            return new Login(source);
        }

        @Override
        public Login[] newArray(int size) {
            return new Login[size];
        }
    };
}
