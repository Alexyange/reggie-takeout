package com.itheima.reggie.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author 黑马程序员
 * @since 2022-02-16
 */
@Data
public class Friend implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonProperty("friendName")
    private String friendName;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonProperty("friendId")
    private Long friendId;

    private String nickname;

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", friend_name=" + friendName +
                ", friend_id=" + friendId +
                ", nickname=" + nickname +
                "}";
    }
}
