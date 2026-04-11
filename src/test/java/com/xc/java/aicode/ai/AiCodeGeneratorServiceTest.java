package com.xc.java.aicode.ai;

import com.xc.java.aicode.ai.model.HtmlCodeResult;
import com.xc.java.aicode.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode() {
        HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode("做个简单闹钟页面,不超过五十行");
        Assertions.assertTrue(true);
    }

    @Test
    void generaMultiFileCode() {
        MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode("做个简单闹钟页面,不超过五十行");
        Assertions.assertTrue(true);
    }

}