package com.logicaltriangle.skl.model;

import java.util.List;

public class ItemResult {
    private boolean status;
    private List<Item> itemList;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
