package com.kaidongyuan.app.tyorder.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.tyorder.R;
import com.kaidongyuan.app.tyorder.app.MyApplication;
import com.kaidongyuan.app.tyorder.bean.Order;
import com.kaidongyuan.app.tyorder.constants.URLCostant;
import com.kaidongyuan.app.tyorder.ui.activity.OrderDetailsActivity;
import com.kaidongyuan.app.tyorder.util.ExceptionUtil;
import com.kaidongyuan.app.tyorder.util.HttpUtil;
import com.kaidongyuan.app.tyorder.util.NetworkUtil;
import com.kaidongyuan.app.tyorder.util.logger.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/25.
 * 订单详情界面
 */
public class OrderDetailsActivityBiz {

    private OrderDetailsActivity mActivity;

    /**
     * 网络请求订单详情数据的标记
     */
    private final String mTagGetOrderDetailsData = "mTagGetOrderDetailsData",mTagCancelOrder="mTagCancelOrder";
    /**
     * 网络订单审核的标记
     */
    private final String mUpdateAudit="UpdateAudit";
    /**
     * 网络订单反审核的标记
     */
    private final String mReturnAudit="ReturnAudit";
    /**
     * 网络请求
     */
    private final String mTagChangeOrderProducts="changeOrderProducts";
    /**
     * 订单详情
     */
    private Order mOrder;

    public OrderDetailsActivityBiz(OrderDetailsActivity activity) {
        this.mActivity = activity;
    }

    public boolean cancelOrder(final Long orderid){
        try {
            StringRequest request=new StringRequest(Request.Method.POST, URLCostant.OrderCancel, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "getOrderDetailsDataSuccess:" + response);
                    cancelOrderSuccess(response);
                }
            },new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "getOrderDetailsDataError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getOrderDetailsDataError("取消订单失败！");
                    } else {
                        mActivity.getOrderDetailsDataError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strOrderIdx", orderid+"");//strOrderId , 13464
                    params.put("strUserIdx",MyApplication.getInstance().getUser().getIDX());
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagCancelOrder);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HttpUtil.getRequestQueue().add(request);
            return true;
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
    }

    private void cancelOrderSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type==1){
                mActivity.cancelOrderSuccess();
            } else {
                mActivity.getOrderDetailsDataError(object.getString("msg"));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 网络请求订单详情数据
     *
     * @param orderId 订单编号
     * @return 发送请求是否成功
     */
    public boolean getOrderDetailsData(final Long orderId) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GET_ORDER_DETAIL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "getOrderDetailsDataSuccess:" + response);
                    getOrderDetailsDataSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "getOrderDetailsDataError:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.getOrderDetailsDataError("获取订单详情失败！");
                    } else {
                        mActivity.getOrderDetailsDataError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strOrderId", orderId+"");//strOrderId , 13464
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetOrderDetailsData);
            request.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            HttpUtil.getRequestQueue().add(request);
            return true;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 网络请求订单详情数据成功返回信息
     *
     * @param response 返回的信息
     */
    private void getOrderDetailsDataSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                JSONArray ja = JSON.parseArray(object.getString("result"));
                if (ja.size() > 0) {
                    object = JSON.parseObject(ja.get(0).toString());
                    mOrder = JSON.parseObject(object.getString("order"), Order.class);
                    mActivity.getOrderDetailsDataSuccess();
                } else {
                    mActivity.getOrderDetailsDataError("获取订单详情数据失败,返回数据为空！");
                }
            } else {
                mActivity.getOrderDetailsDataError(object.getString("msg"));
            }
        } catch (Exception e) {
            mActivity.getOrderDetailsDataError(e.getMessage()+"订单详情数据异常");
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 订单审核接口
     * @return
     */
    public boolean orderUpdateAudit(){
        try {
            StringRequest request=new StringRequest(Request.Method.POST, URLCostant.UpdateAudit, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "orderUpdateAuditSuccess:" + response);
                    orderUpdateAuditSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "orderUpdateAuditError:" + volleyError.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.orderUpdateAuditError("审核提交失败！");
                    } else {
                        mActivity.orderUpdateAuditError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strOrderIdx", mOrder.getIDX()+"");//strOrderId , 13464
                    params.put("strUserName",MyApplication.getInstance().getUser().getUSER_NAME());
                    params.put("strLicense", "");
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
    public boolean orderReturnAudit(final String strReason){
        try {
            StringRequest request=new StringRequest(Request.Method.POST, URLCostant.RuturnAudit, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "orderReturnAuditSuccess:" + response);
                    orderReturnAuditSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "orderUpdateAuditError:" + volleyError.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.orderReturnAuditError("审核提交失败！");
                    } else {
                        mActivity.orderReturnAuditError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strOrderIdx", mOrder.getIDX()+"");//strOrderId , 13464
                    params.put("strUserName",MyApplication.getInstance().getUser().getUSER_NAME());
                    params.put("strReason",strReason);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mReturnAudit);
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

    /**
     * 20171218 修改未审核订单的备注信息
     * @param strRemake
     * @return
     */
    public boolean orderRemakeChange(final String strRemake){
        try {
            StringRequest request=new StringRequest(Request.Method.POST, URLCostant.UpdateRemark, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "orderRemakeChangeSuccess:" + response);
                    orderRemakeChangeSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "orderRemakeChangeError:" + volleyError.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.orderRemakeChangeError("修改备注失败！");
                    } else {
                        mActivity.orderRemakeChangeError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strIdx", mOrder.getIDX()+"");
                    params.put("strRemark",strRemake);
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mReturnAudit);
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

    public void orderRemakeChangeSuccess(String response){
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type==1){
                mActivity.changeOrderRemakeSuccess();
            }else {
                mActivity.orderRemakeChangeError(object.getString("msg"));
            }
        }catch (Exception e){
            ExceptionUtil.handlerException(e);
            mActivity.orderRemakeChangeError("修改备注异常，"+e.getMessage());
        }
    }

    private void orderUpdateAuditSuccess(String response){
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type==1){
                mActivity.orderUpdateAuditSuccess();
            }else {
                mActivity.orderUpdateAuditError(object.getString("msg"));
            }
        }catch (Exception e){
            ExceptionUtil.handlerException(e);
            mActivity.orderUpdateAuditError("审核提交异常，"+e.getMessage());
        }
    }

    public void orderReturnAuditSuccess(String response){
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type==1){
                mActivity.orderReturnAuditSuccess();
            }else {
                mActivity.orderReturnAuditError(object.getString("msg"));
            }
        }catch (Exception e){
            ExceptionUtil.handlerException(e);
            mActivity.orderReturnAuditError("订单退回异常，"+e.getMessage());
        }
    }

    /**
     * 对新建未审核订单进行审核时修改订单内下单产品数量的方法
     * @param idx  OrderDetailActivity中各个货物详情的OD_IDX
     * @param qty  修改后产品的下单数量
     * @return
     */
    public boolean changeOrderProducts(final String idx, final float qty){
        try {
            StringRequest request=new StringRequest(Request.Method.POST, URLCostant.SetProductQty, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "changeOrderProductsSuccess:" + response);
                    changeOrderProductSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Logger.w(OrderDetailsActivityBiz.this.getClass() + "changeOrderProductsError:" + volleyError.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.changeOrderProductsError("修改产品下单数量失败！");
                    } else {
                        mActivity.changeOrderProductsError(MyApplication.getmRes().getString(R.string.please_check_net));
                    }
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strOrderIdx", idx+"");//OrderDetail中各个货物详情的OD_IDX
                  //  params.put("idx",idx);
                    params.put("strQty",qty+"");
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagChangeOrderProducts);
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
    private void changeOrderProductSuccess(String response){
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type==1){
                mActivity.changeOrderProductsSuccess();
            }else {
                mActivity.changeOrderProductsError(object.getString("msg"));
            }
        }catch (Exception e){
            ExceptionUtil.handlerException(e);
            mActivity.changeOrderProductsError("修改产品下单数量异常，"+e.getMessage());
        }
    }

    /**
     * 取消网络请求
     */
    public void cancelRequest() {
        HttpUtil.cancelRequest(mTagGetOrderDetailsData,mUpdateAudit,mReturnAudit);
    }

    /**
     * 获取订单详情
     *
     * @return 订单详情
     */
    public Order getOrderDetails() {
        return mOrder;
    }

}
