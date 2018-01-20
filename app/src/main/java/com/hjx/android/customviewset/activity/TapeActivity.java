package com.hjx.android.customviewset.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.hjx.android.customviewset.R;
import com.hjx.android.customviewset.widget.TapeView;

public class TapeActivity extends AppCompatActivity {

    private TextView tvIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tape);

        tvIndex = ((TextView) findViewById(R.id.tv_index));
        TapeView tapeView = (TapeView) findViewById(R.id.tapeView);
        tapeView.setListener(new TapeView.OnIndexChangeListener() {
            @Override
            public void changeIndex(double index) {
                tvIndex.setText(String.format("%.1f",index));
            }
        });
    }
}
