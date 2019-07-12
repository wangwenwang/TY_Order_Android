package com.kaidongyuan.app.tyorder.bean;

public  class OrderDetails implements java.io.Serializable {
	private String PRODUCT_NO;
	private String PRODUCT_NAME;
	private String PRODUCT_DESC;
	private double ORDER_QTY;
	private String ORDER_UOM;
	private String ORDER_WEIGHT;
	private String ORDER_VOLUME;
	private String ISSUE_QTY;
	private String ISSUE_WEIGHT;
	private String ISSUE_VOLUME;
	private String PRODUCT_PRICE;
	private double ACT_PRICE;
	private String MJ_PRICE;
	private String MJ_REMARK;
	private double ORG_PRICE;
	private String PRODUCT_URL;
	private String PRODUCT_TYPE;
	private String OD_IDX;//订单详情内货物信息的id

	public String getPRODUCT_TYPE() {
		return PRODUCT_TYPE;
	}

	public void setPRODUCT_TYPE(String PRODUCT_TYPE) {
		this.PRODUCT_TYPE = PRODUCT_TYPE;
	}

	public double getACT_PRICE() {
		return ACT_PRICE;
	}

	public void setACT_PRICE(double ACT_PRICE) {
		this.ACT_PRICE = ACT_PRICE;
	}

	public String getMJ_PRICE() {
		return MJ_PRICE;
	}

	public void setMJ_PRICE(String MJ_PRICE) {
		this.MJ_PRICE = MJ_PRICE;
	}

	public String getMJ_REMARK() {
		return MJ_REMARK;
	}

	public void setMJ_REMARK(String MJ_REMARK) {
		this.MJ_REMARK = MJ_REMARK;
	}

	public String getPRODUCT_URL() {
		return PRODUCT_URL;
	}

	public void setPRODUCT_URL(String PRODUCT_URL) {
		this.PRODUCT_URL = PRODUCT_URL;
	}

	public String getPRODUCT_NO() {
		return PRODUCT_NO;
	}

	public void setPRODUCT_NO(String PRODUCT_NO) {
		this.PRODUCT_NO = PRODUCT_NO;
	}

	public String getPRODUCT_NAME() {
		return PRODUCT_NAME;
	}

	public void setPRODUCT_NAME(String PRODUCT_NAME) {
		this.PRODUCT_NAME = PRODUCT_NAME;
	}

	public double getORDER_QTY() {
		return ORDER_QTY;
	}

	public void setORDER_QTY(double ORDER_QTY) {
		this.ORDER_QTY = ORDER_QTY;
	}

	public double getORG_PRICE() {
		return ORG_PRICE;
	}

	public void setORG_PRICE(double ORG_PRICE) {
		this.ORG_PRICE = ORG_PRICE;
	}

	public String getORDER_UOM() {
		return ORDER_UOM;
	}

	public void setORDER_UOM(String ORDER_UOM) {
		this.ORDER_UOM = ORDER_UOM;
	}

	public String getORDER_WEIGHT() {
		return ORDER_WEIGHT;
	}

	public void setORDER_WEIGHT(String ORDER_WEIGHT) {
		this.ORDER_WEIGHT = ORDER_WEIGHT;
	}

	public String getORDER_VOLUME() {
		return ORDER_VOLUME;
	}

	public void setORDER_VOLUME(String ORDER_VOLUME) {
		this.ORDER_VOLUME = ORDER_VOLUME;
	}

	public String getISSUE_QTY() {
		return ISSUE_QTY;
	}

	public void setISSUE_QTY(String ISSUE_QTY) {
		this.ISSUE_QTY = ISSUE_QTY;
	}

	public String getISSUE_WEIGHT() {
		return ISSUE_WEIGHT;
	}

	public void setISSUE_WEIGHT(String ISSUE_WEIGHT) {
		this.ISSUE_WEIGHT = ISSUE_WEIGHT;
	}

	public String getISSUE_VOLUME() {
		return ISSUE_VOLUME;
	}

	public void setISSUE_VOLUME(String ISSUE_VOLUME) {
		this.ISSUE_VOLUME = ISSUE_VOLUME;
	}

	public String getPRODUCT_PRICE() {
		return PRODUCT_PRICE;
	}

	public void setPRODUCT_PRICE(String PRODUCT_PRICE) {
		this.PRODUCT_PRICE = PRODUCT_PRICE;
	}

	public String getPRODUCT_DESC() {
		return PRODUCT_DESC;
	}

	public void setPRODUCT_DESC(String PRODUCT_DESC) {
		this.PRODUCT_DESC = PRODUCT_DESC;
	}

	public String getOD_IDX() {
		return OD_IDX;
	}

	public void setOD_IDX(String OD_IDX) {
		this.OD_IDX = OD_IDX;
	}

	@Override
	public String toString() {
		return "OrderDetails{" +
				"PRODUCT_NO='" + PRODUCT_NO + '\'' +
				", PRODUCT_NAME='" + PRODUCT_NAME + '\'' +
				", PRODUCT_DESC='" + PRODUCT_DESC + '\'' +
				", ORDER_QTY=" + ORDER_QTY +
				", ORDER_UOM='" + ORDER_UOM + '\'' +
				", ORDER_WEIGHT='" + ORDER_WEIGHT + '\'' +
				", ORDER_VOLUME='" + ORDER_VOLUME + '\'' +
				", ISSUE_QTY='" + ISSUE_QTY + '\'' +
				", ISSUE_WEIGHT='" + ISSUE_WEIGHT + '\'' +
				", ISSUE_VOLUME='" + ISSUE_VOLUME + '\'' +
				", PRODUCT_PRICE='" + PRODUCT_PRICE + '\'' +
				", ACT_PRICE=" + ACT_PRICE +
				", MJ_PRICE='" + MJ_PRICE + '\'' +
				", MJ_REMARK='" + MJ_REMARK + '\'' +
				", ORG_PRICE=" + ORG_PRICE +
				", PRODUCT_URL='" + PRODUCT_URL + '\'' +
				", PRODUCT_TYPE='" + PRODUCT_TYPE + '\'' +
				", OD_IDX='" + OD_IDX + '\'' +
				'}';
	}
}
