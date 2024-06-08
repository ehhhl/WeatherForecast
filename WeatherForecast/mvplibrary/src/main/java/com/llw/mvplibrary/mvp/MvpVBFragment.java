package com.llw.mvplibrary.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.viewbinding.ViewBinding;

import com.llw.mvplibrary.base.BasePresenter;
import com.llw.mvplibrary.base.BaseView;
import com.llw.mvplibrary.base.vb.BaseVBActivity;
import com.llw.mvplibrary.base.vb.BaseVBFragment;

/**
 * 适用于需要访问网络接口的Activity
 *
 * @author llw
 */

public abstract class MvpVBFragment<T extends ViewBinding, P extends BasePresenter> extends BaseVBFragment<T> {

    protected P mPresent;
    protected Activity context;

    @Override
    protected void initData() {
        context = requireActivity();
        mPresent = createPresent();
        mPresent.attach((BaseView) this);
    }

    protected abstract P createPresent();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresent.detach((BaseView) this);
    }

}
