package com.example.tqapp.ui.activity;

import android.view.View;

import com.example.tqapp.common.App;
import com.example.tqapp.common.BaseBindingActivity;
import com.example.tqapp.common.bean.User;
import com.example.tqapp.common.database.Database;
import com.example.tqapp.databinding.ActivityRegisterBinding;
import com.example.tqapp.databinding.ActivityUpdateBinding;
import com.llw.goodweather.utils.StatusBarUtil;

public class UpdatePasswordActivity extends BaseBindingActivity<ActivityUpdateBinding> {


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
                String origin = viewBinder.etOrigin.getText().toString().trim();

                String pwd = viewBinder.etPassword.getText().toString().trim();
                if (origin.isEmpty()) {
                    viewBinder.etOrigin.setError("请输入原密码");
                    return;
                }
                if (pwd.isEmpty()) {
                    viewBinder.etPassword.setError("请输入新密码");
                    return;
                }
                if (!origin.equals(App.user.password)) {
                    toast("原密码错误！");
                    return;
                }
                App.user.password = pwd;
                Database.getDao().updateUserInfo(App.user);
                finish();
                toast("修改成功！");
            }
        });
    }

    @Override
    protected void initData() {

    }
}