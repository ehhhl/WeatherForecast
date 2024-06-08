package com.example.tqapp.ui.activity;

import android.view.View;

import com.example.tqapp.common.BaseBindingActivity;
import com.example.tqapp.common.bean.User;
import com.example.tqapp.common.database.Database;
import com.example.tqapp.databinding.ActivityRegisterBinding;
import com.llw.goodweather.utils.StatusBarUtil;

public class RegisterActivity extends BaseBindingActivity<ActivityRegisterBinding> {


    @Override
    protected void initListener() {
        StatusBarUtil.transparencyBar(this);//透明状态栏
        viewBinder.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewBinder.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = viewBinder.etAccount.getText().toString().trim();
                String name = viewBinder.etUsername.getText().toString().trim();
                String pwd = viewBinder.etPassword.getText().toString().trim();
                if (account.isEmpty()) {
                    viewBinder.etAccount.setError("请输入账号");
                    return;
                }
                if (name.isEmpty()) {
                    viewBinder.etUsername.setError("请输入昵称");
                    return;
                }
                if (pwd.isEmpty()) {
                    viewBinder.etPassword.setError("请输入密码");
                    return;
                }
                User user = Database.getDao().queryUser(account);
                if (user == null) {
                    user = new User();
                    user.account = account;
                    user.name = name;
                    user.password = pwd;
                    Database.getDao().register(user);
                    toast("注册成功！");
                    finish();
                }else {
                    toast("账户已被使用！");
                }
            }
        });
    }

    @Override
    protected void initData() {

    }
}