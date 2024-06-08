package com.example.tqapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.tqapp.common.App;
import com.example.tqapp.common.BaseBindingActivity;
import com.example.tqapp.common.bean.History;
import com.example.tqapp.common.database.Database;
import com.example.tqapp.databinding.ActivityWeatherBinding;
import com.example.tqapp.ui.fragment.WeatherFragment;

public class WeatherActivity extends BaseBindingActivity<ActivityWeatherBinding> {

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        WeatherFragment fragment = new WeatherFragment();
        String city = getIntent().getStringExtra("city");
        String provinces = getIntent().getStringExtra("provinces");
        viewBinder.tvTitle.setText(provinces + "-" + city);
        Bundle args = new Bundle();
        args.putString("city", city);
        args.putString("provinces", provinces);
        fragment.setArguments(args);
        fragmentTransaction.add(R.id.root, fragment).show(fragment).commitNow();
        History history = Database.getDao().getMyHistory(App.user.account, provinces, city);
        if (history == null) {
            new AlertDialog.Builder(this)
                    .setTitle("注意")
                    .setMessage("是否将此城市保存到天气界面")
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            History history = new History();
                            history.provinces = provinces;
                            history.city = city;
                            history.account = App.user.account;
                            Database.getDao().addHistory(history);
                        }
                    }).setPositiveButton("不用了，谢谢",null).show();
        }
    }
}