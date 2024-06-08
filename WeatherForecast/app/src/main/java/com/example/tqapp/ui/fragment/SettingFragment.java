package com.example.tqapp.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tqapp.common.App;
import com.example.tqapp.common.BaseBindingFragment;
import com.example.tqapp.common.PreferenceUtil;
import com.example.tqapp.common.bean.History;
import com.example.tqapp.common.database.Database;
import com.example.tqapp.databinding.FragmentSettingBinding;
import com.example.tqapp.ui.activity.LoginActivity;
import com.example.tqapp.ui.activity.UpdatePasswordActivity;
import com.github.gzuliyujiang.wheelpicker.AddressPicker;
import com.github.gzuliyujiang.wheelpicker.annotation.AddressMode;
import com.github.gzuliyujiang.wheelpicker.contract.OnAddressPickedListener;
import com.github.gzuliyujiang.wheelpicker.entity.CityEntity;
import com.github.gzuliyujiang.wheelpicker.entity.CountyEntity;
import com.github.gzuliyujiang.wheelpicker.entity.ProvinceEntity;

import org.greenrobot.eventbus.EventBus;

public class SettingFragment extends BaseBindingFragment<FragmentSettingBinding> {


    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {


    }

    @Override
    protected void initListener() {
        viewBinder.tvCity.setText(App.user.account);
        viewBinder.tvNicknname.setText(App.user.name);
        viewBinder.llOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.getInstance().remove("auto");
                PreferenceUtil.getInstance().remove("account");
                PreferenceUtil.getInstance().remove("password");
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        viewBinder.llEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
            }
        });

    }
}
