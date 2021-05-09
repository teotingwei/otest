package com.example.ocbcpayment.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.ocbcpayment.database.dao.UserDao;
import com.example.ocbcpayment.database.UserDatabase;
import com.example.ocbcpayment.model.User;

public class UserRepository {
    private UserDao userDao;
    private Application application;

    public UserRepository(Application application) {
        UserDatabase database = UserDatabase.getInstance(application);
        application = application;
        userDao = database.userDao();
    }

    public void insert(User user) {
        new InsertUserAsyncTask(userDao).execute(user);
    }

    public void update(User user) {
        new UpdateUserAsyncTask(userDao).execute(user);
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private InsertUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.insert(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao userDao;

        private UpdateUserAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDao.update(users[0]);
            return null;
        }
    }


}
