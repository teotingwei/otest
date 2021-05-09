package com.example.ocbcpayment.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ocbcpayment.R;
import com.example.ocbcpayment.base.Utils;

import static com.example.ocbcpayment.base.Constants.EXTRA_MOBILE;
import static com.example.ocbcpayment.base.Constants.EXTRA_NAME;
import static com.example.ocbcpayment.base.Constants.EXTRA_PASSWORD;

public class CreateAccountActivity extends AppCompatActivity {


    private EditText editTextName;
    private EditText editTextMobile;
    private EditText editTextPassword;
    private Button createAccBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createacc);

        initView();
        listener();

    }

    private void initView() {
        setTitle(getString(R.string.title_create_acc));
        editTextName = findViewById(R.id.et_createacc);
        editTextMobile = findViewById(R.id.et_mobile);
        editTextPassword = findViewById(R.id.et_password);
        createAccBtn = findViewById(R.id.create_btn);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void listener() {
        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String name = editTextName.getText().toString();
        String mobile = editTextMobile.getText().toString();
        String password = editTextPassword.getText().toString();


        if (name.trim().isEmpty() || mobile.trim().isEmpty()|| password.trim().isEmpty()) {
            Utils.showToast(getString(R.string.error_message_createaccpage), this);
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_MOBILE, mobile);
        data.putExtra(EXTRA_PASSWORD, password);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
