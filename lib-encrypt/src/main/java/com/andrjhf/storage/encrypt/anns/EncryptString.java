package com.andrjhf.storage.encrypt.anns;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author :  jiahongfei
 * @email : jiahongfeinew@163.com
 * @date : 2018/1/31
 * @desc :  javabean字段注释这个表示需要进行AES256加密，目前只支持String类型的加密
 */
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.FIELD})//定义注解的作用目标**作用范围字段、枚举的常量
@Documented//说明该注解将被包含在javadoc中
public @interface EncryptString {
}
