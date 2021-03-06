package com.kaidongyuan.app.tyorder.constants;

/**
 * Created by Administrator on 2016/5/16.
 * 网络请求常量
 */
public class URLCostant {


    /**
     * OrderUtil 中使用到
     */
    public static final long ENT_IDX = 9008;

    /**
     * 服务器地址
     */
    //public static final String BASE_URL = "http://oms.kaidongyuan.com:8088/api/";
    public static final String BASE_URL = "http://119.29.148.189:8888/api/";
    /**
     * 文件下载根地址
     */
   // public static final String LOA_URL = "http://oms.kaidongyuan.com:8088/";
    public static final String LOA_URL = "http://119.29.148.189:8888/";
    /**
     * 最新资讯图片下载的根地址
     */
    public static final String INFORMATION_PICTURE_URL = "http://china56pd.com:8090/uploadfile/";

    /**
     * 登录接口
     */
    public static final String LOGIN_URL = BASE_URL + "Login";//登录
    /**
     * 注册账户
     */
    public static final String REGISTER=BASE_URL+"register";
    /**
     * 获取省市区县四级的联动列表
     */
    public static final String NormalAdressList=BASE_URL+"NormalAdressList";
    /**
     * 加入客户资料
     */
    public static final String AddParty=BASE_URL+"AddParty";

    /**
     * 解绑客户资料
     */
    public static final String DeleteAppUserParty=BASE_URL+"DeleteAppUserParty";
    /**
     * 添加客户地址
     */
    public static final String  AddPartyAddress=BASE_URL+"AddAddress";
    /**
     * 删除客户地址
     */
    public static final String  deletePartyAddress=BASE_URL+"DeleteAddress";
    /**
     * 修改客户地址
     */
    public static final String UpdatePartyAddress=BASE_URL+"UpdateAddress";
    /**
     * 获取用户业务类型
     */
    public static final String GET_BUISNESS_LIST = BASE_URL + "GetBuisnessList";

    /**
     * 获取客户列表
     */
    public static final String GET_PARTY_LIST = BASE_URL + "GetPartyList";

    /**
     * 获取在途订单列表
     */
    public static final String GET_ORDER_LIST = BASE_URL + "GetOrderList";

    /**
     * 获取客户地址列表
     */
    public static final String GET_ADDRESS = BASE_URL + "GetAddress";

    /**
     * 获取订单轨迹
     */
    public static final String GET_ORDER_TRAJECTORY = BASE_URL + "GetLocaltionForOrdNo";

    /**
     * 获取客户报表
     */
    public static final String GET_CUSTOMER_CHART_DATA = BASE_URL + "CustomerCount";

    /**
     * 获取产品报表
     */
    public static final String GET_PRODUCT_CHART_DATA = BASE_URL + "ProductCount";

    /**
     * 获取热销产品列表
     */
    public static final String GET_HOT_SELL_PRODUCT = BASE_URL + "GetProductList";

    /**
     * 获取最新资讯
     */
    public static final String GET_NEWEST_INFORMATION = BASE_URL + "Information";

    /**
     * 获取资讯详情
     */
    public static final String GET_INFORMATION_DETAILS = BASE_URL + "GetNewDetail";

    /**
     * 获取订单详情
     */
    public static final String GET_ORDER_DETAIL = BASE_URL + "GetOrderDetail";

    /**
     * 获取订单物流信息
     */
    public static final String GET_ORDER_TMSLIST = BASE_URL + "GetOrderTmsList";

//    /**
//     * 获取物流信息详情
//     */
//    public static final String GET_ORDER_TMS_INFORMATION = BASE_URL + "GetOrderTmsInfo";

    /**
     * 获取物流信息详情
     */
    public static final String GET_ORDER_TMS_INFORMATION = BASE_URL + "GetOrderTmsOrderNoInfo";

    /**
     * 获取订单位置信息
     */
    public static final String GET_LOCATION = BASE_URL + "GetLocaltion";

    /**
     * 获取最新版本 app 信息
     */
    public static final String CHECK_VERSION = BASE_URL + "GetVersion";

    /**
     * 修改密码接口
     */
    public static final String UPDATA_PASSWORD = BASE_URL + "modifyPassword";

    /**
     * 获取付款方式 post strLicense  过来就行了
     */
    public static final String GET_PAYMENT_TYPE = BASE_URL + "GetPaymentType";

    /**
     * 获取产品品牌和分类列表
     */
    public static final String GET_PRODUCT_TYPE = BASE_URL + "GetProductType";

    /**
     * 根据品牌分类信息获取产品列表
     */
    public static final String GET_PRDUCT_LIST_TYPE = BASE_URL + "GetProductListType";

    /**
     * 获取促销信息
     */
    public static final String GET_PROMOTION_INFORMATION = BASE_URL + "GetPartySalePolicy";

    /**
     * 提交获取促销信息
     */
    public static final String SUBMIT_ORDER = BASE_URL + "SubmitOrder1";

    /**
     * 最终提交订单
     */
    public static final String CONFIRM_ORDER = BASE_URL + "ConfirmOrderTest";

    /**
     * 获取赠品品类详细信息
     */
    public static final String GET_COMMODITY_DATA = BASE_URL + "GetProductListType";

    /**
     * 获取电子签名和交货现场图片
     */
    public static final String GETAUTOGRAPH = BASE_URL + "GetAutograph";

    /**
     * 1.1 、添加客户库存
     */
    public static final String AddStock=BASE_URL+"AddStock";

    /**
     * 1.2 展示客户库存登记表
     */
    public static final String GetStockList=BASE_URL+"GetStockList";
    /**
     * 1.2.1 展示客户库存登记表
     */
    public static final String GetStockList1=BASE_URL+"GetStockList1";
    /**
     * 1.3 展示客户库存详细登记
     */
    public static final String GetAppStockList=BASE_URL+"GetAppStockList";
    /**
     * 1.7 取消客户库存
     */
    public static final String CancelStock=BASE_URL+"CancelStock";
    /**
     * 取消待接收状态的订单
     */
    public static final String OrderCancel=BASE_URL+"OrderCancel";
    /**
     * 1.9 账单列表
     */
    public static final String GetAppBillFeeList=BASE_URL+"GetAppBillFeeList";
    /**
     * 2.0 费用列表
     */
    public static final String GetAppBusinessFeeList=BASE_URL+"GetAppBusinessFeeList";

    /**
     * 20170421 订单审核
     */
    public static final String UpdateAudit=BASE_URL+"UpdateAudit";
    /**
     * 20170421 订单反审核
     */
    public static final String RuturnAudit=BASE_URL+"RuturnAudit";
    /**
     * 20171020 对未审核订单进行审核时修改订单中下单产品数量
     */
    public static final String SetProductQty=BASE_URL+"SetProductQty";
    /**
     * 20171211 获取销售发运方式
     */
    public static final String GetDeliveryWay=BASE_URL+"GetDeliveryWay";
    /**
     * 20171218 修改未审核订单的下单备注信息
     */
    public static final String UpdateRemark=BASE_URL+"UpdateRemark";
    /**
     * 20190816 补20180907 获取订单类型
     */
    public static final String GetToBusiness_Type = BASE_URL + "GetToBusiness_Type";

}
















