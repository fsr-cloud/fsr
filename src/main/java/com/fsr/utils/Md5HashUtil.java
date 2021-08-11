package com.fsr.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

public class Md5HashUtil {
    public static String getMd5Hash(String password,String salt,int hashIterations) {
        return new Md5Hash(password,salt,hashIterations).toHex();
    }


}
