package com.yangbo.seckill.seckill.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//登陆验证的工具包
public class ValidatorUtil {

    private static Logger logger = LoggerFactory.getLogger(ValidatorUtil.class);
    //正则表达式  1开头 后面需要10位数字
    public static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");
    //判断是否是手机号
    public static boolean isMobile(String src){
        if(StringUtils.isEmpty(src)){
            return false;
        }
        Matcher m  = mobile_pattern.matcher(src);
        return m.matches();
    }

//    public static void main(String[] args) {
//        System.out.println(isMobile("18912312341"));
//        System.out.println(isMobile("1891231"));
//    }
}
