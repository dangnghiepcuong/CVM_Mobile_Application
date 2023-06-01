package com.example.cvm_mobile_application.ui;

import org.json.JSONException;

public interface ViewStructure {
    public void implementView();
    public void bindViewData() throws JSONException;
    public void setViewListener();
}
