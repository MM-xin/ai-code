package com.xc.java.aicode.core;

import com.xc.java.aicode.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private  AiCodeGeneratorFacade aiCodeGenerator;

    @Test
    void generateAndSaveCode() {
        File file = aiCodeGenerator.generateAndSaveCode("做个简单闹钟页面，不超过三十行", CodeGenTypeEnum.HTML);
        Assertions.assertNotNull(file);
    }

    @Test
    void generateAndSaveCode2() {
        File file = aiCodeGenerator.generateAndSaveCode("做个简单闹钟页面，不超过三十行", CodeGenTypeEnum.MULTI_FILE);
        Assertions.assertNotNull(file);
    }

}