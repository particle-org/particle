package com.particle.util.http.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.particle.util.http.IHttpConnectionApi;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 单个言论匹配API接口的实现版本
 */
public class OKHttpImpl implements IHttpConnectionApi {

    private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain; charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");


    private static final Logger LOGGER = LoggerFactory.getLogger(OKHttpImpl.class);

    private static final int MAX_IDLE_CONNECTIONS = 2;
    private static final int RETRY_MAX_TIME = 3;

    private static final long KEEP_ALIVE_DURATION = 60000;
    private static final int CONNECT_TIMEOUT = 1000;
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    private OkHttpClient client;

    private ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setDaemon(true).build());

    /**
     * 初始化OKHttp
     * 该步骤会初始化一个全局连接池，一个OkHttpClient，并配置好超时时间、最大连接数、连接保持时间
     */
    public OKHttpImpl() {
        ConnectionPool connectionPool = new ConnectionPool(
                MAX_IDLE_CONNECTIONS,
                KEEP_ALIVE_DURATION,
                TIME_UNIT);

        client = new OkHttpClient.Builder()
                .connectionPool(connectionPool)
                .connectTimeout(CONNECT_TIMEOUT, TIME_UNIT)
                .build();
    }

    /**
     * 发送Http POST请求，该步骤会以递归的方式处理连接失败的情况
     *
     * @param content 发送内容
     * @return 识别结果
     */
    public int send(String api, String content) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_TEXT, content);

        Request request = new Request.Builder()
                .url(api)
                .post(requestBody)
                .build();

        return send(request, 0);
    }

    @Override
    public CompletableFuture<String> postJson(String url, Map<String, String> headData, String content) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, content);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);

        if (headData != null) {
            for (Map.Entry<String, String> headDatum : headData.entrySet()) {
                requestBuilder.addHeader(headDatum.getKey(), headDatum.getValue());
            }
        }

        return postJson(requestBuilder.build());
    }

    @Override
    public CompletableFuture<String> postJson(String url, String content) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_JSON, content);

        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(requestBody);

        return postJson(requestBuilder.build());
    }

    private CompletableFuture<String> postJson(Request request) {
        return CompletableFuture.supplyAsync(() -> {
            try (Response response = client.newCall(request).execute()) {
                ResponseBody responseBody = response.body();

                if (responseBody == null) {
                    return null;
                } else {
                    return responseBody.string();
                }
            } catch (IOException e) {
                LOGGER.error("Fail to post json {} to {}", request.body(), request.url());

                return null;
            }
        }, this.executor);
    }

    private int send(Request request, int retryTime) {
        Response response = null;

        try {
            response = client.newCall(request).execute();

            return response.code();
        } catch (SocketTimeoutException e) {
            if (retryTime < RETRY_MAX_TIME) {
                LOGGER.error("Can't connect to review API, retry times " + retryTime);

                return send(request, retryTime + 1);
            } else {
                LOGGER.error("Fail connect to review API!");
                e.printStackTrace();

                return 400;
            }
        } catch (IOException e) {
            LOGGER.error("Fail connect to review API!");
            e.printStackTrace();
        } finally {
            if (response != null)
                response.close();
        }

        return 0;
    }
}
