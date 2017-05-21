package com.like.alipay.sample;

import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;

import com.like.alipay.AliPayUtils;
import com.like.commonlib.base.context.BaseActivity;
import com.like.commonlib.base.entity.Host;
import com.like.commonlib.base.viewmodel.BaseViewModel;
import com.like.commonlib.retrofit.BaseInterceptor;
import com.like.commonlib.retrofit.RequestConfig;
import com.like.commonlib.retrofit.RetrofitUtils;
import com.like.commonlib.retrofit.security.AESUtil;
import com.like.commonlib.util.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {
    public RetrofitUtils mRetrofitUtils;

    @Override
    protected BaseViewModel getViewModel() {
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        initRetrofit();
        if (RetrofitUtils.initialized()) {
            this.mRetrofitUtils = new RetrofitUtils(new Host(this));
        }
        return null;
    }

    public void alipay(View view) {
        Map<String, Object> params = new HashMap<>();
        params.put("payway", 0);// 0：支付宝，1：微信
        params.put("username", "13399857800");
        params.put("amount", "0.01");// 充值金额
        mRetrofitUtils.request("rechargePersonCenter", new MyOnResponseListener(this) {
            @Override
            public void onSuccess(String data) {
                try {
                    String payInfo = new JSONObject(data).getString("payInfo");
                    AliPayUtils.getInstance(MainActivity.this).pay(payInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, params);
    }

    private void initRetrofit() {
        SPUtils spUtils = SPUtils.getInstance(this);
        String ip = spUtils.getIp();
        int port = spUtils.getPort();
        if (TextUtils.isEmpty(ip)) {
            ip = "218.21.73.24";
            port = 81;
            spUtils.putIp(ip);
            spUtils.putPort(port);
        }
        RetrofitUtils.init(new RequestConfig(ip, Api.class).setPort(port)
                .setInterceptor(new BaseInterceptor() {
                    @Override
                    protected HashMap<String, Object> getPublicParamsMap() {
                        HashMap<String, Object> publicParamsMap = new HashMap<>();
                        publicParamsMap.put("device", "0");
                        publicParamsMap.put("timestamp", System.currentTimeMillis() + "");
                        publicParamsMap.put("userId", "122828c90382164e772a52a3653b8dcd");
                        publicParamsMap.put("token", "zLo3wA4aRqVUd+IGJsUP6MNgN4HI30z+GpUgD8ZxVl4=");
                        return publicParamsMap;
                    }

                    @Override
                    protected HashMap<String, String> getHeaderMap() {
                        HashMap<String, String> headerMap = new HashMap<>();
                        headerMap.put("Cookie", "JSESSIONID=94b11151-0169-4454-8983-99b9350aff76");
                        return headerMap;
                    }

                    @Override
                    protected String handlePostContent(String content) {
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("data", AESUtil.aesEncrypt(content, AESUtil.AES_KEY));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return obj.toString();
                    }
                }));
    }
}
