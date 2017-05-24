package com.like.alipay.sample;

import android.content.Context;

import com.like.logger.Logger;
import com.like.retrofit.request.security.AESUtil;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;


/**
 * 数据模型的解析、以及错误处理
 *
 * @author like
 * @version v1.0.0
 * @创建日期 2016年2月16日 下午1:55:55
 */
public class ResponseParser {

    /**
     * 身份验证失败，由于多个手机登录同一个账号导致，此时就跳转到登录界面
     */
    private static final int STATE_AUTHENTICATION_FAILURE = -10;
    /**
     * userid或者token无效，由于锁屏导致数据被android系统清除，此时就跳转到登录界面
     */
    private static final int STATE_USERID_OR_TOKEN_FAILURE = -2;
    /**
     * 其它错误
     */
    private static final int STATE_OTHER_FAILURE = -1;
    /**
     * 请求完全成功
     */
    private static int STATE_SUCCESS = 0;

    public int state = STATE_OTHER_FAILURE;
    public String message = "";
    public String data = "";

    /**
     * 构造方法.
     *
     * @param context
     * @param responseBody 返回结果
     * @throws JSONException
     */
    public ResponseParser(Context context, ResponseBody responseBody) throws Exception {
        JSONObject responseJsonObject = new JSONObject(responseBody.string());
        JSONObject jsonObject = new JSONObject(AESUtil.aesDecrypt(responseJsonObject.getString("data"), AESUtil.AES_KEY));
        Logger.i("ResponseParser --> " + jsonObject.toString());
        try {
            if (jsonObject.has("rcode")) {
                state = jsonObject.getInt("rcode");
            } else {
                throw new RuntimeException("按照ResponseParser解析rcode失败，需要调整ResponseParser.");
            }
            if (jsonObject.has("msg")) {
                message = jsonObject.getString("msg");
            } else if (jsonObject.has("message")) {
                message = jsonObject.getString("message");
            } else {
                throw new RuntimeException("按照ResponseParser解析message失败，需要调整ResponseParser.");
            }
            if (jsonObject.has("form")) {
                data = jsonObject.getString("form");
            } else {
                throw new RuntimeException("按照ResponseParser解析form失败，需要调整ResponseParser.");
            }
        } catch (JSONException e) {
            throw new RuntimeException("按照ResponseParser解析失败，需要调整ResponseParser.");
        }

        if (state == STATE_AUTHENTICATION_FAILURE || state == STATE_USERID_OR_TOKEN_FAILURE) {
            // 身份验证失败，由于多个手机登录同一个账号导致，此时就跳转到登录界面
        } else if (state != STATE_SUCCESS) {
            // 其他失败
        }
    }
}
