package com.like.alipay.sample;

import android.content.Context;

import com.like.retrofit.request.listener.OnResponseListener;

import okhttp3.ResponseBody;

/**
 * 用来通过ResponseParser统一解析数据，并进行错误处理。
 * 其它的不能用ResponseParser来解析的返回结果，请使用OnResponseListener<ResponseBody>
 *
 * @author like
 * @version 1.0
 * @created at 2017/3/18 20:39
 */
public abstract class MyOnResponseListener implements OnResponseListener<ResponseBody> {
    private Context mContext;

    public MyOnResponseListener(Context context) {
        mContext = context;
    }

    @Override
    public void onSuccess(ResponseBody responseBody) {
        try {
            // 这里解析会处理错误。
            ResponseParser responseParser = new ResponseParser(mContext, responseBody);
            onSuccess(responseParser.data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void onSuccess(String data);

    @Override
    public void onFailure(Throwable throwable) {

    }
}
