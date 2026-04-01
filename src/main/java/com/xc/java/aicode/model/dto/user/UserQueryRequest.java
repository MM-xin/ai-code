package com.xc.java.aicode.model.dto.user;

import com.xc.java.aicode.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户查询请求")
public class UserQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（前端雪花ID为字符串，用String接收避免精度丢失）
     */
    @Schema(description = "用户ID")
    private String id;

    /**
     * 账号
     */
    @Schema(description = "账号")
    private String userAccount;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String userName;

    /**
     * 角色：0-普通用户, 1-管理员, 2-超级管理员
     */
    @Schema(description = "角色")
    private Integer userRole;
}
