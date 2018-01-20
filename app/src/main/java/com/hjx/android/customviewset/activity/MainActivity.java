package com.hjx.android.customviewset.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hjx.android.customviewset.R;
import com.hjx.android.customviewset.widget.SquareCipherView;

public class MainActivity extends AppCompatActivity implements SquareCipherView.LineListener {

    private SquareCipherView scv;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scv = ((SquareCipherView) findViewById(R.id.scv));
        tvInfo = ((TextView) findViewById(R.id.tv_info));
        scv.setListener(this);
    }

    @Override
    public void onEvent(int event) {
        switch (event){
            case SquareCipherView.TOO_LITTLE:
                tvInfo.setText(R.string.too_little);
                break;
            case SquareCipherView.INPUT_SUCCESS:
                tvInfo.setText(R.string.input_again);
                break;
            default:
            break;
            }
    }

    public void goToImageTest(View view) {
        startActivity(new Intent(this,ClipImageTestActivity.class));
    }
}
