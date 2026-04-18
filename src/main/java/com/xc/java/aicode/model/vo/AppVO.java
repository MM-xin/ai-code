package com.xc.java.aicode.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 应用视图对象
 */
@Data
@Schema(description = "应用视图对象")
public class AppVO implements Serializable {

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
     * 应用初始化 prompt
     */
    @Schema(description = "应用初始化 prompt")
    private String initPrompt;

    /**
     * 代码生成类型
     */
    @Schema(description = "代码生成类型")
    private String codeGenType;

    /**
     * 部署标识
     */
    @Schema(description = "部署标识")
    private String deployKey;

    /**
     * 部署时间
     */
    @Schema(description = "部署时间")
    private LocalDateTime deployedTime;

    /**
     * 优先级
     */
    @Schema(description = "优先级")
    private Integer priority;

    /**
     * 创建用户 ID
     */
    @Schema(description = "创建用户 ID")
    private Long userId;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    /**
     * 创建用户信息
     */
    @Schema(description = "用户信息")
    private UserVO user;
}
