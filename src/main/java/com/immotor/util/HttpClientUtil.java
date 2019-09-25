package com.immotor.util;

import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String post(String url, String params,String token){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        if(!StringUtils.isEmpty(token)){
            headers.add("AccessToken",token);
        }
        HttpEntity<String> formEntity = new HttpEntity<String>(params, headers);
        String result = restTemplate.postForObject(url, formEntity, String.class);
        return result;
    }
    /**
     * 发起HTTP GET请求
     *
     * @param url URL
     * @return 响应结果
     */
    public static final String get(String url) {
        return execute(new HttpGet(url));
    }

    /**
     * 发起HTTP GET请求
     *
     * @param charset 编码
     * @param uri     URI
     * @param params  请求参数
     * @return 响应结果
     */
    public static final String get(String charset, String uri, List<NameValuePair> params) {
        String queryString = URLEncodedUtils.format(params, charset);
        return get(uri + "?" + queryString);
    }
    /**
     * 发起HTTP请求
     *
     * @param request HTTP请求
     * @return 响应结果
     */
    private static final String execute(HttpUriRequest request) {
        String body = "";
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            client = HttpClientBuilder.create().build();
            response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == org.apache.http.HttpStatus.SC_OK) {
                org.apache.http.HttpEntity entity = response.getEntity();
                body = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consumeQuietly(entity);
            } else {
                String formatter = "HTTP请求失败: %s, status: %s %s";
                StatusLine statusLine = response.getStatusLine();
                String msg = String.format(formatter, request.getRequestLine(), statusLine.getStatusCode(), statusLine.getReasonPhrase());
                throw new RuntimeException(msg);

            }
        } catch (IOException e) {
            throw new RuntimeException("HTTP请求异常: " + request.getRequestLine(), e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                logger.warn("Release resources failed!", e);
            }
        }
        return body;
    }



}
