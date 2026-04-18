package com.xc.java.aicode.model.dto.app;

import com.xc.java.aicode.common.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 管理员应用分页查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "管理员应用分页查询请求")
public class AppQueryRequest extends PageRequest implements Serializable {

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
     * 是否删除
     */
    @Schema(description = "是否删除")
    private Integer isDelete;
}
