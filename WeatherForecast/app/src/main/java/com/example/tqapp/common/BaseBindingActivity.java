package com.example.tqapp.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.example.tqapp.R;
import com.llw.goodweather.utils.Constant;
import com.llw.goodweather.utils.SPUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class BaseBindingActivity<T extends ViewBinding> extends AppCompatActivity {
    protected T viewBinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Type type = getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        Class<T> tClass = (Class<T>) actualTypeArguments[0];
        try {
            Method method = tClass.getMethod("inflate", LayoutInflater.class);
            viewBinder = (T) method.invoke(null, getLayoutInflater());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        setContentView(viewBinder.getRoot());
        initData();
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWallpaper();
    }

    private void updateWallpaper() {
        ImageView imageView = findViewById(R.id.bg1);
        if (imageView != null) {
            String imgUrl = SPUtils.getString(Constant.WALLPAPER_URL, null, this);
            Glide.with(this).load(imgUrl != null ? imgUrl : com.llw.goodweather.R.drawable.img_5).into(imageView);
        }

    }

    /**
     * 页面跳转
     *
     * @param tClass
     * @param <T>
     */
    public <T extends Activity> void startActivity(Class<T> tClass) {
        Intent intent = new Intent(this, tClass);
        startActivity(intent);
    }

    protected abstract void initListener();

    protected abstract void initData();

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
