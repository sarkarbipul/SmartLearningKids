package com.logicaltriangle.skl.model;

import java.util.List;

public class CatResult {
    private boolean status;

    //@SerializedName("category")
    private List<Category> catList;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Category> getCatList() {
        return catList;
    }

    public void setCatList(List<Category> catList) {
        this.catList = catList;
    }
}
