package com.hjx.android.customviewset.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjx.android.customviewset.R;
import com.hjx.android.customviewset.bean.ViewBean;

import java.util.List;

/**
 * Created by hjx on 2017/10/17.
 * You can make it better
 */

public class ViewSetAdapter extends BaseQuickAdapter<ViewBean,BaseViewHolder> {
    public ViewSetAdapter(@LayoutRes int layoutResId, @Nullable List<ViewBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ViewBean item) {
        helper.setText(R.id.tv,item.getName());
    }
}
