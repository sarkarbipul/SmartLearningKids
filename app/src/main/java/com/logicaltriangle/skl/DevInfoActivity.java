package com.logicaltriangle.skl;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class DevInfoActivity extends AppCompatActivity {

    TextView tvObjctive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_info);

        tvObjctive = findViewById(R.id.tvObjctive);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvObjctive.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }
    }
}
