package com.github.dapeng.util;

import com.google.gson.Gson;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * HTTP工具类
 */
public class HttpUtils {
    /**
     * 定义编码格式 UTF-8
     */
    public static final String URL_PARAM_DECODECHARSET_UTF8 = "UTF-8";
    /**
     * 定义编码格式 GBK
     */
    public static final String URL_PARAM_DECODECHARSET_GBK = "GBK";
    private static MultiThreadedHttpConnectionManager connectionManager = null;
    private static int connectionTimeOut = 25000;
    private static int socketTimeOut = 25000;
    private static int maxConnectionPerHost = 20;
    private static int maxTotalConnections = 20;
    private static HttpClient client;

    static {
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
        connectionManager.getParams().setSoTimeout(socketTimeOut);
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
        connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
        client = new HttpClient(connectionManager);
    }

    /**
     * 无参数的POST方式提交数据<br>
     * 编码默认为UTF-8
     *
     * @param url 待请求的URL
     * @return 响应结果
     */
    public static String doPost(String url) {
        return doPost(url, null, URL_PARAM_DECODECHARSET_UTF8);
    }

    /**
     * POST方式提交数据<br>
     * 编码默认为UTF-8
     *
     * @param url 待请求的URL
     * @return 响应结果
     */
    public static String doPost(String url, Map<String, String> params) {
        System.out.println(params);
        return doPost(url, params, URL_PARAM_DECODECHARSET_UTF8);
    }

    /**
     * POST方式提交数据
     *
     * @param url    待请求的URL
     * @param params 要提交的数据
     * @param enc    编码
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String doPost(String url, Map<String, String> params, String enc) {

        String response = null;
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);
            postMethod.setRequestHeader("Content-Type", "application/json;charset=" + enc);
            //postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + enc);
            if (params != null) {
                //将表单的值放入postMethod中
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    String value = params.get(key);
                    postMethod.addParameter(key, value == null ? "" : value);
                }
            }
            //执行postMethod
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED || statusCode == HttpStatus.SC_BAD_REQUEST) {
                response = postMethod.getResponseBodyAsString();
            } else {
                System.out.println("响应状态码 = " + postMethod.getStatusCode());
            }
        } catch (HttpException e) {
            System.out.println("发生致命的异常，可能是协议不对或者返回的内容有问题:" + e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("发生网络异常:" + e);
            e.printStackTrace();
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
                postMethod = null;
            }
        }
        return response;
    }

    /**
     * POST方式提交数据
     *
     * @param url    待请求的URL
     * @param params 要提交的数据
     * @param enc    编码
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String doPostJson(String url, String paramsJson, String enc) {
        enc = StringUtils.isBlank(enc) ? URL_PARAM_DECODECHARSET_UTF8 : enc;
        String response = "";
        PostMethod postMethod = null;
        try {
            postMethod = new PostMethod(url);
            //postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + enc);
            postMethod.setRequestHeader("Content-Type", "application/json;charset=" + enc);
            RequestEntity entity = new StringRequestEntity(paramsJson, "application/json", "UTF-8");
            postMethod.setRequestEntity(entity);

            //执行postMethod
            int statusCode = client.executeMethod(postMethod);
            if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED || statusCode == HttpStatus.SC_BAD_REQUEST) {
                response = postMethod.getResponseBodyAsString();
            } else {
                System.out.println("响应状态码 = " + postMethod.getStatusCode());
            }
        } catch (HttpException e) {
            System.out.println("发生致命的异常，可能是协议不对或者返回的内容有问题");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("发生网络异常");
            e.printStackTrace();
        } finally {
            if (postMethod != null) {
                postMethod.releaseConnection();
                postMethod = null;
            }
        }

        return response;
    }

    /**
     * POST方式提交数据
     *
     * @param url    待请求的URL
     * @param params 要提交的数据
     * @param enc    编码
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String doPostJson(String url, Map params, String enc) {
        return doPostJson(url, new Gson().toJson(params), enc);
    }


    /**
     * 无参数的GET方式提交数据<br>
     * 编码默认为UTF-8
     *
     * @param url 待请求的URL
     * @return 响应结果
     */
    public static String doGet(String url) {
        return doGet(url, null, URL_PARAM_DECODECHARSET_UTF8);
    }

    /**
     * GET方式提交数据<br>
     * 编码默认为UTF-8
     *
     * @param url 待请求的URL
     * @return 响应结果
     */
    public static String doGet(String url, Map<String, String> params) {
        return doGet(url, params, URL_PARAM_DECODECHARSET_UTF8);
    }

    /**
     * GET方式提交数据
     *
     * @param url    待请求的URL
     * @param params 要提交的数据
     * @param enc    编码
     * @return 响应结果
     * @throws IOException IO异常
     */
    public static String doGet(String url, Map<String, String> params, String enc) {

        String response = null;
        GetMethod getMethod = null;
        StringBuffer strtTotalURL = new StringBuffer(url);

        if (params != null) {
            if (strtTotalURL.indexOf("?") == -1) {
                strtTotalURL.append("?").append(getUrl(params, enc));
            } else {
                strtTotalURL.append("&").append(getUrl(params, enc));
            }
        }
        System.out.println("GET请求URL = \n" + strtTotalURL.toString());

        try {
            getMethod = new GetMethod(strtTotalURL.toString());
            getMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + enc);
            //执行getMethod
            int statusCode = client.executeMethod(getMethod);
            if (statusCode == HttpStatus.SC_OK) {
                response = getMethod.getResponseBodyAsString();
            } else {
                System.out.println("响应状态码 = " + getMethod.getStatusCode());
            }
        } catch (HttpException e) {
            System.out.println("发生致命的异常，可能是协议不对或者返回的内容有问题");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("发生网络异常");
            e.printStackTrace();
        } finally {
            if (getMethod != null) {
                getMethod.releaseConnection();
                getMethod = null;
            }
        }

        return response;
    }

    /**
     * 据Map生成URL字符串
     *
     * @param map      Map
     * @param valueEnc URL编码
     * @return URL
     */
    private static String getUrl(Map<String, String> map, String valueEnc) {
        StringBuffer url = new StringBuffer();
        Set<Entry<String, String>> set = map.entrySet();
        for (Entry<String, String> entry : set) {
            String key = entry.getKey();
            String value = entry.getValue() == null ? "" : entry.getValue();
            try {
                url.append(key).append("=").append(URLEncoder.encode(value, valueEnc)).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return url.lastIndexOf("&") > -1 ? url.substring(0, url.lastIndexOf("&")) : url.toString();
    }

}
