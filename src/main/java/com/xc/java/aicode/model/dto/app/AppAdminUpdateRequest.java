package com.xc.java.aicode.model.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理员更新应用请求
 */
@Data
@Schema(description = "管理员更新应用请求")
public class AppAdminUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用 ID
     */
    @Schema(description = "应用 ID")
    private Long id;

    /**
     * 应用名称
     */
    @Schema(description = "应用名称")
    private String appName;

    /**
     * 应用封面
     */
    @Schema(description = "应用封面")
    private String cover;

    /**
     * 优先级
     */
    @Schema(description = "优先级")
    private Integer priority;
}
