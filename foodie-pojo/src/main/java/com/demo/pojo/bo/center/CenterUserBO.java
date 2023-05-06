package com.demo.pojo.bo.center;


import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@ToString
public class CenterUserBO {


    private String username;
    @ToString.Exclude
    private String password;
    @ToString.Exclude
    private String confirmPassword;


    @NotBlank(message = "用户昵称不能为空")
    @Length(max = 12, message = "用户昵称不能超过12位")
    private String nickname;

    @Length(max = 12, message = "用户真实姓名不能超过12位")
    private String realname;

    @Pattern(regexp = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\\d{8})$", message = "手机号格式不正确")
    private String mobile;

    @Email
    @ToString.Exclude
    private String email;

    @Min(value = 0, message = "性别选择不正确")
    @Max(value = 2, message = "性别选择不正确")
    @ToString.Exclude
    private Integer sex;
    @ToString.Exclude
    private Date birthday;

}