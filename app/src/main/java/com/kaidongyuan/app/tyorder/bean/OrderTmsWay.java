package com.kaidongyuan.app.tyorder.bean;


/**
 * 订单类型（运输方式），下单时选择
 */
public class OrderTmsWay implements java.io.Serializable {

    /**
     * IDX
     */
    private String IDX;
    /**
     * 订单类型（运输方式）
     */
    private String BUSINESS_IDX;

    public String getIDX() {
        return IDX;
    }

    public void setIDX(String IDX) {
        this.IDX = IDX;
    }

    public String getBUSINESS_IDX() {
        return BUSINESS_IDX;
    }

    public void setBUSINESS_IDX(String BUSINESS_IDX) {
        this.BUSINESS_IDX = BUSINESS_IDX;
    }

    @Override
    public String toString() {
        return "OrderTmsWay{" +
                "IDX='" + IDX + '\'' +
                ", BUSINESS_IDX='" + BUSINESS_IDX + '\'' +
                '}';
    }
}
