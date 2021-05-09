package com.example.ocbcpayment.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ocbcpayment.base.PreferenceUtil;
import com.example.ocbcpayment.R;
import com.example.ocbcpayment.model.User;
import com.example.ocbcpayment.database.UserDatabase;
import com.example.ocbcpayment.repository.UserRepository;
import com.example.ocbcpayment.base.Utils;

import static com.example.ocbcpayment.base.Constants.CREATE_ACC_REQUEST;
import static com.example.ocbcpayment.base.Constants.EXTRA_MOBILE;
import static com.example.ocbcpayment.base.Constants.EXTRA_NAME;
import static com.example.ocbcpayment.base.Constants.EXTRA_PASSWORD;

public class LoginActivity extends AppCompatActivity {
    private EditText edit_text_mobile;
    private EditText edit_text_password;
    private TextView create_btn;
    private Button login_btn;
    private boolean doubleBackToExitPressedOnce;

    PreferenceUtil preferenceUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        listener();
    }

    private void initView() {
        preferenceUtil = PreferenceUtil.getInstance(getBaseContext());
        preferenceUtil.setSessionKey("");

        edit_text_mobile = findViewById(R.id.edit_text_mobile);
        edit_text_password = findViewById(R.id.edit_text_password);
        create_btn = findViewById(R.id.create_btn);
        login_btn = findViewById(R.id.login_btn);
    }


    private void listener() {
        create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivityForResult(intent,CREATE_ACC_REQUEST);
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mobile = edit_text_mobile.getText().toString().trim();
                String password = edit_text_password.getText().toString().trim();

                if (mobile.isEmpty() || password.isEmpty()) {
                    Utils.showToast(getString(R.string.error_message_loginpage), LoginActivity.this);
                    return;
                } else {
                    checkCredentials(mobile, password);
                }


            }
        });
    }

    private void checkCredentials(String mobile, String password) {
        class checkCredentials extends AsyncTask<Void, Void, Integer> {


            @Override
            protected Integer doInBackground(Void... voids) {
                Integer taskList = UserDatabase
                        .getInstance(getApplicationContext())
                        .userDao()
                        .validUser(mobile, password);
                return taskList;
            }

            @Override
            protected void onPostExecute(Integer result) {
                super.onPostExecute(result);
                if (result == 1) {
                    preferenceUtil.setSessionKey(edit_text_mobile.getText().toString());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    edit_text_mobile.setText("");
                    edit_text_password.setText("");
                } else {
                    Utils.showToast(getString(R.string.error_message_login_return), LoginActivity.this);
                }

            }
        }

        checkCredentials gt = new checkCredentials();
        gt.execute();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Utils.showToast(getString(R.string.message_press_back_to_exit), this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_ACC_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(EXTRA_NAME);
            String mobile = data.getStringExtra(EXTRA_MOBILE);
            String password = data.getStringExtra(EXTRA_PASSWORD);

            User user = new User(name, mobile, password, "0");

            UserRepository repo = new UserRepository(getApplication());
            repo.insert(user);
            Utils.showToast(getString(R.string.toast_acc_created), this);
        } else {
            Utils.showToast(getString(R.string.toast_fatal_error), this);
        }
    }

}
