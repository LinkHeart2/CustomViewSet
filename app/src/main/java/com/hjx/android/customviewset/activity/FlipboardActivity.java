package com.hjx.android.customviewset.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hjx.android.customviewset.R;
import com.hjx.android.customviewset.widget.FlipboardView;

public class FlipboardActivity extends AppCompatActivity {

    private FlipboardView fbv;
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flipboard);

        fbv = ((FlipboardView) findViewById(R.id.fbv));
        btnStart = ((Button) findViewById(R.id.btn_start));
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStart.setEnabled(false);
                ObjectAnimator upDegree = ObjectAnimator.ofInt(fbv, "upDegree", 0, 45).setDuration(800);
                upDegree.setStartDelay(500);
                ObjectAnimator rotaDegree = ObjectAnimator.ofInt(fbv, "rotaDegree", 0, 270).setDuration(800);
                rotaDegree.setStartDelay(500);
                ObjectAnimator leftDegree = ObjectAnimator.ofInt(fbv, "leftDegree", 0, 45).setDuration(800);
                leftDegree.setStartDelay(500);

                AnimatorSet set = new AnimatorSet();
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fbv.reset();
                        btnStart.setEnabled(true);
                    }
                });

                set.playSequentially(upDegree,rotaDegree,leftDegree);
                set.start();

            }
        });


    }
}
