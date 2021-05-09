package com.example.ocbcpayment.view.activity;

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

import static com.example.ocbcpayment.base.Constants.EXTRA_MOBILE_RECEIVER;
import static com.example.ocbcpayment.base.Constants.EXTRA_MOBILE_RECEIVER_NAME;

public class PayActivity extends AppCompatActivity {
    private String mobile_no_receiver;
    private String name_receiver;
    private TextView tv_name;
    private TextView recepient_name_tv;
    private EditText input_amt_et;
    private Button pay_btn;
    PreferenceUtil preferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        if (getIntent() != null) {
            initView();
            setListener();
        } else {
            Utils.showToast(getString(R.string.toast_fatal_error), this);
        }
    }

    private void initView() {
        preferenceUtil = PreferenceUtil.getInstance(getBaseContext());
        setTitle(getString(R.string.label_transfer));

        mobile_no_receiver = getIntent().getStringExtra(EXTRA_MOBILE_RECEIVER);
        name_receiver = getIntent().getStringExtra(EXTRA_MOBILE_RECEIVER_NAME);

        tv_name = findViewById(R.id.tv_name);
        recepient_name_tv = findViewById(R.id.recepient_name_tv);
        input_amt_et = findViewById(R.id.input_amt_et);
        pay_btn = findViewById(R.id.pay_btn);

        recepient_name_tv.setText("Payment to: " + name_receiver);
        getUserAccountDetails(preferenceUtil.getSessionKey());
    }


    private void setListener() {
        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (input_amt_et.getText().toString().isEmpty()) {
                    Utils.showToast(getString(R.string.error_message_input_amt), PayActivity.this);
                } else {
                    getUserBal_remove(preferenceUtil.getSessionKey());
                }
            }
        });
    }

    private void getUserAccountDetails(String mobile) {
        class GetTasks extends AsyncTask<Void, Void, User> {
            @Override
            protected User doInBackground(Void... voids) {
                User taskList = UserDatabase
                        .getInstance(getApplicationContext())
                        .userDao()
                        .getUserData(mobile);
                return taskList;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                tv_name.setText("your balance is: " + user.getAmount());
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void getUserBal_remove(String mobile) {
        class GetTasks extends AsyncTask<Void, Void, User> {
            @Override
            protected User doInBackground(Void... voids) {
                User taskList = UserDatabase
                        .getInstance(getApplicationContext())
                        .userDao()
                        .getUserData(mobile);
                return taskList;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                updateUserAccountBal(user);
                getUserAccountDetails(user.getPhone());
                getUserBal_add(mobile_no_receiver);

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void updateUserAccountBal(User user) {
        int acc_bal = Integer.parseInt(user.getAmount());
        int new_acc_bal = acc_bal - Integer.parseInt(input_amt_et.getText().toString());

        String new_amt = Integer.toString(new_acc_bal);

        User user1 = new User(user.getName(),
                user.getPhone(),
                user.getPassword(), new_amt);
        user1.setId(user.getId());

        UserRepository repo = new UserRepository(getApplication());
        repo.update(user1);
    }

    private void getUserBal_add(String mobile) {
        class GetTasks extends AsyncTask<Void, Void, User> {
            @Override
            protected User doInBackground(Void... voids) {
                User taskList = UserDatabase
                        .getInstance(getApplicationContext())
                        .userDao()
                        .getUserData(mobile);
                return taskList;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);

                int acc_bal = Integer.parseInt(user.getAmount());
                int new_acc_bal = acc_bal + Integer.parseInt(input_amt_et.getText().toString());

                String new_amt = Integer.toString(new_acc_bal);

                User user1 = new User(user.getName(),
                        user.getPhone(),
                        user.getPassword(), new_amt);
                user1.setId(user.getId());

                UserRepository repo = new UserRepository(getApplication());
                repo.update(user1);
                setResult(RESULT_OK);
                finish();


            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

}