package com.yangbo.seckill.seckill.util;
import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;
//设置密码加密

public class MD5Util {
    //设置固定值
    public static final String salt = "1a2b3c4d";

    public static String md5(String src){
        //将字符串加密
        return DigestUtils.md5DigestAsHex(src.getBytes(StandardCharsets.UTF_8));
    }

    //第一次加密
    public static String inputPassFormPass(String inputPass){
        //拼接字符串   salt穿插
        String str = ""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }
    //二次加密
    public static String formPassToDbPass(String formPass,String salt){
        String str = ""+salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    //总方法
    public static String inputPassToDbPass(String input,String saltDb){
        String dbPass = formPassToDbPass(inputPassFormPass(input), saltDb);
        return dbPass;
    }

//    //测试 密码是多少
//    public static void main(String[] args) {
//        System.out.println(inputPassFormPass("13662019580"));
//        //输出  d3b1294a61a07da9b49b6e22b2cbd7f9
//        System.out.println(formPassToDbPass(inputPassFormPass("13662019580"),"1a2b3c"));
//    }
}
