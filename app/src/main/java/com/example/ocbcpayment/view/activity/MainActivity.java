package com.example.ocbcpayment.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ocbcpayment.base.PreferenceUtil;
import com.example.ocbcpayment.R;
import com.example.ocbcpayment.model.User;
import com.example.ocbcpayment.database.UserDatabase;
import com.example.ocbcpayment.repository.UserRepository;
import com.example.ocbcpayment.base.Utils;

import static com.example.ocbcpayment.base.Constants.EXTRA_AMOUNT;
import static com.example.ocbcpayment.base.Constants.SEARCH_REQUEST;
import static com.example.ocbcpayment.base.Constants.TOP_UP_REQUEST;

public class MainActivity extends AppCompatActivity {
    private String mobile_no;
    private TextView tv_name;
    private TextView tv_balance;
    private Button logout_btn;
    private Button topup_btn;
    private Button pay_btn;
    PreferenceUtil preferenceUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    private void initView() {
        setTitle(getString(R.string.label_account));
        preferenceUtil = PreferenceUtil.getInstance(getBaseContext());
        mobile_no = preferenceUtil.getSessionKey();
        getUserAccountDetails();

    }

    private void getUserAccountDetails() {
        class GetTasks extends AsyncTask<Void, Void, User> {
            @Override
            protected User doInBackground(Void... voids) {
                User taskList = UserDatabase
                        .getInstance(getApplicationContext())
                        .userDao()
                        .getUserData(preferenceUtil.getSessionKey());
                return taskList;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                updateUI(user);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    private void updateUI(User user) {
        tv_name = findViewById(R.id.tv_name);
        tv_balance = findViewById(R.id.tv_balance);
        tv_name.setText(getString(R.string.label_intro_name) + " " + user.getName());
        tv_balance.setText(getString(R.string.label_intro_bal) + " " + user.getAmount());
        logout_btn = findViewById(R.id.logout_btn);
        topup_btn = findViewById(R.id.topup_btn);
        pay_btn = findViewById(R.id.pay_btn);

        topup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TopupActivity.class);
                startActivityForResult(intent, TOP_UP_REQUEST);
            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferenceUtil.setSessionKey("");
                Utils.showToast(getString(R.string.toast_logout), MainActivity.this);
                finish();
            }
        });
        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, SEARCH_REQUEST);
            }
        });
    }

    private void getTopupRequest( String new_val) {
        class GetTasks extends AsyncTask<Void, Void, User> {

            @Override
            protected User doInBackground(Void... voids) {
                User taskList = UserDatabase
                        .getInstance(getApplicationContext())
                        .userDao()
                        .getUserData(preferenceUtil.getSessionKey());
                return taskList;
            }

            @Override
            protected void onPostExecute(User user) {
                super.onPostExecute(user);
                if (user != null) {
                    int id = user.getId();
                    if (id == -1) {
                        return;
                    }
                    int old_val = Integer.parseInt(user.getAmount());
                    int new_val1 = Integer.parseInt(new_val);
                    String new_amt = Integer.toString(old_val + new_val1);

                    User updateUser = new User(user.getName(),
                            user.getPhone(),
                            user.getPassword(), new_amt);
                    updateUser.setId(user.getId());

                    UserRepository repo = new UserRepository(getApplication());
                    repo.update(updateUser);
                    getUserAccountDetails();

                    Utils.showToast(getString(R.string.toast_topup_success), MainActivity.this);
                } else {
                    Utils.showToast(getString(R.string.toast_fatal_error), MainActivity.this);
                }

            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOP_UP_REQUEST && resultCode == RESULT_OK) {
            getTopupRequest( data.getStringExtra(EXTRA_AMOUNT));
        } else if (requestCode == SEARCH_REQUEST && resultCode == RESULT_OK) {
            getUserAccountDetails();
        }
    }
}