package com.pang;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Pang
 * @date 2021/4/22
 */
public class Main {
    private static final OkHttpClient CLIENT = new OkHttpClient().newBuilder()
            .build();
    private static final String ACCESS_TOKEN = "access_token";
    /*申请的gitee token*/
    private static final String ACCESS_TOKEN_VALUE = "";
    /*gitee 用户名*/
    private static final String GITEE_USER_NAME = "";
    /*gitee 的仓库名*/
    private static final String GITEE_RESP_NAME = "";
    private static final String CONTENT = "content";
    private static final String MESSAGE = "message";
    private static final String MESSAGE_VALUE = "上传图片";
    private static final String GITEE_BASE_URL = "https://gitee.com";
    private static final String GITEE_UPLOAD_URL = GITEE_BASE_URL + "/api/v5/repos/" + GITEE_USER_NAME + "/" + GITEE_RESP_NAME + "/contents/";


    public static void main(String[] args) throws IOException {
        List<String> result=new LinkedList<>();

        for (String path : args) {
            JSONObject param = new JSONObject();
            File file = new File(path);
            String fileName = file.getName();
            InputStream fileStream = new FileInputStream(file);
            byte[] fileBytes = fileStream.readAllBytes();
            String encodedFileString = Base64.getEncoder().encodeToString(fileBytes);
            String remoteFilePath = "img/" + System.currentTimeMillis() + "/" + fileName;
            String realUploadUrl = GITEE_UPLOAD_URL + URLEncoder.encode(remoteFilePath, StandardCharsets.UTF_8);
            MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
            param.put(ACCESS_TOKEN, ACCESS_TOKEN_VALUE);
            param.put(CONTENT, encodedFileString);
            param.put(MESSAGE, MESSAGE_VALUE);

            RequestBody body = RequestBody.create(mediaType, param.toJSONString());

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


            Response response = CLIENT.newCall(request).execute();

            JSONObject responseJsonObj = JSON.parseObject(response.body().string());

            String downloadPath = responseJsonObj.getJSONObject("content").getString("download_url");
            result.add(downloadPath);
        }

        for (String s : result) {
            System.out.println(s);
        }

    }

}
