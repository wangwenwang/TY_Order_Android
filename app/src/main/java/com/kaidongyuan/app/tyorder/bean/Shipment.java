package com.kaidongyuan.app.tyorder.bean;

import java.io.Serializable;

/**
 * Created by ${tom} on 2017/10/11.
 */
public class Shipment implements Serializable {
    private String AUDIT_FLAG;
    private String ERROR_FLAG;
    private String ERROR_DESC;
    private String REFERENCE01;//中转单号
    private String REFERENCE04;//中转电话

    public String getAUDIT_FLAG() {
        return AUDIT_FLAG;
    }

    public void setAUDIT_FLAG(String AUDIT_FLAG) {
        this.AUDIT_FLAG = AUDIT_FLAG;
    }

    public String getERROR_FLAG() {
        return ERROR_FLAG;
    }

    public void setERROR_FLAG(String ERROR_FLAG) {
        this.ERROR_FLAG = ERROR_FLAG;
    }

    public String getERROR_DESC() {
        return ERROR_DESC;
    }

    public void setERROR_DESC(String ERROR_DESC) {
        this.ERROR_DESC = ERROR_DESC;
    }

    public String getREFERENCE01() {
        return REFERENCE01;
    }

    public void setREFERENCE01(String REFERENCE01) {
        this.REFERENCE01 = REFERENCE01;
    }

    public String getREFERENCE04() {
        return REFERENCE04;
    }

    public void setREFERENCE04(String REFERENCE04) {
        this.REFERENCE04 = REFERENCE04;
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "AUDIT_FLAG='" + AUDIT_FLAG + '\'' +
                ", ERROR_FLAG='" + ERROR_FLAG + '\'' +
                ", ERROR_DESC='" + ERROR_DESC + '\'' +
                ", REFERENCE01='" + REFERENCE01 + '\'' +
                ", REFERENCE04='" + REFERENCE04 + '\'' +
                '}';
    }
}
