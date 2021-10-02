package com.yangbo.seckill.seckill.validator;

import com.yangbo.seckill.seckill.util.ValidatorUtil;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile,String > {

    public boolean require = false;
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
        require =constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(require){
            //调用 ValidatorUtil里面的isMobile 是否是手机号进行验证
            return ValidatorUtil.isMobile(value);
        }else {
            if (StringUtils.isEmpty(value)) {
                return true;
            }else {
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
