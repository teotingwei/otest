package com.example.ocbcpayment.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ocbcpayment.base.PreferenceUtil;
import com.example.ocbcpayment.R;
import com.example.ocbcpayment.base.Utils;

import static com.example.ocbcpayment.base.Constants.EXTRA_AMOUNT;
import static com.example.ocbcpayment.base.Constants.EXTRA_ID;

public class TopupActivity extends AppCompatActivity {

    private EditText edit_text_topup;
    private Button topupBtn;

    PreferenceUtil preferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        initView();
        listener();
    }

    private void initView() {
        setTitle(getString(R.string.label_topup));

        preferenceUtil = PreferenceUtil.getInstance(getBaseContext());
        edit_text_topup = findViewById(R.id.edit_text_topup);
        topupBtn = findViewById(R.id.topupBtn);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void listener() {
        topupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_text_topup.getText().toString().isEmpty()) {
                    Utils.showToast(getString(R.string.error_message_input_amt), TopupActivity.this);
                } else {
                    topupAction();
                }
            }
        });
    }

    private void topupAction() {
        String value = edit_text_topup.getText().toString();
        Intent data = new Intent();
        data.putExtra(EXTRA_AMOUNT, value);
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
