package com.galaxy.base.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author 玩你个球儿
 * @since 2020-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EduAdmin对象", description="")
public class EduAdmin implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String nickName;
    private String username;

    private String password;

    private String avatar;

    @TableLogic
    private Boolean did;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime eduCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime eduModified;


}
