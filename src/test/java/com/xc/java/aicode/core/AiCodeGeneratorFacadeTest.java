package com.xc.java.aicode.core;

import com.xc.java.aicode.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource(name = "aiCodeGeneratorFacadeV2")
    private  AiCodeGeneratorFacadeV2 aiCodeGenerator;

    @Test
    void generateAndSaveCode() {
        File file = aiCodeGenerator.generateAndSaveCode("做个简单闹钟页面，不超过三十行", CodeGenTypeEnum.HTML, 1L);
        Assertions.assertNotNull(file);
    }

    @Test
    void generateAndSaveCode2() {
        File file = aiCodeGenerator.generateAndSaveCode("做个简单闹钟页面，不超过三十行", CodeGenTypeEnum.MULTI_FILE, 1L);
        Assertions.assertNotNull(file);
    }

    @Test
    void generateAndSaveCodeStream() {
        Flux<String> codeStream = aiCodeGenerator.generateAndSaveCodeStream("做个简单登录页面，不超过三十行", CodeGenTypeEnum.HTML, 1L);
        // 阻塞等待所有数据收集完成
        List<String> result = codeStream.collectList().block();
        Assertions.assertNotNull(result);
        // 拼接字符串，得到完整结果
        String code = String.join("", result);
        Assertions.assertNotNull(code);
    }

    @Test
    void generateAndSaveCodeStream2() {
        Flux<String> codeStream = aiCodeGenerator.generateAndSaveCodeStream("做个简单登录页面，不超过三十行", CodeGenTypeEnum.MULTI_FILE, 1L);
        // 阻塞等待所有数据收集完成
        List<String> result = codeStream.collectList().block();
        Assertions.assertNotNull(result);
        // 拼接字符串，得到完整结果
        String code = String.join("", result);
        Assertions.assertNotNull(code);
    }

}