package com.yangbo.seckill.seckill.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@ApiModel(value = "参数校验类")
public class ValidVo {
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty(value = "应用ID",example = "cloud")
    private String appId;

    @NotEmpty(message = "级别不能为空")
    @ApiModelProperty(value = "级别")
    private String level;

    @ApiModelProperty(value = "年龄")
    private int age;

}
