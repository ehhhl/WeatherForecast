package com.example.tqapp.common.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tqapp.common.bean.History;
import com.example.tqapp.common.bean.Record;
import com.example.tqapp.common.bean.User;

import java.util.List;


@Dao
public interface DatabaseDao {
    /**
     * 注册
     *
     * @param user
     */
    @Insert
    void register(User user);

    /**
     * 更新用户信息
     *
     * @param user
     */
    @Update
    void updateUserInfo(User user);

    /**
     * 根据账号和密码查询用户
     *
     * @param username
     * @param password
     * @return
     */
    @Query("select * from user where account=:username and password = :password")
    User queryUser(String username, String password);

    // 根据账号和日期获取记录
    @Query("select * from record where (userAccount =:account ) and time like :date")
    List<Record> getRecord(String account, String date);




    @Query("select * from record where (userAccount =:account )")
    List<Record> getRecord(String account);

    /**
     * 根据账号查询用户信息
     *
     * @param username
     * @return
     */
    @Query("select * from user where account=:username")
    User queryUser(String username);

    /**
     * 根据id查询用户
     *
     * @param id
     * @return
     */
    @Query("select * from user where id=:id")
    User queryUser(int id);

    @Insert
    void addHistory(History history);

    @Query("select * from history where account = :account order by id desc limit :count ")
    List<History> getMyHistory(String account, int count);


    @Query("select * from history where account = :account and provinces=:provinces and city = :city")
    History getMyHistory(String account, String provinces, String city);

    @Insert
    void addRecord(Record record);

    @Delete
    void deleteRecord(Record data);

    @Update
    void updateRecord(Record data);
}
