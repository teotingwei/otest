package com.example.ocbcpayment.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ocbcpayment.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT phone FROM user_table where name = :name")
    String loadFromUser(String name);

    @Query("SELECT * FROM user_table where phone = :phone")
    User getUserData(String phone);

    @Query("UPDATE user_table SET amount = :amount where phone = :mobile")
    void updateamt(String mobile, String amount);

    @Query("SELECT count (name) FROM user_table where phone = :phone_input AND password = :password_input")
    int validUser(String phone_input, String password_input);


    @Query("SELECT * FROM user_table WHERE phone != :phone" +
            " ORDER BY amount DESC ")
    List<User> getAllPayers(String phone);


}