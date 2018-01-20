package com.hjx.android.customviewset.bean;

/**
 * Created by hjx on 2017/10/17.
 * You can make it better
 */

public class ViewBean {
    private String name;
    private Class activity;

    public ViewBean(String name, Class activity) {
        this.name = name;
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getActivity() {
        return activity;
    }

    public void setActivity(Class activity) {
        this.activity = activity;
    }
}
