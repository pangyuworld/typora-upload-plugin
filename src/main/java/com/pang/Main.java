package com.pang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import static com.pang.Config.*;

/**
 * @author Pang
 * @date 2021/4/22
 */
public class Main {
    private static final OkHttpClient CLIENT = new OkHttpClient().newBuilder()
            .build();
    private static final String ACCESS_TOKEN = "access_token";
    private static final String CONTENT = "content";
    private static final String MESSAGE = "message";
    private static final String MESSAGE_VALUE = "上传图片";
    private static final String GITEE_BASE_URL = "https://gitee.com";
    private static final String GITEE_UPLOAD_URL = GITEE_BASE_URL + "/api/v5/repos/" + GITEE_USER_NAME + "/" + GITEE_RESP_NAME + "/contents/";


    public static void main(String[] args) throws IOException {
        List<String> result = new LinkedList<>();

        for (String path : args) {
            JSONObject param = new JSONObject();
            File file = new File(path);
            String fileName = file.getName();
            // 读取文件内容
            byte[] fileBytes = readFileContent(file);
            // 进行base64编码
            String encodedFileString = Base64.getEncoder().encodeToString(fileBytes);
            // 文件远程路径的url编码
            String remoteFilePath = "img/" + System.currentTimeMillis() + "/" + fileName;
            // 实际上传url
            String realUploadUrl = GITEE_UPLOAD_URL + URLEncoder.encode(remoteFilePath);
            // 上传的文件类型
            MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
            // 构建参数
            param.put(ACCESS_TOKEN, ACCESS_TOKEN_VALUE);
            param.put(CONTENT, encodedFileString);
            param.put(MESSAGE, MESSAGE_VALUE);
            // 构建访问请求
            RequestBody body = RequestBody.create(mediaType, param.toJSONString());
            // 访问
            Request request = new Request.Builder()
                    .url(realUploadUrl)
                    .method("POST", body)
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Accept", "application/json, text/plain, */*")
                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                    .addHeader("Origin", "https://gitee.com")
                    .addHeader("Referer", "https://gitee.com/api/v5/swagger")
                    .addHeader("Accept-Language", "zh,en;q=0.9,zh-CN;q=0.8")
                    .build();
            // 获取访问结果
            Response response = CLIENT.newCall(request).execute();
            // 将结果转换为json
            JSONObject responseJsonObj = JSON.parseObject(response.body().string());
            // 获取到下载链接
            String downloadPath = responseJsonObj.getJSONObject("content").getString("download_url");
            // 添加到结果
            result.add(downloadPath);
        }

        for (String s : result) {
            System.out.println(s);
        }

    }

    /**
     * 读取文件内容
     *
     * @param file 文件
     * @return 文件内容的编码
     */
    public static byte[] readFileContent(File file) {
        byte[] fileBytes = new byte[0];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileBytes = new byte[(int) file.length()];
            fileInputStream.read(fileBytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBytes;
    }
}
