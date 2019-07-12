package com.kaidongyuan.app.tyorder.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kaidongyuan.app.tyorder.R;
import com.kaidongyuan.app.tyorder.adapter.CheckOrderFragmentViewpagerAdapter;
import com.kaidongyuan.app.tyorder.app.MyApplication;
import com.kaidongyuan.app.tyorder.model.CheckOrderFragmentsBiz;
import com.kaidongyuan.app.tyorder.ui.pagerinviewpage.CheckOrderFragmentAduitOrderPage;
import com.kaidongyuan.app.tyorder.ui.pagerinviewpage.CheckOrderFragmentCancelOrderPager;
import com.kaidongyuan.app.tyorder.ui.pagerinviewpage.CheckOrderFragmentCompleteOrderPage;
import com.kaidongyuan.app.tyorder.ui.pagerinviewpage.CheckOrderFragmentInTransOrderPage;
import com.kaidongyuan.app.tyorder.ui.pagerinviewpage.CheckOrderFragmentPermittedOrderPage;
import com.kaidongyuan.app.tyorder.ui.pagerinviewpage.CheckOrderFragmentUnAduitOrderPage;
import com.kaidongyuan.app.tyorder.ui.pagerinviewpage.PagerNecessaryInterface;
import com.kaidongyuan.app.tyorder.util.ExceptionUtil;
import com.kaidongyuan.app.tyorder.util.ToastUtil;
import com.kaidongyuan.app.tyorder.widget.loadingdialog.MyLoadingDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/4/1.
 * 主页 Fragment
 */
public class CheckOrderFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private View mParentView;
    private TextView tv_menue_auditOrder;//批量审核/批量释放的textview
    /**
     * ViewPager 显示订单集合
     */
    private ViewPager mViewpager;
    /**
     * 显示订单集合 Viewpager 适配器
     */
    private CheckOrderFragmentViewpagerAdapter mViewPagerAdapter;
    /**
     * 未审核订单按钮
     */
    private Button mButtonUnAduitOrder;
    /**
     * 已审核订单按钮
     */
    private Button mButtonAduitOrder;
    /**
     * 已释放订单按钮
     */
    private Button mButtonPermitOrder;
    /**
     * 在途订单按钮
     */
    private Button mButtonInTrainOrder;
    /**
     * 已完成订单按钮
     */
    private Button mButtonCompleteOrder;
    /**
     * 已取消订单按钮
     */
    private Button mButtonCancelOrder;

    /**
     * 点击切换按钮前现实的 Pager Index
     */
    private int mPreviousIndex;
    /**
     * 当前需要显示的 Pager Index
     */
    private int mCurrentIndex;
    /**
     * 存放切换按钮的数组
     */
    private Button[] mButtonsArr;
    /**
     * 存放 Pager 的数组
     */
    private PagerNecessaryInterface[] mPagersArr;

    /**
     * 未审核订单界面角标
     */
    private final int mOrderUnAduitOrderPagerIndex = 0;
    /**
     * 已审核订单界面角标
     */
    private final int mOrderAduitOrderPagerIndex = 1;
//    /**
//     * 已释放订单界面角标
//     */
//    private final int mOrderPermittedOrderPagerIndex=2;
    /**
     * 在途订单界面角标
     */
    private final int mOrderInTrainsOrderPagerIndex=2;
    /**
     * 已完成订单界面角标
     */
    private final int mOrderCompleteOrderPageIndex=3;
    /**
     * 已取消订单界面角标
     */
    private final int mOrderCancelOrderPagerIndex = 4;
    /**
     * 网络请求是显示的 Dialog
     */
    private MyLoadingDialog mLoadingDialog;

    private CheckOrderFragmentsBiz mBiz;
    private CheckOrderFragmentUnAduitOrderPage pagerOne;
    private CheckOrderFragmentAduitOrderPage pagerTwo;
//    private CheckOrderFragmentPermittedOrderPage pagerThree;
    private CheckOrderFragmentInTransOrderPage pagerFour;
    private CheckOrderFragmentCompleteOrderPage pagerFive;
    private CheckOrderFragmentCancelOrderPager pagerSix;
    private String usertype;
    private ArrayList<View> viewList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            mParentView = inflater.inflate(R.layout.fragment_checkorder, null);
            initData();
            initView();
            setListener();
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
        return mParentView;
    }

    @Override
    public void onDestroyView() {//执行每个 Pager 的销毁方法，取消请求，取消 显示的 Dialog
        super.onDestroyView();
        PagerNecessaryInterface pager;
        for (int i=mPagersArr.length-1; i>=0; i--) {
            pager = mPagersArr[i];
            if (pager!=null) {
                pager.onDestroy();
            }
            mViewpager = null;
            mViewPagerAdapter = null;
            mContext = null;
            mParentView = null;
            mButtonsArr = null;
        }
    }

    private void initData() {
        try {
            this.mContext = this.getActivity();
            this.mPreviousIndex = -1;
            this.mCurrentIndex = mOrderUnAduitOrderPagerIndex;
            this.mButtonsArr = new Button[6];
            this.mPagersArr = new PagerNecessaryInterface[6];
            this.mBiz=new CheckOrderFragmentsBiz(this);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 2017.05.05 根据用户角色选择是否显示已审核列表的 boolean值
     * BUSINESS业务员,PARTY客户,ORDER订单员,MANAGER经理,ADMIN管理员
     */
    private boolean isaduitbyusetype(){
        switch (MyApplication.getInstance().getUser().getUSER_TYPE().trim()){
            case "PARTY":
                return false;
            case "BUSINESS":
                return false;
            case "ORDER":
                return true;
            case "MANAGER":
                return true;
            case "ADMIN":
                return true;
            case "ALL":
                return true;
            default:
                return false;
        }
    }
    private void initView() {
        try {
            this.mViewpager = (ViewPager) mParentView.findViewById(R.id.viewpager_order);
            this.mViewPagerAdapter = new CheckOrderFragmentViewpagerAdapter(getViews());
            this.mViewpager.setAdapter(mViewPagerAdapter);
            this.mViewpager.setOnPageChangeListener(new InnerPagerChangeListener());
            this.mButtonUnAduitOrder = (Button) mParentView.findViewById(R.id.button_unaduitOrder);
            this.mButtonsArr[mOrderUnAduitOrderPagerIndex] = mButtonUnAduitOrder;
            this.mButtonAduitOrder = (Button) mParentView.findViewById(R.id.button_aduitOrder);
            this.mButtonsArr[mOrderAduitOrderPagerIndex] = mButtonAduitOrder;
//           if (isaduitbyusetype()){
//               this.mButtonAduitOrder.setVisibility(View.VISIBLE);
//               this.mButtonsArr[mOrderAduitOrderPagerIndex] = mButtonAduitOrder;
//           }else {
//               this.mButtonAduitOrder.setVisibility(View.GONE);
//           }
//            this.mButtonPermitOrder= (Button) mParentView.findViewById(R.id.button_permitOrder);
//            this.mButtonsArr[mOrderPermittedOrderPagerIndex]=mButtonPermitOrder;
            this.mButtonInTrainOrder= (Button) mParentView.findViewById(R.id.button_inTransOrder);
            this.mButtonsArr[mOrderInTrainsOrderPagerIndex]=mButtonInTrainOrder;
            this.mButtonCompleteOrder= (Button) mParentView.findViewById(R.id.button_completeOrder);
            this.mButtonsArr[mOrderCompleteOrderPageIndex]=mButtonCompleteOrder;
            this.mButtonCancelOrder = (Button) mParentView.findViewById(R.id.button_cancelOrder);
            this.mButtonsArr[mOrderCancelOrderPagerIndex] = mButtonCancelOrder;
            mViewpager.setCurrentItem(mOrderUnAduitOrderPagerIndex);
            tv_menue_auditOrder= (TextView) mParentView.findViewById(R.id.tv_menue_auditOrder);

            setCurrentPager(mOrderUnAduitOrderPagerIndex);
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    private void setListener() {
        try {
            this.mButtonUnAduitOrder.setOnClickListener(this);
            this.mButtonAduitOrder.setOnClickListener(this);
           // this.mButtonPermitOrder.setOnClickListener(this);
            this.mButtonInTrainOrder.setOnClickListener(this);
            this.mButtonCompleteOrder.setOnClickListener(this);
            this.mButtonCancelOrder.setOnClickListener(this);
            usertype = MyApplication.getInstance().getUser().getUSER_TYPE();
            if (usertype !=null&&usertype.equals("MANAGER")){//批量审核
                tv_menue_auditOrder.setVisibility(View.VISIBLE);
                tv_menue_auditOrder.setOnClickListener(this);
            }else if (usertype !=null&&usertype.equals("ADMIN")){//批量释放
                tv_menue_auditOrder.setVisibility(View.VISIBLE);
                tv_menue_auditOrder.setText(getString(R.string.menue_permitOrder));
                tv_menue_auditOrder.setOnClickListener(this);
            }else if (usertype!=null&&usertype.equals("ALL")){
                tv_menue_auditOrder.setVisibility(View.VISIBLE);
                tv_menue_auditOrder.setOnClickListener(this);
            } else {
                tv_menue_auditOrder.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 获取 ViewPager 的各个页面
     * @return 页面集合
     */
    private List<View> getViews() {
        try {
            viewList = new ArrayList<>();
            pagerOne = new CheckOrderFragmentUnAduitOrderPage(mContext);
            viewList.add(mOrderUnAduitOrderPagerIndex, pagerOne.getSelf());
            mPagersArr[mOrderUnAduitOrderPagerIndex] = pagerOne;
//           if (isaduitbyusetype()){
//               pagerTwo = new CheckOrderFragmentAduitOrderPage(mContext);
//               viewList.add(mOrderAduitOrderPagerIndex, pagerTwo.getSelf());
//               mPagersArr[mOrderAduitOrderPagerIndex] = pagerTwo;
//           }
            pagerTwo = new CheckOrderFragmentAduitOrderPage(mContext);
            viewList.add(mOrderAduitOrderPagerIndex, pagerTwo.getSelf());
            mPagersArr[mOrderAduitOrderPagerIndex] = pagerTwo;
//            pagerThree=new CheckOrderFragmentPermittedOrderPage(mContext);
//            viewList.add(mOrderPermittedOrderPagerIndex,pagerThree.getSelf());
//            mPagersArr[mOrderPermittedOrderPagerIndex]=pagerThree;
            pagerFour=new CheckOrderFragmentInTransOrderPage(mContext);
            viewList.add(mOrderInTrainsOrderPagerIndex,pagerFour.getSelf());
            mPagersArr[mOrderInTrainsOrderPagerIndex]=pagerFour;
            pagerFive=new CheckOrderFragmentCompleteOrderPage(mContext);
            viewList.add(mOrderCompleteOrderPageIndex,pagerFive.getSelf());
            mPagersArr[mOrderCompleteOrderPageIndex]=pagerFive;
            pagerSix = new CheckOrderFragmentCancelOrderPager(mContext);
            viewList.add(mOrderCancelOrderPagerIndex, pagerSix.getSelf());
            mPagersArr[mOrderCancelOrderPagerIndex] = pagerSix;
            return viewList;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.button_unaduitOrder://显示未审核订单
                    setCurrentPager(mOrderUnAduitOrderPagerIndex);
                    break;
                case R.id.button_aduitOrder://显示已审核订单
                    setCurrentPager(mOrderAduitOrderPagerIndex);
                    break;
//                case R.id.button_permitOrder://显示已释放订单
//                    setCurrentPager(mOrderPermittedOrderPagerIndex);
//                    break;
                case R.id.button_inTransOrder:
                    setCurrentPager(mOrderInTrainsOrderPagerIndex);
                    break;
                case R.id.button_completeOrder:
                    setCurrentPager(mOrderCompleteOrderPageIndex);
                    break;
                case R.id.button_cancelOrder://显示已取消订单
                    setCurrentPager(mOrderCancelOrderPagerIndex);
                    break;
                case R.id.tv_menue_auditOrder:
                    if (mViewpager!=null&&mViewpager.getCurrentItem()==mOrderUnAduitOrderPagerIndex){
                        if (pagerOne.getmOrderListApapter().getCheckedOrderIDXs().size()>0){
                            ToastUtil.showToastBottom("正批审提交所选的"+pagerOne.getmOrderListApapter().getCheckedOrderIDXs().size()+"张订单", Toast.LENGTH_LONG);
                            if (mBiz.orderlistUpdate(pagerOne.getmOrderListApapter().getCheckedOrderIDXs())){
                                showLoadingDialog();
                            }else {
                                ToastUtil.showToastBottom("发送网络请求失败！", Toast.LENGTH_SHORT);
                                    refreshCurrentPager(mCurrentIndex);
                            }
//
//                            for (int i=0;i<pagerOne.getmOrderListApapter().getCheckedOrderIDXs().size();i++){
//                            try {
//                               // Thread.currentThread().sleep(3000);
//                                if (i<(pagerOne.getmOrderListApapter().getCheckedOrderIDXs().size()-1)&&mBiz.orderUpdateAudit(pagerOne.getmOrderListApapter().getCheckedOrderIDXs().get(i),false)){
//                                    showLoadingDialog();
//                                }else if (mBiz.orderUpdateAudit(pagerOne.getmOrderListApapter().getCheckedOrderIDXs().get(i),true)){
//                                    showLoadingDialog();
//                                } else {
//                                    ToastUtil.showToastBottom("发送网络请求失败！", Toast.LENGTH_SHORT);
//                                    refreshCurrentPager(mCurrentIndex);
//                                    return;
//                                }
//                            }catch (Exception e){
//                                ExceptionUtil.handlerException(e);
//                            }
//
//                            }
//                            pagerOne.getmOrderListApapter().getCheckedOrderIDXs().clear();
//                            refreshCurrentPager(mCurrentIndex);
                            return;
                        }else {
                            ToastUtil.showToastBottom("请先勾选需批审订单", Toast.LENGTH_SHORT);
                        }
                    }else if (mViewpager!=null &&mViewpager.getCurrentItem()==mOrderAduitOrderPagerIndex){
                        if (pagerTwo.getmOrderListApapter().getCheckedOrderIDXs().size()>0){
                            ToastUtil.showToastBottom("正批释提交所选的"+pagerTwo.getmOrderListApapter().getCheckedOrderIDXs().size()+"张订单", Toast.LENGTH_LONG);
                            if (mBiz.orderlistUpdate(pagerTwo.getmOrderListApapter().getCheckedOrderIDXs())){
                                showLoadingDialog();
                            }else {
                                ToastUtil.showToastBottom("发送网络请求失败！", Toast.LENGTH_SHORT);
                                refreshCurrentPager(mCurrentIndex);
                            }
//                            for (int i=0;i<pagerTwo.getmOrderListApapter().getCheckedOrderIDXs().size();i++){
//                                if (i<(pagerTwo.getmOrderListApapter().getCheckedOrderIDXs().size()-1)&&mBiz.orderUpdateAudit(pagerTwo.getmOrderListApapter().getCheckedOrderIDXs().get(i),false)){
//                                    showLoadingDialog();
//                                }else if (mBiz.orderUpdateAudit(pagerTwo.getmOrderListApapter().getCheckedOrderIDXs().get(i),true)){
//                                    showLoadingDialog();
//                                } else {
//                                    ToastUtil.showToastBottom("发送网络请求失败！", Toast.LENGTH_SHORT);
//                                    refreshCurrentPager(mCurrentIndex);
//                                    return;
//                                }
//                            }
//                            pagerTwo.getmOrderListApapter().getCheckedOrderIDXs().clear();
//                            refreshCurrentPager(mCurrentIndex);
                            return;
                        }else {
                            ToastUtil.showToastBottom("请先勾选需批释订单", Toast.LENGTH_SHORT);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }

    }

    /**
     * 切换显示的 Pager
     *
     * @param pagerIndex 需要显示的 Pager 的 Index
     */
    private void setCurrentPager(int pagerIndex) {
        try {
            mCurrentIndex = pagerIndex;
            setButtonBackground();
            mViewpager.setCurrentItem(mCurrentIndex);
            mPagersArr[mCurrentIndex].initData();//让选中的 Pager 初始化数据，在 Pager 中判断，仅初始化一次
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 刷新指定的CherckOrderFragment***OrderPage页面数据
     *  @param pagerIndex
     */
    private void refreshCurrentPager(int pagerIndex){
        try {
            if (mPagersArr[pagerIndex]!=null) {
                mPagersArr[pagerIndex].refreshData();

            }
        }catch (Exception e){
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 设置切换按钮的背景颜色
     */
    private void setButtonBackground() {
        try {
            if (mPreviousIndex == mCurrentIndex) {
                return;
            }
            int unSelectedColor = MyApplication.getmRes().getColor(R.color.checkorder_topbutton_background_unSelected);
            int unSelectedTextColor = MyApplication.getmRes().getColor(R.color.checkorder_topbutton_textcolor_unSelected);
            for (Button button : mButtonsArr) {//初始化选择按钮
                if (button != null) {
                    button.setBackgroundColor(unSelectedColor);
                    button.setTextColor(unSelectedTextColor);
                }
            }
            //设置选择按钮
            Button selectedButton = mButtonsArr[mCurrentIndex];
            selectedButton.setBackgroundColor(MyApplication.getmRes().getColor(R.color.checkorder_topbutton_background_Selected));
            selectedButton.setTextColor(MyApplication.getmRes().getColor(R.color.checkorder_topbutton_textcolor_Selected));
            mPreviousIndex = mCurrentIndex;
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }

    /**
     * 网络请求是显示 Dialog
     */
    private void showLoadingDialog() {
        try {
            if (mLoadingDialog == null) {
                mLoadingDialog = new MyLoadingDialog(mContext);
                mLoadingDialog.showDialog();
            }else if (!mLoadingDialog.isShowing()) {
                mLoadingDialog.showDialog();
            }
        } catch (Exception e) {
            ExceptionUtil.handlerException(e);
        }
    }
    /**
     * 订单审核提交成功
     */
    public void orderUpdateAuditSuccess(boolean islast){
        if (islast) {
            mLoadingDialog.dismiss();
            ToastUtil.showToastBottom("批量提交成功，正刷新订单列表", Toast.LENGTH_LONG);
            refreshCurrentPager(mCurrentIndex);
        }
    }
    /**
     * 网络订单审核失败
     */
    public void orderUpdateAuditError(String message){
        try {
            if (mLoadingDialog!=null) {
                mLoadingDialog.dismiss();
            }
            ToastUtil.showToastBottom(String.valueOf(message), Toast.LENGTH_SHORT);
            refreshCurrentPager(mCurrentIndex);
        }catch (Exception e){
            ExceptionUtil.handlerException(e);
        }
    }


    /**
     * ViewPager 切换界面时的监听器
     */
    private class InnerPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            try {
                setCurrentPager(i);
            } catch (Exception e) {
                ExceptionUtil.handlerException(e);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }

    }

}























