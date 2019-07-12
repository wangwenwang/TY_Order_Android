package com.kaidongyuan.app.tyorder.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.tyorder.R;
import com.kaidongyuan.app.tyorder.app.MyApplication;
import com.kaidongyuan.app.tyorder.constants.URLCostant;
import com.kaidongyuan.app.tyorder.ui.fragment.CheckOrderFragment;
import com.kaidongyuan.app.tyorder.util.ExceptionUtil;
import com.kaidongyuan.app.tyorder.util.HttpUtil;
import com.kaidongyuan.app.tyorder.util.NetworkUtil;
import com.kaidongyuan.app.tyorder.util.logger.Logger;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/4/24.
 */
public class CheckOrderFragmentsBiz {
    private CheckOrderFragment checkOrderFragment;
    /**
     * 网络订单审核的标记
     */
    private final String mUpdateAudit="UpdateAudit";

    public CheckOrderFragmentsBiz(CheckOrderFragment checkOrderFragment) {
        this.checkOrderFragment = checkOrderFragment;
    }

    /**
     * 订单审核
     * @param orderid
     * @return
     */
    public boolean orderUpdateAudit(final Long orderid, final boolean islast){
        try {

            StringRequest request=new StringRequest(Request.Method.POST, URLCostant.UpdateAudit, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CheckOrderFragmentsBiz.this.getClass() +String.valueOf(orderid)+ "orderUpdateAuditSuccess:" + response);
                    orderUpdateAuditSuccess(response,islast);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Logger.w(CheckOrderFragmentsBiz.this.getClass() +String.valueOf(orderid)+ "orderUpdateAuditError:" + volleyError.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        checkOrderFragment.orderUpdateAuditError("订单批量提交失败，正刷新订单列表数据");
                    } else {
                        checkOrderFragment.orderUpdateAuditError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strOrderIdx", orderid+"");//strOrderId , 13464
                    params.put("strUserName",MyApplication.getInstance().getUser().getUSER_NAME());
                    params.put("strLicense", "");
                    Logger.w(CheckOrderFragmentsBiz.this.getClass() +"提交审核orderUpdate："+params.get("strOrderIdx")+ "|" + params.get("strUserName"));
                    return params;
                }

            };
            request.setTag(mUpdateAudit);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HttpUtil.getRequestQueue().add(request);

            return true;
        }catch (Exception ex){
            ExceptionUtil.handlerException(ex);
            return false;
        }
    }
    public boolean orderlistUpdate(final List<Long> orderids){
        try {
            StringRequest request=new StringRequest(Request.Method.POST, URLCostant.UpdateAudit, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(CheckOrderFragmentsBiz.this.getClass() + String.valueOf(orderids.get(0)) + "orderUpdateAuditSuccess:" + response);
                    if (orderids.size() <= 1) {
                        orderUpdateAuditSuccess(response, true);
                    } else {
                        JSONObject object = JSON.parseObject(response);
                        int type = object.getInteger("type");
                        if (type==1) {
                            orderids.remove(0);
                            orderlistUpdate(orderids);
                        }else {
                            orderUpdateAuditSuccess(response,true);
                        }
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Logger.w(CheckOrderFragmentsBiz.this.getClass() +String.valueOf(orderids.get(0))+ "orderUpdateAuditError:" + volleyError.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        checkOrderFragment.orderUpdateAuditError("订单批量提交失败，正刷新订单列表数据");
                    } else {
                        checkOrderFragment.orderUpdateAuditError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strOrderIdx", orderids.get(0)+"");//strOrderId , 13464
                    params.put("strUserName",MyApplication.getInstance().getUser().getUSER_NAME());
                    params.put("strLicense", "");
                    Logger.w(CheckOrderFragmentsBiz.this.getClass() +"提交审核orderUpdate："+params.get("strOrderIdx")+ "|" + params.get("strUserName"));
                    return params;
                }
            };
        request.setTag(mUpdateAudit);
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        HttpUtil.getRequestQueue().add(request);
            return true;
        }catch (Exception e){
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    private void orderUpdateAuditSuccess(String response,boolean islast){
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type==1){
                checkOrderFragment.orderUpdateAuditSuccess(islast);
            }else {
                checkOrderFragment.orderUpdateAuditError("批量提交异常，"+object.getString("msg"));
            }
        }catch (Exception e){
            ExceptionUtil.handlerException(e);
            checkOrderFragment.orderUpdateAuditError("批量提交异常，正刷新订单列表数据"+e.getMessage());
        }
    }
}
