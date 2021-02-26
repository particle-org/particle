package com.particle.util.http;

import com.google.inject.ProvidedBy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 单个言论匹配接口
 */
@ProvidedBy(HttpConnectionProvider.class)
public interface IHttpConnectionApi {
    int send(String api, String content);

    CompletableFuture<String> postJson(String api, Map<String, String> headData, String content);

    CompletableFuture<String> postJson(String api, String content);
}
