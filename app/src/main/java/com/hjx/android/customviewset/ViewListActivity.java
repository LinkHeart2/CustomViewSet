package com.hjx.android.customviewset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hjx.android.customviewset.activity.AnimTestActivity;
import com.hjx.android.customviewset.activity.ClipImageTestActivity;
import com.hjx.android.customviewset.activity.FlipboardActivity;
import com.hjx.android.customviewset.activity.MainActivity;
import com.hjx.android.customviewset.activity.PraiseActivity;
import com.hjx.android.customviewset.activity.ShapeTestActivity;
import com.hjx.android.customviewset.activity.TapeActivity;
import com.hjx.android.customviewset.adapter.ViewSetAdapter;
import com.hjx.android.customviewset.bean.ViewBean;

import java.util.ArrayList;
import java.util.List;

public class ViewListActivity extends AppCompatActivity {

    private RecyclerView rv;
    private List<ViewBean> list;

    {
        list = new ArrayList<>();
        list.add(new ViewBean("图案解锁",MainActivity.class));
        list.add(new ViewBean("图片选择挖空",ClipImageTestActivity.class));
        list.add(new ViewBean("仿即刻点赞",PraiseActivity.class));
        list.add(new ViewBean("仿Flipboard效果",FlipboardActivity.class));
        list.add(new ViewBean("卷尺",TapeActivity.class));
        list.add(new ViewBean("shape测试",ShapeTestActivity.class));
        list.add(new ViewBean("anim测试",AnimTestActivity.class));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        rv = ((RecyclerView) findViewById(R.id.rv));
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        ViewSetAdapter adapter = new ViewSetAdapter(R.layout.rv_view_list, list);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(ViewListActivity.this,list.get(position).getActivity()));
            }
        });
        rv.setAdapter(adapter);
    }
}
