package com.yangbo.seckill.seckill.controller;

import com.yangbo.seckill.seckill.vo.ValidVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;

@RestController
@Api(tags = "参数校验")
@Slf4j
@Validated  //？？？？？
public class SwaggerController {
    @PostMapping("/valid/test1")
    @ApiOperation("RequestBody校验")
    public String test1(@Validated @RequestBody ValidVo validVO){
        log.info("validEntity is {}", validVO);
        return "test1 valid success";
    }

    @ApiOperation("Form校验")
    @PostMapping(value = "/valid/test2")
    public String test2(@Validated ValidVo validVO){
        log.info("validEntity is {}", validVO);
        return "test2 valid success";
    }

    @ApiOperation("单参数校验")
    @PostMapping(value = "/valid/test3")
    public String test3(@Email String email){
        log.info("email is {}", email);
        return "email valid success";
    }
}

