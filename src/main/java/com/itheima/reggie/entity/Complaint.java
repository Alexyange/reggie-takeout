package com.itheima.reggie.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author 黑马程序员
 * @since 2022-02-15
 */
@Data
public class Complaint implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long friendId;

    private String content;


    private LocalDateTime time;

}
