package com.example.tqapp.ui.fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.tqapp.common.BaseBindingFragment;
import com.example.tqapp.common.BindAdapter;
import com.example.tqapp.databinding.FragmentWeatherBinding;
import com.example.tqapp.databinding.ItemDetailBinding;
import com.example.tqapp.databinding.ItemFutureBinding;
import com.google.gson.Gson;
import com.qweather.sdk.bean.air.AirNowBean;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.base.IndicesType;
import com.qweather.sdk.bean.base.Lang;
import com.qweather.sdk.bean.base.Unit;
import com.qweather.sdk.bean.indices.IndicesBean;
import com.qweather.sdk.bean.weather.WeatherDailyBean;
import com.qweather.sdk.bean.weather.WeatherHourlyBean;
import com.qweather.sdk.bean.weather.WeatherNowBean;
import com.qweather.sdk.view.HeConfig;
import com.qweather.sdk.view.QWeather;

import java.util.Collections;

public class WeatherFragment extends BaseBindingFragment<FragmentWeatherBinding> {

    private BindAdapter<ItemDetailBinding, IndicesBean.DailyBean> adapter = new BindAdapter<ItemDetailBinding, IndicesBean.DailyBean>() {
        @Override
        public ItemDetailBinding createHolder(ViewGroup parent) {
            return ItemDetailBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemDetailBinding item, IndicesBean.DailyBean data, int position) {
            item.tvName.setText(data.getName());
            item.tvValue.setText(data.getCategory());
            item.tvInfo.setText("解析" + data.getText());
        }
    };
    GeoCoder mCoder;

    @Override
    protected void initData() {
        viewBinder.tvFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView view = new RecyclerView(requireContext());
                view.setAdapter(dailyBindAdapter);
                view.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(requireContext()));
                new AlertDialog.Builder(requireContext())
                        .setView(view)
                        .show();
            }
        });
        viewBinder.tvHourly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView view = new RecyclerView(requireContext());
                view.setAdapter(hourlyAdapter);
                view.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(requireContext()));
                new AlertDialog.Builder(requireContext())
                        .setView(view)
                        .show();
            }
        });
        Bundle arguments = getArguments();
        if (arguments != null) {
            viewBinder.rvLife.setAdapter(adapter);
            String city = arguments.getString("city");
            String provinces = arguments.getString("provinces");
            mCoder = GeoCoder.newInstance();
            OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
                @Override
                public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                    if (null != geoCodeResult && null != geoCodeResult.getLocation()) {
                        if (geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                            //没有检索到结果
                            Log.d("TAG", "没结果: ");
                            Toast.makeText(requireContext(), "无法获取地址!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            double latitude = geoCodeResult.getLocation().latitude;
                            double longitude = geoCodeResult.getLocation().longitude;
                            Log.d("TAG", "onGetGeoCodeResult: " + latitude);
                            Log.d("TAG", "onGetGeoCodeResult: " + longitude);
                            HeConfig.switchToBizService();
                            String latlng = longitude + "," + latitude;
                            getAirNow(latlng);
                            getWeatherNow(latlng);
                            getIndices(latlng);
                            getFeature(latlng);
                            getHourly(latlng);
                        }
                    }
                }

                @Override
                public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                }
            };
            if (city == null|| provinces == null){
                return;
            }
            mCoder.setOnGetGeoCodeResultListener(listener);
            mCoder.geocode(new GeoCodeOption()
                    .city(city)
                    .address(provinces + city));
        }
    }

    private void getHourly(String latlng) {
        QWeather.getWeather24Hourly(requireContext(), latlng, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherHourlyListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d("TAG", "onError: " + throwable.getMessage());
            }

            @Override
            public void onSuccess(WeatherHourlyBean weatherHourlyBean) {
                hourlyAdapter.getData().clear();
                hourlyAdapter.getData().addAll(weatherHourlyBean.getHourly());
            }
        });
    }
    private BindAdapter<ItemFutureBinding, WeatherHourlyBean.HourlyBean> hourlyAdapter = new BindAdapter<ItemFutureBinding, WeatherHourlyBean.HourlyBean>() {
        @Override
        public ItemFutureBinding createHolder(ViewGroup parent) {
            return ItemFutureBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemFutureBinding item, WeatherHourlyBean.HourlyBean data, int position) {
            item.tvTime.setText(data.getFxTime());
            item.tvWeather.setText(data.getText());
            item.tvTemp.setText(data.getTemp());
            item.ivIcon.setImageResource(
                    getResources().getIdentifier("ic_" + data.getIcon(), "drawable", requireContext().getPackageName()));
        }
    };
    private BindAdapter<ItemFutureBinding, WeatherDailyBean.DailyBean> dailyBindAdapter = new BindAdapter<ItemFutureBinding, WeatherDailyBean.DailyBean>() {
        @Override
        public ItemFutureBinding createHolder(ViewGroup parent) {
            return ItemFutureBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemFutureBinding item, WeatherDailyBean.DailyBean data, int position) {
            item.tvTime.setText(data.getFxDate());
            item.tvWeather.setText(data.getTextDay());
            item.tvTemp.setText(data.getTempMax() + "~" + data.getTempMin());
            item.ivIcon.setImageResource(
                    getResources().getIdentifier("ic_" + data.getIconDay(), "drawable", requireContext().getPackageName()));
        }
    };

    private void getFeature(String latlng) {
        QWeather.getWeather7D(requireContext(), latlng, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherDailyListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(WeatherDailyBean weatherDailyBean) {
                Log.i("TAG", "getIndices onSuccess: " + new Gson().toJson(weatherDailyBean));
                if (Code.OK == weatherDailyBean.getCode()) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            dailyBindAdapter.getData().clear();
                            dailyBindAdapter.getData().addAll(weatherDailyBean.getDaily());
                            dailyBindAdapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    //在此查看返回数据失败的原因
                    Code code = weatherDailyBean.getCode();
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });
    }

    private void getIndices(String latlng) {
        QWeather.getIndices1D(requireContext(), latlng, Lang.ZH_HANS, Collections.singletonList(IndicesType.ALL), new QWeather.OnResultIndicesListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i("TAG", "getWeather onError: " + throwable.getLocalizedMessage());
            }

            @Override
            public void onSuccess(IndicesBean indicesBean) {
                Log.i("TAG", "getIndices onSuccess: " + new Gson().toJson(indicesBean));
                if (Code.OK == indicesBean.getCode()) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.getData().clear();
                            adapter.getData().addAll(indicesBean.getDailyList());
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else {
                    //在此查看返回数据失败的原因
                    Code code = indicesBean.getCode();
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });
    }

    private void getAirNow(String latlng) {
        QWeather.getAirNow(requireContext(), latlng, Lang.ZH_HANS, new QWeather.OnResultAirNowListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i("TAG", "getWeather onError: " + throwable.getLocalizedMessage());
            }

            @Override
            public void onSuccess(AirNowBean airNowBean) {
                Log.i("TAG", "getAirNow onSuccess: " + new Gson().toJson(airNowBean));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK == airNowBean.getCode()) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            AirNowBean.NowBean now = airNowBean.getNow();
                            viewBinder.tvAqi.setText(now.getAqi());
                            viewBinder.tvCategory.setText(now.getCategory());
                            viewBinder.tvPm25.setText(now.getPm2p5());
                            viewBinder.tvPm10.setText(now.getPm10());
                            viewBinder.tvNo2.setText(now.getNo2());
                            viewBinder.tvSo2.setText(now.getSo2());
                            viewBinder.tvCo.setText(now.getCo());
                            viewBinder.tvO3.setText(now.getO3());
                        }
                    });
                } else {
                    //在此查看返回数据失败的原因
                    Code code = airNowBean.getCode();
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });
    }

    private void getWeatherNow(String latlng) {
        QWeather.getWeatherNow(requireContext(), latlng, Lang.ZH_HANS, Unit.METRIC, new QWeather.OnResultWeatherNowListener() {
            @Override
            public void onError(Throwable e) {
                Log.i("TAG", "getWeather onError: " + e);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(WeatherNowBean weatherBean) {
                Log.i("TAG", "getWeather onSuccess: " + new Gson().toJson(weatherBean));
                //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                if (Code.OK == weatherBean.getCode()) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            WeatherNowBean.NowBaseBean now = weatherBean.getNow();
                            viewBinder.tvName.setText(now.getText());
                            viewBinder.tvValue.setText(now.getTemp());
                            viewBinder.tvMax.setText(now.getFeelsLike() + "℃");
                            viewBinder.tvHumidity.setText(now.getHumidity());
//                                                viewBinder.tvSunDown.setText(now.getHumidity());
                            viewBinder.tvWineLevel.setText(now.getWindSpeed() + "公里/小时");
                            viewBinder.tvWindDirection.setText(now.getWindDir());
                            viewBinder.ivIcon.setImageResource(
                                    getResources().getIdentifier("ic_" + now.getIcon(), "drawable", requireContext().getPackageName())
                            );
                        }
                    });
                } else {
                    //在此查看返回数据失败的原因
                    Code code = weatherBean.getCode();
                    Log.i(TAG, "failed code: " + code);
                }
            }
        });
    }

    @Override
    protected void initListener() {

    }
}
