package com.hjx.android.customviewset.activity;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.PathInterpolator;

import com.hjx.android.customviewset.R;


public class AnimTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_test);


//        ListView lv = (ListView) findViewById(R.id.lv);
//        ArrayList<String> strings = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            strings.add("张三"+i);
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, strings);
//        lv.setAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
//        LinearLayout llAdd = (LinearLayout) findViewById(R.id.ll_add_test);
//        for (int i = 0; i < 10; i++) {
//            TextView view = new TextView(this);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
//            view.setLayoutParams(params);
//            view.setText("张飞"+i);
//            view.setTextSize(20);
//            llAdd.addView(view);
//        }

        View iv = findViewById(R.id.iv);
        float x = iv.getTranslationX();
        float y = iv.getTranslationY();

        Path path = new Path();
        path.moveTo(0,0);
        path.lineTo(0.25f,-1);
        path.lineTo(0.5f,1);
        path.lineTo(0.75f,-1);
        path.lineTo(1,1);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            PathInterpolator interpolator = new PathInterpolator(path);
            ObjectAnimator animator = ObjectAnimator.ofFloat(iv, "translationY", y, y - 100);
            animator.setDuration(5000).setInterpolator(interpolator);
            animator.start();

            int widthPixels = getResources().getDisplayMetrics().widthPixels;
            ObjectAnimator.ofFloat(iv,"translationX",x,widthPixels).setDuration(5000).start();
        }

    }
}
