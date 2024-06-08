package com.example.tqapp.common;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    private T viewBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = getViewBinding(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        initUI(viewBinding);

    }

    protected abstract void initUI(T viewBinding);

    protected abstract T getViewBinding(LayoutInflater inflater);

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    protected void refresh() {

    }
}
