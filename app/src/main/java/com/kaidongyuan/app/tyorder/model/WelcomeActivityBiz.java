package com.kaidongyuan.app.tyorder.model;

import android.content.Context;
import android.widget.ImageView;

import com.kaidongyuan.app.tyorder.R;
import com.kaidongyuan.app.tyorder.constants.BusinessConstants;
import com.kaidongyuan.app.tyorder.constants.SharedPreferenceConstants;
import com.kaidongyuan.app.tyorder.ui.activity.WelcomeActivity;
import com.kaidongyuan.app.tyorder.util.ExceptionUtil;
import com.kaidongyuan.app.tyorder.util.SharedPreferencesUtil;

/**
 * Created by Administrator on 2016/5/16.
 * WelcomeActivity 的业务类
 */
public class WelcomeActivityBiz {

    private Context mContext;
    private WelcomeActivity mWelcomeActivity;

    public WelcomeActivityBiz(WelcomeActivity activity) {
        this.mContext = activity;
        this.mWelcomeActivity = activity;
    }


    /**
     * 根据业务类型设置欢迎界面的图片
     * @param mImageViewPicture 显示图片的控件
     */
    public void setImageViewBackground(ImageView mImageViewPicture) {
        try {
            String businessType = SharedPreferencesUtil.getValueByName(SharedPreferenceConstants.BUSSINESS_CODE,
                    SharedPreferenceConstants.NAME, SharedPreferencesUtil.STRING);
//            根据业务类型不同，打开app显示不同的图片
//            if (businessType.contains(BusinessConstants.TYPE_YIBAO)) {
//                mImageViewPicture.setBackgroundResource(R.drawable.bg_splash);
//            } else if (businessType.contains(BusinessConstants.TYPE_DIKUI)) {
//                mImageViewPicture.setBackgroundResource(R.drawable.bg_splash2);
//            }else if (businessType.contains(BusinessConstants.TYPE_KANGSHIFU)) {
//                mImageViewPicture.setBackgroundResource(R.drawable.bg_splash3);
//            }else {
//                mImageViewPicture.setImageResource(R.drawable.login_background);
//            }
//          通宇物流启动界面
            mImageViewPicture.setBackgroundResource(R.drawable.bg_splash);
        } catch (Exception e) {
            mImageViewPicture.setImageResource(R.drawable.login_background);
            ExceptionUtil.handlerException(e);
        }
    }
}











