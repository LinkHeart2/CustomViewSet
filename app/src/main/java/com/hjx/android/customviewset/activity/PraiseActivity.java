package com.hjx.android.customviewset.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hjx.android.customviewset.R;
import com.hjx.android.customviewset.widget.JKPraise;

public class PraiseActivity extends AppCompatActivity implements View.OnClickListener {

    private JKPraise jkPraise;
    private EditText etCount;
    private Button btnSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_praise);

        jkPraise = ((JKPraise) findViewById(R.id.jk_praise));

        etCount = ((EditText) findViewById(R.id.et_count));
        btnSet = ((Button) findViewById(R.id.bt_set));

        btnSet.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_set:
                String s = etCount.getText().toString();
                if(TextUtils.isEmpty(s))
                    return;
                try {
                    int i = Integer.parseInt(s);
                    jkPraise.setCount(i);
                }catch (NumberFormatException e){
                    Toast.makeText(this,"请输入正确数字",Toast.LENGTH_SHORT).show();
                }
            break;
            default:
            break;
            }
    }
}
