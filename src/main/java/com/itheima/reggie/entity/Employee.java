package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName Employee.java
 * @Description TODO
 * @createTime 2022年01月21日 20:01:00
 */
@Data
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    //员工ID
    private Long id;

    //员工账号
    private String username;

    //员工姓名
    private String name;

    //员工的密码
    private String password;

    //电话
    private String phone;

    //性别
    private String sex;

    //身份证号
    private String idNumber; //驼峰命名法 ---> 映射的字段名为 id_number

    //状态 0:禁用，1:正常
    private Integer status;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //创建人
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    //修改人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
