package com.xc.java.aicode.model.dto.app;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 应用创建请求
 */
@Data
@Schema(description = "应用创建请求")
public class AppAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用初始化 prompt（必填）
     */
    @Schema(description = "应用初始化 prompt")
    private String initPrompt;

}
