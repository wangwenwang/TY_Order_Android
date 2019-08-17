package com.kaidongyuan.app.tyorder.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.SlideDateTimeListener;
import com.example.mylibrary.SlideDateTimePicker;
import com.kaidongyuan.app.tyorder.R;
import com.kaidongyuan.app.tyorder.adapter.BusinessAdapter;
import com.kaidongyuan.app.tyorder.adapter.OrderProductDetailAdapter;
import com.kaidongyuan.app.tyorder.adapter.OrderPromotionAdapter;
import com.kaidongyuan.app.tyorder.adapter.OrderTmsWayAdapter;
import com.kaidongyuan.app.tyorder.adapter.TmsTypeAdapter;
import com.kaidongyuan.app.tyorder.app.MyApplication;
import com.kaidongyuan.app.tyorder.bean.Business;
import com.kaidongyuan.app.tyorder.bean.OrderGift;
import com.kaidongyuan.app.tyorder.bean.OrderTmsWay;
import com.kaidongyuan.app.tyorder.bean.Product;
import com.kaidongyuan.app.tyorder.bean.PromotionDetail;
import com.kaidongyuan.app.tyorder.bean.PromotionOrder;
import com.kaidongyuan.app.tyorder.bean.TmsType;
import com.kaidongyuan.app.tyorder.constants.BusinessConstants;
import com.kaidongyuan.app.tyorder.constants.EXTRAConstants;
import com.kaidongyuan.app.tyorder.constants.SharedPreferenceConstants;
import com.kaidongyuan.app.tyorder.model.OrderConfirmActivityBiz;
import com.kaidongyuan.app.tyorder.util.DensityUtil;
import com.kaidongyuan.app.tyorder.util.ExceptionUtil;
import com.kaidongyuan.app.tyorder.util.OrderUtil;
import com.kaidongyuan.app.tyorder.util.SharedPreferencesUtil;
import com.kaidongyuan.app.tyorder.util.ToastUtil;
import com.kaidongyuan.app.tyorder.util.logger.Logger;
import com.kaidongyuan.app.tyorder.widget.MyListView;
import com.kaidongyuan.app.tyorder.widget.loadingdialog.MyLoadingDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/6/4.
 * 订单确认界面
 */
public class OrderConfirmActivity extends BaseFragmentActivity implements View.OnClickListener {

    /**
     * 跳转到添加赠品界面时用到的常量
     */
    private final int ADDGIFT_REQUESCODE = 12;

    /**
     * 对应的业务类
     */
    private OrderConfirmActivityBiz mBiz;

    /**
     * 返回上一界面按钮
     */
    private ImageView mImageViewGoBack;
    /**
     * 用户选中的商品
     */
    private List<Product> mChoicedProducts;

    /**
     * 送货时间
     */
    private TextView mTextViewTime;
    /**
     * 产品总数
     */
    private TextView mTextViewTotalCount;
    /**
     * 原价
     */
    private TextView mTextViewOrgPrice;
    /**
     * 实际付款
     */
    private TextView mTextViewActPrice;
    /**
     * 促销信息
     */
    private TextView mTextViewPromotionRemark;
    /**
     * 促销减价
     */
    private TextView mTextViewPromotionPrice;
    /**
     * 支付方式
     */
    private TextView mTextViewPayPrice;
    /**
     *
     */
    private TextView mTextViewTmsType;
    /**
     *
     */
    private TextView mTextViewOrderTmsWay;
    /**
     * 填写部门
     */
    private EditText ed_REFERENCE01;
    /**
     * 无赠品时显示的提示框
     */
    private TextView mTextViewNoPromotion;
    /**
     * 产品列表
     */
    private MyListView mListViewProduct;
    /**
     * 产品列表适配器
     */
    private OrderProductDetailAdapter mProductAdapter;
    /**
     * 赠品列表
     */
    private MyListView mListViewPromotion;
    /**
     * 赠品列表适配器
     */
    private OrderPromotionAdapter mPromotionAdapter;
    /**
     * 备注信息输入框
     */
    private EditText mEditTextMark;
    /**
     * 网络请求时显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;
    /**
     * 添加赠品按钮
     */
    private Button mBtAddGift;
    /**
     * 用户选择的送货时间
     */
    private Date mDate;
    /**
     * 手动添加的赠品
     */
    private ArrayList<PromotionDetail> mGiftData;
    /**
     * 商品总数过度值
     */
    private double mTempTotalQTY = 0;
    /**
     * 添加赠品界面返回的赠品
     */
    private ArrayList<PromotionDetail> mReturnGiftData;
    /**
     * 赠品的品类集合
     */
    private List<OrderGift> mGiftCategoryData;
    /**
     * 发运方式类型 Dialog
     */
    private Dialog mTmsTypesDialog;
    /**
     * 发运方式类型 ListView
     */
    private ListView mListViewTmsTypes;
    private TmsTypeAdapter mTmsTypeAdapter;
    /**
     * 订单类型 Dialog
     */
    private Dialog mOrderWaysDialog;
    /**
     * 订单类型 ListView
     */
    private ListView mListViewOrderWays;
    private OrderTmsWayAdapter mOrderWaysAdapter;
    private List<OrderTmsWay> orderTmsWayList;
    private int orderTmsWayListIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_order_confirm);
            initData();
            setTop();
            initView();
            setListener();
            getPromotionData();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            mBiz.cancelRequest();
            mBiz = null;
            if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
            if (mTmsTypesDialog!=null && mTmsTypesDialog.isShowing()) {
                mTmsTypesDialog.dismiss();
            }
            mTmsTypesDialog = null;
            if (mOrderWaysDialog!=null && mOrderWaysDialog.isShowing()) {
                mOrderWaysDialog.dismiss();
            }
            mOrderWaysDialog = null;
            mLoadingDialog = null;
            mImageViewGoBack = null;
            mChoicedProducts = null;
            mTextViewTime = null;
            mTextViewTotalCount = null;
            mTextViewOrgPrice = null;
            mTextViewActPrice = null;
            mTextViewPromotionRemark = null;
            mTextViewPromotionPrice = null;
            mTextViewPayPrice = null;
            mTextViewTmsType=null;
            mTextViewOrderTmsWay = null;
            ed_REFERENCE01=null;
            mTextViewNoPromotion = null;
            mListViewProduct = null;
            mProductAdapter = null;
            mListViewPromotion = null;
            mPromotionAdapter = null;
            mEditTextMark = null;
            mBtAddGift = null;
            mDate = null;
            mGiftData = null;
            mReturnGiftData = null;
            mGiftCategoryData = null;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void initData() {
        try {
            mBiz = new OrderConfirmActivityBiz(this);
            Intent intent = getIntent();
            mChoicedProducts = intent.getParcelableArrayListExtra(EXTRAConstants.CHOICED_PRODUCT_LIST);
            orderTmsWayList = (MyApplication.getInstance().getOrderTmsWayList() == null) ? new ArrayList<OrderTmsWay>() : MyApplication.getInstance().getOrderTmsWayList();
            orderTmsWayListIndex = -1;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setTop () {
        //版本4.4以上设置状态栏透明，界面布满整个界面
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            View topView = findViewById(R.id.topview);
            ViewGroup.LayoutParams topParams = topView.getLayoutParams();
            topParams.height = DensityUtil.getStatusHeight()*16/30;
            topView.setLayoutParams(topParams);
        }
    }

    private void initView() {
        try {
            setTieltLayoutHeight();
            mImageViewGoBack = (ImageView) this.findViewById(R.id.button_goback);
            mTextViewTime = (TextView) this.findViewById(R.id.tv_time);
            mTextViewTotalCount = (TextView) this.findViewById(R.id.tv_total_count);
            mTextViewOrgPrice = (TextView) this.findViewById(R.id.tv_org_price);
            mTextViewActPrice = (TextView) this.findViewById(R.id.tv_act_price);
            mTextViewPromotionRemark = (TextView) this.findViewById(R.id.tv_promotion_remark);
            mTextViewPromotionPrice = (TextView) this.findViewById(R.id.tv_promotion_price);
            mTextViewPayPrice = (TextView) this.findViewById(R.id.tv_pay_price);
            mTextViewTmsType= (TextView) this.findViewById(R.id.tv_tms_type);
            mTextViewOrderTmsWay= (TextView) this.findViewById(R.id.tv_order_tms_way);
            ed_REFERENCE01= (EditText) this.findViewById(R.id.ed_REFERENCE01);
            mTextViewNoPromotion = (TextView) this.findViewById(R.id.tv_no_promotion);
            mListViewProduct = (MyListView) this.findViewById(R.id.lv_product);
            mProductAdapter = new OrderProductDetailAdapter(this, null);
            mListViewProduct.setAdapter(mProductAdapter);
            mListViewPromotion = (MyListView) this.findViewById(R.id.lv_promotion);
            mPromotionAdapter = new OrderPromotionAdapter(this, null);
            mListViewPromotion.setAdapter(mPromotionAdapter);
            mEditTextMark = (EditText) this.findViewById(R.id.et_mark);
            mBtAddGift = (Button) this.findViewById(R.id.bt_addgift);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 输入框在底部，所以要动态调整标题栏的高度
     * 设置标题栏高度，默认是50dp，根据百分百设置，统一标题栏高度
     */
    private void setTieltLayoutHeight() {
        try {
            String strHeight = getString(R.string.title_height);//百分百布局格式 如 1%h
            int index = strHeight.indexOf("%");
            if (index != -1) {
                int height = Integer.parseInt(strHeight.substring(0, index));
                int titleLayoutHeight = DensityUtil.getHeight() * height / 100;
                RelativeLayout relativeLayoutTitle = (RelativeLayout) findViewById(R.id.percentRL_title);
                ViewGroup.LayoutParams params = relativeLayoutTitle.getLayoutParams();
                params.height = titleLayoutHeight;
                relativeLayoutTitle.setLayoutParams(params);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            mImageViewGoBack.setOnClickListener(this);
            findViewById(R.id.btn_confirm).setOnClickListener(this);
            mBtAddGift.setOnClickListener(this);
            findViewById(R.id.rl_select_time).setOnClickListener(this);
            mProductAdapter.setInterface(new OrderProductDetailAdapter.OrderProductDetailAdapterModifyPriceInterface() {
                @Override
                public void raisePrice(int dataIndex) {//上调价格 0.1元
                    mBiz.raisePrice(dataIndex);
                    notifyDataChange();
                }

                @Override
                public void cutPrice(int dataIndex) {//下调价格 0.1元
                    mBiz.cutPrice(dataIndex);
                    notifyDataChange();
                }

                @Override
                public void setProductPrice(int dataIndex, double inputPirce) {//手动设置价格
                    mBiz.setPrice(dataIndex, inputPirce);
                    notifyDataChange();
                }
            });
            mTextViewOrderTmsWay.setOnClickListener(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 发送订单信息到后台，获取后台返回的订单信息
     */
    private void getPromotionData() {
        try {
            if (mBiz.getPromotionData(mBiz.getSubmitString(mChoicedProducts))&&mBiz.getDeliveryWay()) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求是显示的 Dialog
     */
    private void showLoadingDialog() {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = new MyLoadingDialog(this);
            }
            mLoadingDialog.showDialog();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.tv_tms_type:
                    showTmsTypesDialog();
                    break;
                case R.id.button_goback://返回上一界面
                    finish();
                    break;
                case R.id.btn_confirm://提交订单
                    confirm();
                    break;
                case R.id.bt_addgift://添加赠品
                    choiceGift();
                    break;
                case R.id.rl_select_time://选择送货时间
                    new SlideDateTimePicker.Builder((getSupportFragmentManager()))
                            .setListener(new DateHandler(0))
                            .setInitialDate(new Date())
                            .setMinDate(new Date())
                            .build()
                            .show();
                case R.id.tv_order_tms_way:
                    showOrderTmsWaysDialog();
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取促销信息失败时回调的方法
     *
     * @param msg 显示的信息
     */
    public void getDataError(String msg) {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom(String.valueOf(msg), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    public void getTmsTypesSuccess(){
        mTextViewTmsType.setOnClickListener(this);
        showTmsTypesDialog();
    }

    private void showTmsTypesDialog() {
        try {
           mBiz.mTmsTypes= mBiz.mTmsTypes == null ? new ArrayList<TmsType>() : mBiz.mTmsTypes;
            if (mTmsTypesDialog == null) {
                createTmsTypesDialog();
            } else {
                mTmsTypesDialog.show();
                mTmsTypeAdapter.notifyChange(mBiz.mTmsTypes);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 创建用户业务类型的 Dialog
     */
    private void createTmsTypesDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            mTmsTypesDialog = builder.create();
            mTmsTypesDialog.setCanceledOnTouchOutside(false);
            mTmsTypesDialog.show();
            Window window = mTmsTypesDialog.getWindow();
            window.setContentView(R.layout.dialog_tmstypes_choice);
            mListViewTmsTypes = (ListView) window.findViewById(R.id.listView_business);
            mListViewTmsTypes.setOnItemClickListener(new InnerOnItemClickListener());
            mTmsTypeAdapter = new TmsTypeAdapter(mBiz.mTmsTypes, this);
            mListViewTmsTypes.setAdapter(mTmsTypeAdapter);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void showOrderTmsWaysDialog() {
        try {
            if (mOrderWaysDialog == null) {
                createOrderTmsWaysDialog();
            } else {
                mOrderWaysDialog.show();
                mOrderWaysAdapter.notifyChange(orderTmsWayList);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 创建订单类型的 Dialog
     */
    private void createOrderTmsWaysDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            mOrderWaysDialog = builder.create();
            mOrderWaysDialog.setCanceledOnTouchOutside(false);
            mOrderWaysDialog.show();
            Window window = mOrderWaysDialog.getWindow();
            window.setContentView(R.layout.dialog_tmstypes_choice);
            mListViewOrderWays = (ListView) window.findViewById(R.id.listView_business);
            mListViewOrderWays.setOnItemClickListener(new InnerOnItemClickListener1());
            mOrderWaysAdapter = new OrderTmsWayAdapter(orderTmsWayList, this);
            mListViewOrderWays.setAdapter(mOrderWaysAdapter);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取促销信息成功时回调的方法
     */
    public void getPromotionDataSuccess() {
        try {
            mLoadingDialog.dismiss();
            PromotionOrder promotionOrder = mBiz.getOrder();
            if (promotionOrder != null) {
                setViewData();
                //设置添加赠品按钮是否可见
                String bussinessName = SharedPreferencesUtil.getValueByName(SharedPreferenceConstants.BUSSINESS_CODE, SharedPreferenceConstants.NAME, 0);
                if (bussinessName.contains(BusinessConstants.TYPE_KANGSHIFU)&& BusinessConstants.IS_HAVE_GIFT.equals(mBiz.getOrder().HAVE_GIFT)) {
                    mBtAddGift.setVisibility(View.VISIBLE);
                } else {
                    mBtAddGift.setVisibility(View.GONE);
                }
            } else {
                ToastUtil.showToastBottom("确认订单信息失败！", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 成功提交订单时回调的方法
     */
    public void confirmSuccess() {
        try {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom("提交订单成功！", Toast.LENGTH_SHORT);
            MyApplication.getInstance().finishActivity(MakeOrderActivity.class);
            this.finish();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 设置空间的信息
     */
    private void setViewData() {
        try {
            PromotionOrder order = mBiz.getOrder();
            List<PromotionDetail> products = mBiz.getProducts();
            for (PromotionDetail promotionDetail:products){
                for (Product product:mChoicedProducts){
                    if (product.getPRODUCT_NO()!=null&&promotionDetail.PRODUCT_NO!=null
                            &&product.getPRODUCT_NO().equals(promotionDetail.PRODUCT_NO)){
                        promotionDetail.stock_qty=product.getQTYAVAILABLE();
                    }
                }
            }
            mProductAdapter.notifyChange(products);
            List<PromotionDetail> promotions = mBiz.getPromotions();
            mPromotionAdapter.notifyChange(promotions);
            if (products == null || promotions.size() == 0) {
                mTextViewNoPromotion.setVisibility(View.VISIBLE);
                mListViewPromotion.setVisibility(View.GONE);
            } else {
                mTextViewNoPromotion.setVisibility(View.GONE);
                mListViewPromotion.setVisibility(View.VISIBLE);
            }
            mTextViewTotalCount.setText(String.valueOf(order.TOTAL_QTY));
            if (promotions.size() == 0) {
                mTextViewOrgPrice.setText("￥" + order.ORG_PRICE + "");
            } else {
                mTextViewOrgPrice.setText("￥" + (order.ORG_PRICE - mBiz.getPromotionPrice(promotions)));
            }
            // 不需要显示赠品价格
            mTextViewPayPrice.setText(String.valueOf(OrderUtil.getPaymentType(order.PAYMENT_TYPE)));
            mTextViewTmsType.setText("送货");
            if(orderTmsWayList.size() > 0) {
                mTextViewOrderTmsWay.setText(orderTmsWayList.get(0).getBUSINESS_IDX());
                orderTmsWayListIndex = 0;
            }
            if (order.MJ_REMARK == null || order.MJ_REMARK.equals("") || order.MJ_REMARK.equals("+|+")) {
                mTextViewPromotionRemark.setVisibility(View.GONE);
                this.findViewById(R.id.textview_promotion_remark_head).setVisibility(View.GONE);
                mTextViewPromotionPrice.setVisibility(View.GONE);
                this.findViewById(R.id.textview_promotion_price_head).setVisibility(View.GONE);
                mTextViewActPrice.setText("￥" + order.ACT_PRICE);
            } else {
                mTextViewPromotionRemark.setText(String.valueOf(OrderUtil.getPromotionRemark(order.MJ_REMARK, false)));
                mTextViewPromotionPrice.setText("- ￥" + order.MJ_PRICE + "");
                mTextViewActPrice.setText("￥" + (order.ACT_PRICE - order.MJ_PRICE));
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 选择送货时间的监听
     */
    class DateHandler extends SlideDateTimeListener {
        int which;

        DateHandler(int which) {
            this.which = which;
        }

        @Override
        public void onDateTimeCancel() {//不设置送货时间
            try {
                super.onDateTimeCancel();
                mDate = null;
                mTextViewTime.setText(null);
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }

        @Override
        public void onDateTimeSet(Date date) {//设置送货时间
            try {
                mDate = date;
                if (date != null) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    mTextViewTime.setText(df.format(date));
                }
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    /**
     * 确认订单 最后一步提交到服务器
     */
    private void confirm() {
        try {
            String remark = mEditTextMark.getText().toString().trim();
            String REFERENCE01=ed_REFERENCE01.getText().toString().trim();//20171020 填写部门
            String REFERENCE02=mTextViewTmsType.getText().toString().trim();//20171218 所选发运方式
            long REFERENCE03=Long.parseLong(orderTmsWayList.get(orderTmsWayListIndex).getIDX());//20190817 所选订单类型
            mBiz.setConfirmData(mReturnGiftData, mChoicedProducts, mTempTotalQTY, mDate, remark,REFERENCE01,REFERENCE02,REFERENCE03);
            if (mBiz.confirm()) {
                showLoadingDialog();
            } else {
                ToastUtil.showToastBottom(getString(R.string.sendrequest_fail), Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 跳转到选择赠品界面
     */
    private void choiceGift() {
        try {
            PromotionOrder order = mBiz.getOrder();
            if (mReturnGiftData!=null){
                order.OrderDetails.removeAll(mReturnGiftData);
            }
            mTempTotalQTY = 0;
            mReturnGiftData = null;
            mGiftCategoryData = order.GiftClasses;
            if (mGiftCategoryData != null && mGiftCategoryData.size() > 0) {
                if (mGiftData != null) {//将已选赠品集合清空
                    mGiftData = null;
                }
                Intent intent1 = new Intent(OrderConfirmActivity.this, AddGiftActivity.class);
                Intent intent = this.getIntent();
                if (intent.hasExtra(EXTRAConstants.ORDER_PARTY_ID)) {
                    String party_idx = intent.getStringExtra(EXTRAConstants.ORDER_PARTY_ID);
                    intent1.putExtra(AddGiftActivity.EXTRA_STR_PARTYID, party_idx);
                }
                if (intent.hasExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX)) {
                    String strAddressIDX = intent.getStringExtra(EXTRAConstants.ORDER_PARTY_ADDRESS_IDX);
                    intent1.putExtra(AddGiftActivity.EXTRA_STR_PARTY_ADDRESSID, strAddressIDX);
                }

                intent1.putExtra(AddGiftActivity.EXTRA_PROMOTION_START_LINE_NO, mBiz.getPromotionNumber());
                intent1.putParcelableArrayListExtra(AddGiftActivity.EXTRA_GIFT_DATA, (ArrayList<? extends Parcelable>) mGiftCategoryData);

                if (BusinessConstants.IS_HAVE_GIFT.equals(order.HAVE_GIFT) && order.OrderDetails == null) {
                    order.OrderDetails = new ArrayList<>();
                }
                intent1.putParcelableArrayListExtra(AddGiftActivity.EXTRA_ORDER_DETAIL, (ArrayList<? extends Parcelable>) order.OrderDetails);
                startActivityForResult(intent1, ADDGIFT_REQUESCODE);
                mBiz.setmOrder(order);
            } else {
                ToastUtil.showToastBottom("无赠品可添加！", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == ADDGIFT_REQUESCODE) {
                if (mGiftData == null) {
                    mGiftData = new ArrayList<>();
                }
                List<PromotionDetail> promotions = mBiz.getPromotions();
                if (promotions != null && promotions.size() > 0) {
                    mGiftData.addAll(promotions);
                }
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(OrderConfirmActivity.this, "添加赠品成功", Toast.LENGTH_SHORT).show();
                    if (data.hasExtra(AddGiftActivity.EXTRA_RETURN_GIFT_DATA)) {
                        mReturnGiftData = data.getParcelableArrayListExtra(AddGiftActivity.EXTRA_RETURN_GIFT_DATA);
                        if (mReturnGiftData != null && mReturnGiftData.size() > 0) {
                            mGiftData.addAll(mReturnGiftData);
                        }
                        Logger.w("OrderConfirmActivity.选择的赠品：" + mGiftData.toString());
                    }
                }
                mPromotionAdapter.notifyChange(mGiftData);
                mTempTotalQTY = mBiz.getOrder().TOTAL_QTY;
                if (mReturnGiftData != null && mReturnGiftData.size() > 0) {
                    for (PromotionDetail promotionDetail : mReturnGiftData) {
                        mTempTotalQTY += promotionDetail.PO_QTY;
                    }
                }
                mTextViewTotalCount.setText(String.valueOf(mTempTotalQTY));
                if (mGiftData.size() > 0) {
                    mTextViewNoPromotion.setVisibility(View.GONE);
                    mListViewPromotion.setVisibility(View.VISIBLE);
                    mBtAddGift.setText("重新添加");
                } else {
                    mTextViewNoPromotion.setVisibility(View.VISIBLE);
                    mListViewPromotion.setVisibility(View.GONE);
                    mBtAddGift.setText("重新添加");
                }
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 产品数据变动是刷新 listView
     */
    private void notifyDataChange() {
        try {
            mProductAdapter.notifyChange(mBiz.getProducts());
            mPromotionAdapter.notifyChange(mBiz.getPromotions());
            mTextViewActPrice.setText(mBiz.getActPrice());
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 用户业务列表 Item 点击监听实现类
     */
    private class InnerOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mTmsTypesDialog.dismiss();
                mTextViewTmsType.setText(mBiz.mTmsTypes.get(position).getItem_name());
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }

    /**
     * 用户业务列表 Item 点击监听实现类
     */
    private class InnerOnItemClickListener1 implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                mOrderWaysDialog.dismiss();
                mTextViewOrderTmsWay.setText(orderTmsWayList.get(position).getBUSINESS_IDX());
                orderTmsWayListIndex = position;
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }
    }
}
