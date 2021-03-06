package com.kaidongyuan.app.tyorder.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kaidongyuan.app.tyorder.bean.BillFee;
import com.kaidongyuan.app.tyorder.constants.URLCostant;
import com.kaidongyuan.app.tyorder.ui.activity.BillFeeListActivity;
import com.kaidongyuan.app.tyorder.util.ExceptionUtil;
import com.kaidongyuan.app.tyorder.util.HttpUtil;
import com.kaidongyuan.app.tyorder.util.NetworkUtil;
import com.kaidongyuan.app.tyorder.util.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${PEOJECT_NAME}
 * Created by Administrator on 2017/6/29.
 */
public class BillFeeListActivityBiz {

    private BillFeeListActivity mActivity;
    /**
     * 获取客户列表的网络请求标记
     */
    private final String mTagGetBillFeeList = "GetAppBillFeeList";
    /**
     * 存放从后台获取的所有客户数据集合
     */
    private List<BillFee> mTotalBillFeeList;
    /**
     * 从客户列表界面中所选择的客户id
     */
    private String PARTY_IDX;
    /**
     * 用户选中的账单月份
     */
    private BillFee mSelectedBillFee;
    /**
     * 默认获取数据是为第一页
     */
    private final int mInitPagerIndex = 1;
    /**
     * 分页加载时加载的页数
     */
    private int mPageIndex = mInitPagerIndex;
    /**
     * 分页加载时每页加载的数据数量
     */
    public final int mPageSize = 20;

    public BillFeeListActivityBiz(BillFeeListActivity activity,String PARTY_IDX) {
        try {
            this.mActivity = activity;
            this.PARTY_IDX=PARTY_IDX;
            this.mTotalBillFeeList = new ArrayList<>();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 初次加载，只加载第一页
     *
     * @return 发送请求是否成功
     */
    public boolean getinitBillFees() {
        try {
            mPageIndex = mInitPagerIndex;
            return GetAppBillFeeList();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 刷新已完成订单数据
     *
     * @return 发送网络请求是否成功
     */
    public boolean reFreshCompleteOrderData() {
        try {
            mPageIndex = mInitPagerIndex;
            return GetAppBillFeeList();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }

    /**
     * 加载跟多的已完成订单数据
     *
     * @return 发送网络请求是否成功
     */
    public boolean loadMoreCompleteOrderData() {
        try {
            return GetAppBillFeeList();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return false;
        }
    }



    /**
     * 获取已完成订单数据
     *
     * @return 发送请求是否成功
     */
    private boolean GetAppBillFeeList() {

        try {
            StringRequest request = new StringRequest(Request.Method.POST, URLCostant.GetAppBillFeeList, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Logger.w(BillFeeListActivityBiz.this.getClass() + ".GetAppBillFeeList:" + response);
                    GetAppBillFeeListSuccess(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Logger.w(BillFeeListActivityBiz.this.getClass() + ".GetAppBillFeeList:" + error.toString());
                    if (NetworkUtil.isNetworkAvailable()) {
                        mActivity.loginError("获取订单失败!");
                    } else {
                        mActivity.loginError("请检查网络是否正常连接！");
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("strPage", mPageIndex + "");
                    params.put("strPageCount", mPageSize + "");
                    params.put("strLicense", "");
                    return params;
                }
            };
            request.setTag(mTagGetBillFeeList);
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
     * 处理网络请求返回数据成功
     * @param response 返回的数据
     */
    private void GetAppBillFeeListSuccess(String response) {
        try {
            JSONObject object = JSON.parseObject(response);
            int type = object.getInteger("type");
            if (type == 1) {
                if (mPageIndex == mInitPagerIndex) {//刷新或初次加载，清除集合中的数据
                    mTotalBillFeeList.clear();
                }
                JSONObject jo=JSON.parseObject(object.getString("result"));
                List<BillFee> tmpBillFeeList = JSON.parseArray(jo.getString("AppBillFee"), BillFee.class);
                mTotalBillFeeList.addAll(tmpBillFeeList);
                mActivity.loginSuccess();
                mPageIndex++;
            } else {
                mActivity.loginError(object.getString("msg"));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            mActivity.loginError("获取订单失败!");
        }
    }

    /**
     * 取消请求
     */
    public void cancelRequest() {
        try {
            HttpUtil.cancelRequest(mTagGetBillFeeList);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 返回请求到的账单列表集合
     * @return
     */
    public List<BillFee> getmTotalBillFeeList() {
        return mTotalBillFeeList;
    }
}
