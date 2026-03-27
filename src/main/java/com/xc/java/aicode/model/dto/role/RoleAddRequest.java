package com.xc.java.aicode.model.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色添加请求
 */
@Data
@Schema(description = "角色添加请求")
public class RoleAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称")
    private String roleName;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码")
    private String roleCode;

    /**
     * 角色描述
     */
    @Schema(description = "角色描述")
    private String description;
}
