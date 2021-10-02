package com.yangbo.seckill.seckill.validator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

//自定义注解
//注解的适用范围   方法/字段/构造方法/方法参数
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
//定义注解的生命周期   运行期
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {IsMobileValidator.class}
)

public @interface IsMobile {
    //参数设置  类型  参数  默认值
    boolean required() default true;
    String message() default "手机格式错误";
    Class<?>[] groups() default { };
    Class<? extends Payload> [] payload() default { };
}
