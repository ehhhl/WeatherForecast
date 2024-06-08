package com.example.tqapp.ui.activity;

import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.tqapp.HomeActivity;
import com.example.tqapp.common.App;
import com.example.tqapp.common.BaseBindingActivity;
import com.example.tqapp.common.PreferenceUtil;
import com.example.tqapp.common.bean.User;
import com.example.tqapp.common.database.Database;
import com.example.tqapp.databinding.ActivityLoginBinding;
import com.llw.goodweather.MainActivity;
import com.llw.goodweather.utils.Constant;
import com.llw.goodweather.utils.SPUtils;
import com.llw.goodweather.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class LoginActivity extends BaseBindingActivity<ActivityLoginBinding> {

    protected void initListener() {
        //点击去注册
        viewBinder.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisterActivity.class);
            }
        });
        //点击登录
        viewBinder.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    protected void initData() {
        StatusBarUtil.transparencyBar(this);//透明状态栏
        boolean auto = PreferenceUtil.getInstance().get("auto", false);
        if (auto) {
            String account = PreferenceUtil.getInstance().get("account", "");
            String password = PreferenceUtil.getInstance().get("password", "");
            viewBinder.phoneEdit.setText(account);
            viewBinder.pwdEdit.setText(password);
            viewBinder.cbAutoLogin.setChecked(true);
            login();
        }
        requestPermission();


    }


    private void requestPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(permission.CAMERA);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[0]);
            Log.d("TAG", "onCreate: " + Arrays.toString(permissions));
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    /**
     * 登陆方法
     */
    private void login() {
        String phone = viewBinder.phoneEdit.getText().toString().trim();
        String pwd = viewBinder.pwdEdit.getText().toString().trim();
        if (phone.isEmpty()) {
            viewBinder.phoneEdit.setError("请输入账号");
            return;
        }
        if (pwd.isEmpty()) {
            viewBinder.pwdEdit.setError("请输入密码");
            return;
        }
        User login = Database.getDao().queryUser(phone, pwd);
        if (login != null) {
            PreferenceUtil.getInstance().save("auto", true);
            PreferenceUtil.getInstance().save("account", phone);
            PreferenceUtil.getInstance().save("password", pwd);
            if (viewBinder.cbAutoLogin.isChecked()) {
                PreferenceUtil.getInstance().save("auto", true);
                PreferenceUtil.getInstance().save("account", phone);
                PreferenceUtil.getInstance().save("password", pwd);
            } else {
                PreferenceUtil.getInstance().remove("auto");
                PreferenceUtil.getInstance().remove("account");
                PreferenceUtil.getInstance().remove("password");
            }
            App.user = login;
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        } else {
            Toast.makeText(this, "账号或密码错误请重新输入", Toast.LENGTH_SHORT).show();
        }
    }


}

