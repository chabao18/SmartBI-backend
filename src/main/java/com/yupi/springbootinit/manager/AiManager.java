package com.yupi.springbootinit.manager;

import cn.hutool.json.JSONObject;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import org.springframework.stereotype.Service;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Service
public class AiManager {

    private static final String OPENAI_API_LEY = "hk-4d9lay1000015625834f0731a653ffdc163206451446100d";

    public String doChat(String userContent) {
        String endpoint = "https://api.openai-hk.com/v1/chat/completions";
        String sysContent = "你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\\n" +
                "分析需求：\\n" +
                "{数据分析的需求或者目标}\\n" +
                "原始数据：\\n" +
                "{csv格式的原始数据，用,作为分隔符}\\n" +
                "请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\\n" +
                "【【【【【\\n" +
                "{前端 Echarts V5 的 option 配置对象js代码，合理地将数据进行可视化，不要生成任何多余的内容，比如注释}\\n" +
                "【【【【【\\n" +
                "{明确的数据分析结论、越详细越好，不要生成多余的注释}";

//        userContent = "分析需求：\\n" +
//                "分析网站用户的增长情况\\n" +
//                "原始数据：\\n" +
//                "日期,用户数\\n" +
//                "1号， 10\\n" +
//                "2号， 20\\n" +
//                "3号， 30";

        String payload = String.format("{\n" +
                "    \"model\": \"gpt-3.5-turbo-0613\",\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"system\",\n" +
                "            \"content\": \"%s\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"role\": \"user\",\n" +
                "            \"content\": \"%s\"\n" +
                "        }\n" +
                "    ]\n" +
                "}", sysContent, userContent);

        try {
            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + OPENAI_API_LEY);
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (Scanner scanner = new Scanner(connection.getInputStream())) {
                String responseBody = scanner.useDelimiter("\\A").next();
                JSONObject jsonResponse = new JSONObject(responseBody);

                // 获取choices数组中的第一个元素
                JSONObject choice = jsonResponse.getJSONArray("choices").getJSONObject(0);
                String content = choice.getJSONObject("message").getStr("content");

                // 输出或进一步处理content字段
                return content;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }

}