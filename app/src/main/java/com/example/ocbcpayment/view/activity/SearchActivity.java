package com.example.ocbcpayment.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ocbcpayment.view.adapter.PayerAdapter;
import com.example.ocbcpayment.base.PreferenceUtil;
import com.example.ocbcpayment.R;
import com.example.ocbcpayment.model.User;
import com.example.ocbcpayment.database.UserDatabase;
import com.example.ocbcpayment.base.Utils;

import java.util.List;

import static com.example.ocbcpayment.base.Constants.PAY_REQUEST;

public class SearchActivity extends AppCompatActivity {
    private String mobile_no;
    RecyclerView recyclerView;
    PayerAdapter arrayAdapter;

    PreferenceUtil preferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }
    @Override
    protected void onResume() {
        initView();
        super.onResume();
    }
    private void initView() {
        setTitle(getString(R.string.label_search));
        preferenceUtil = PreferenceUtil.getInstance(getBaseContext());
        recyclerView = findViewById(R.id.search_result_rv);
        getPayers();
    }


    private void getPayers() {
        class GetTasks extends AsyncTask<String, Void, List<User>> {
            @Override
            protected List<User> doInBackground(String... strings) {
                List<User> taskList = UserDatabase
                        .getInstance(getApplicationContext())
                        .userDao()
                        .getAllPayers(preferenceUtil.getSessionKey());
                return taskList;
            }

            @Override
            protected void onPostExecute(List<User> user) {
                super.onPostExecute(user);
                arrayAdapter = new PayerAdapter(SearchActivity.this, user);
                recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                recyclerView.setAdapter(arrayAdapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_icon);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getString(R.string.hint_search_payer));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                arrayAdapter.getFilter().filter(s);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAY_REQUEST && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        } else {
            Utils.showToast(getString(R.string.toast_fatal_error), SearchActivity.this);
        }
    }

}