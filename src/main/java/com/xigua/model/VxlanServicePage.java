package com.xigua.model;

import java.util.List;

public class VxlanServicePage {
    private List<VxlanServiceModel> model;
    private int currency;
    public List<VxlanServiceModel> getModel() {
        return model;
    }
    public void setModel(List<VxlanServiceModel> model) {
        this.model = model;
    }
    public int getCurrency() {
        return currency;
    }
    public void setCurrency(int currency) {
        this.currency = currency;
    }
}
