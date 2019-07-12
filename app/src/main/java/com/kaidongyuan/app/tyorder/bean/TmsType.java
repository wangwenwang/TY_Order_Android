package com.kaidongyuan.app.tyorder.bean;

import java.io.Serializable;

/**
 * Created by ${tom} on 2017/12/11.
 */
public class TmsType implements Serializable {
    private String item_name;
    private String item_desc;

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }
}
