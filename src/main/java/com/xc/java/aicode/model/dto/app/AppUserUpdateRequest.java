package com.xc.java.aicode.model.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新应用请求
 */
@Data
@Schema(description = "用户更新应用请求")
public class AppUserUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用 ID
     */
    @Schema(description = "应用 ID")
    private Long id;

    /**
     * 应用名称（仅支持修改该字段）
     */
    @Schema(description = "应用名称")
    private String appName;
}
