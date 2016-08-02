/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.alibaba.webx.tutorial1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {

    private static CloseableHttpClient httpClient;
    private static final int           MAX_TOTAL          = 500;
    private static final int           MAX_PER_ROUTE      = 100;
    private static final int           SO_TIMEOUT         = 6000;
    private static final int           CONNECTION_TIMEOUT = 3000;
    private static final Logger        logger             = LoggerFactory.getLogger(HttpClientUtil.class);

    static {
        final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(MAX_TOTAL);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        httpClient = HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager).build();
    }

    public static HttpResult getRequest(String url, int retryTimes) throws Exception {
        return getRequest(url, null, retryTimes);
    }

    public static HttpResult getRequest(String url, Map<String, String> headers, int retryTimes) throws Exception {

        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("url can not be blank");
        }

        HttpResult httpResult = HttpResult.succResult();
        CloseableHttpResponse response = null;
        while (retryTimes >= 0) {
            int statusCode = 0;
            try {
                HttpGet httpGet = new HttpGet(url);
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SO_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).build();
                httpGet.setConfig(requestConfig);
                addHeaders(httpGet, headers);
                
                response = httpClient.execute(httpGet);
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    httpResult.setCode(statusCode);
                    httpResult.setResultCode(HttpResult.ERROR);
                    httpResult.setResultInfo("The body of get is empty,url is " + url);
                    logger.error("The response from HttpGet is not 200 ; url is {}", url);
                    retryTimes--;
                    continue;
                }
                String result = EntityUtils.toString(response.getEntity());
                if (result != null) {
                    httpResult.setResultCode(HttpResult.OK);
                    httpResult.setCode(statusCode);
                    httpResult.setContent(result);
                    httpResult.setResultInfo(null);
                    return httpResult;
                } else {
                    httpResult.setCode(statusCode);
                    httpResult.setResultCode(HttpResult.ERROR);
                    httpResult.setResultInfo("The body of get is empty,url is " + url);
                    logger.error("The body of get is empty,url is " + url);
                    retryTimes--;
                    continue;
                }
            } catch (Exception e) {
                httpResult.setCode(statusCode);
                httpResult.setResultCode(HttpResult.ERROR);
                httpResult.setResultInfo(e.getMessage());
                logger.error("get request error,url=" + url + ",retryTimes=" + retryTimes, e);
                retryTimes--;
                continue;
            } finally {
                try {
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    logger.error("exception occured while close http client connection", e);
                }
            }
        }
        return httpResult;
    }

    /**
     * post请求
     * 
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static HttpResult postRequest(String url, Map<String, String> params, int retryTimes) throws Exception {

        return postRequest(url, params, null,retryTimes);
    }

    /**
     * post请求，带修改请求�?     * 
     * @param url
     * @param entity
     * @param headers
     * @param retryTimes
     * @return
     */
    public static HttpResult postRequest(String url, Map<String, String> params, Map<String, String> headers, int retryTimes)
                                                                                                             throws Exception {
        if (StringUtils.isBlank(url)) {
            throw new RuntimeException("url can not be blank");
        }

        HttpResult httpResult = HttpResult.succResult();
        CloseableHttpResponse response = null;
        while (retryTimes >= 0) {
            int statusCode = 0;
            try {
            	HttpPost httpPost = new HttpPost(url);
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SO_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).build();
                httpPost.setConfig(requestConfig);


                addFormparams(httpPost, params);
                addHeaders(httpPost, headers);
                
                response = httpClient.execute(httpPost);
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    httpResult.setCode(statusCode);
                    httpResult.setResultCode(HttpResult.ERROR);
                    httpResult.setResultInfo("The body of get is empty,url is " + url);
                    logger.error("The response from HttpGet is not 200 ; url is {}", url);
                    retryTimes--;
                    continue;
                }
                String result = EntityUtils.toString(response.getEntity());
                if (result != null) {
                    httpResult.setResultCode(HttpResult.OK);
                    httpResult.setCode(statusCode);
                    httpResult.setContent(result);
                    httpResult.setResultInfo(null);
                    return httpResult;
                } else {
                    httpResult.setCode(statusCode);
                    httpResult.setResultCode(HttpResult.ERROR);
                    httpResult.setResultInfo("The body of get is empty,url is " + url);
                    logger.error("The body of get is empty,url is " + url);
                    retryTimes--;
                    continue;
                }
            } catch (Exception e) {
                httpResult.setCode(statusCode);
                httpResult.setResultCode(HttpResult.ERROR);
                httpResult.setResultInfo(e.getMessage());
                logger.error("get request error,url=" + url + ",retryTimes=" + retryTimes, e);
                retryTimes--;
                continue;
            } finally {
                try {
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    logger.error("exception occured while close http client connection", e);
                }
            }
        }
        return httpResult;
    
    }

    private static void addHeaders(HttpGet get, Map<String, String> headers) {

        if (headers == null || headers.isEmpty()) return;

        Set<String> keys = headers.keySet();
        for (String key : keys) {
            if (StringUtils.isBlank(key)) continue;

            String val = headers.get(key);
            if (StringUtils.isBlank(val)) continue;

            get.setHeader(key, val);
        }
    }

    private static void addFormparams(HttpPost post, Map<String, String> params) throws UnsupportedEncodingException {

        if (params == null || params.isEmpty()) return;

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();

        Set<String> keys = params.keySet();
        for (String key : keys) {
            if (StringUtils.isBlank(key)) continue;

            String value = params.get(key);
            value = StringUtils.isBlank(value) ? "" : value;

            formparams.add(new BasicNameValuePair(key, value));
        }

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
        post.setEntity(entity);
    }

    private static void addHeaders(HttpPost post, Map<String, String> headers) {

        if (headers == null || headers.isEmpty()) return;

        Set<String> keys = headers.keySet();
        for (String key : keys) {
            if (StringUtils.isBlank(key)) continue;

            String val = headers.get(key);
            if (StringUtils.isBlank(val)) continue;

            post.setHeader(key, val);
        }
    }

}
