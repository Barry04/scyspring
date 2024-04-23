package org.scy.scyspring.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName user_info
 */
@TableName(value ="user_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {
    /**
     * 
     */
    @TableId
    private String uuid;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String describes;

    /**
     * 
     */
    private String userphone;

    /**
     * 
     */
    private Date birthday;

    /**
     * 
     */
    private Integer age;

    /**
     * 
     */
    private String sex;

    /**
     * 
     */
    private String nickname;

    /**
     * 
     */
    private String email;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public UserInfo(String uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }
}