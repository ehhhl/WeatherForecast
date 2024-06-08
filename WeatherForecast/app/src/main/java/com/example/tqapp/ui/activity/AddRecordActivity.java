package com.example.tqapp.ui.activity;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;

import com.example.tqapp.common.App;
import com.example.tqapp.common.BaseBindingActivity;
import com.example.tqapp.common.bean.Record;
import com.example.tqapp.common.database.Database;
import com.example.tqapp.databinding.ActivityAddRecordBinding;

import java.util.Calendar;

public class AddRecordActivity extends BaseBindingActivity<ActivityAddRecordBinding> {


    @Override
    protected void initListener() {

    }


    Record data;

    @Override
    protected void initData() {
        data = (Record) getIntent().getSerializableExtra("data");
        if (data != null) {
            viewBinder.etTitle.setText(data.title);
            viewBinder.etContent.setText(data.content);
            viewBinder.etDate.setText(data.time);
        }
        viewBinder.etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar instance = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(AddRecordActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        viewBinder.etDate.setText(String.format("%d-%02d-%02d", year, month + 1, dayOfMonth));
                    }
                }, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });
        // 设置提交按钮的点击事件
        viewBinder.tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入框中的数据
                String content = viewBinder.etContent.getText().toString();
                String title = viewBinder.etTitle.getText().toString();

                // 判断输入框是否为空
                if (title.isEmpty()) {
                    toast("请输入标题");
                    return;
                }
                if (content.isEmpty()) {
                    toast("请输入内容");
                    return;
                }
                if (viewBinder.etDate.getText().toString().equals("")){
                    toast("请选择日期");
                    return;
                }
                if (data != null) {
                    data.title = title;
                    data.content = content;
                    data.time = viewBinder.etDate.getText().toString();
                    Database.getDao().updateRecord(data);
                    // 提示记录成功，并结束当前Activity
                    toast("记录成功");
                    finish();
                    return;
                }
                // 创建Record对象，并设置属性
                Record record = new Record();
                record.userAccount = App.user.account;
                record.title = title;

                record.content = content;
                record.time = viewBinder.etDate.getText().toString();
                // 将Record对象添加到数据库中
                Database.getDao().addRecord(record);
                // 提示记录成功，并结束当前Activity
                toast("记录成功");
                finish();
            }

        });
    }
}