package com.xc.java.aicode.ai;

import com.xc.java.aicode.ai.model.HtmlCodeResult;
import com.xc.java.aicode.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.SystemMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public interface AiCodeGeneratorService {

    /**
     * 生成单文件代码
     * @param userMessage 用户提示词
     * @return AI的输出结果
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    HtmlCodeResult generateHtmlCode(String userMessage);

    /**
     * 生成多文件代码
     * @param userMessage 用户提示词
     * @return AI的输出结果
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    MultiFileCodeResult generateMultiFileCode(String userMessage);

    /**
     * 生成单文件代码(流式)
     * @param userMessage 用户提示词
     * @return AI的输出结果
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    Flux<String> generateHtmlCodeStream(String userMessage);

    /**
     * 生成多文件代码(流式)
     * @param userMessage 用户提示词
     * @return AI的输出结果
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    Flux<String> generateMultiFileCodeStream(String userMessage);

}
