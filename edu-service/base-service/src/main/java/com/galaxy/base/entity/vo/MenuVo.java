package com.galaxy.base.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.galaxy.base.entity.EduMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class MenuVo implements Serializable {

    private static final long serialVersionUID =33L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "上级id")
    private Integer mid;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "0 菜单 1 按钮")
    private Integer type;

    @ApiModelProperty(value = "权限")
    private String authority;

    @ApiModelProperty(value = "层级")
    @TableField(exist = false)
    private Integer level;

    @ApiModelProperty(value = "下级")
    @TableField(exist = false)
    private List<EduMenu> children;

    @ApiModelProperty(value = "是否选中")
    @TableField(exist = false)
    private boolean isSelect;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "组件")
    private String component;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "0 禁止 1 正常")
    private Integer status;

    private List  secondMenu;
    private List  thirdMenu;

}
