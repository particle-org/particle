package com.particle.util.http.impl;


import com.particle.util.http.IHttpConnectionApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 单个言论匹配API接口的实现版本
 */
public class HttpClientImpl implements IHttpConnectionApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientImpl.class);

    private static final int RETRY_MAX_TIME = 3;

    private static final int CONNECT_TIMEOUT = 1000;

    public int send(String api, String content) {
        return send(api, content, 0);
    }

    @Override
    public CompletableFuture<String> postJson(String api, Map<String, String> headData, String content) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public CompletableFuture<String> postJson(String api, String content) {
        throw new RuntimeException("Not implemented");
    }

    private int send(String api, String content, int retryTime) {
        HttpURLConnection connection = null;
        int result = 400;

        try {
            URL realUrl = new URL(api);

            // 打开连接
            connection = (HttpURLConnection) realUrl.openConnection();

            // 发起POST请求
            connection.setDoOutput(true);
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setConnectTimeout(CONNECT_TIMEOUT);

            // 发送请求参数
            connection.getOutputStream().write(content.getBytes("UTF-8"));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();

            while (connection.getInputStream().read() != -1) {
            }

            result = Integer.parseInt(connection.getHeaderField(0).split(" ")[1]);
        } catch (SocketTimeoutException e) {
            if (retryTime < RETRY_MAX_TIME) {
                LOGGER.error("Can't connect to review API, retry times " + retryTime);

                result = send(api, content, retryTime + 1);
            } else {
                LOGGER.error("Fail connect to review API!");
                e.printStackTrace();

                result = 400;
            }
        } catch (IOException e) {
            LOGGER.error("Fail connect to review API!");
            e.printStackTrace();

            result = 400;
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            LOGGER.error("Server respond error!");
            e.printStackTrace();

            result = 504;
        } finally {
            //关闭输出流、输入流
            try {
                if (connection != null) {
                    connection.getInputStream().close();
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
