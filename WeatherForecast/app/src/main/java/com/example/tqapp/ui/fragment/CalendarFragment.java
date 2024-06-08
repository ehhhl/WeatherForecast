package com.example.tqapp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.tqapp.common.App;
import com.example.tqapp.common.BaseBindingFragment;
import com.example.tqapp.common.BindAdapter;
import com.example.tqapp.common.bean.Record;
import com.example.tqapp.common.database.Database;
import com.example.tqapp.databinding.FragmentCalendarBinding;
import com.example.tqapp.databinding.ItemRecordBinding;
import com.example.tqapp.ui.activity.AddRecordActivity;
import com.google.android.material.tabs.TabLayout;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class CalendarFragment extends BaseBindingFragment<FragmentCalendarBinding> {
    private BindAdapter<ItemRecordBinding, Record> adapter = new BindAdapter<ItemRecordBinding, Record>() {
        @Override
        public ItemRecordBinding createHolder(ViewGroup parent) {
            return ItemRecordBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemRecordBinding item, Record data, int position) {
            item.tvTime.setText(data.time);
            item.tvName.setText(data.title);
            item.tvWight.setText(data.content);
            item.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CalendarFragment.this.requireContext(), AddRecordActivity.class);
                    intent.putExtra("data", data);
                    startActivity(intent);
                }
            });
            item.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Database.getDao().deleteRecord(data);
                    initListData();
                    return true;
                }
            });
        }
    };


    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {
        viewBinder.tabLayout.removeAllTabs();
        viewBinder.tabLayout.addTab(viewBinder.tabLayout.newTab().setText("今年"));
        viewBinder.tabLayout.addTab(viewBinder.tabLayout.newTab().setText("当月"));
        viewBinder.tabLayout.addTab(viewBinder.tabLayout.newTab().setText("本周"));
        viewBinder.tabLayout.addTab(viewBinder.tabLayout.newTab().setText("今日"));
        viewBinder.calendarView.setVisibility(View.GONE);
        viewBinder.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                adapter.getData().clear();
                viewBinder.calendarView.setVisibility(View.GONE);
                switch (tab.getPosition()) {
                    case 0:
                        String date = new SimpleDateFormat("yyyy", Locale.CHINA).format(new Date());
                        adapter.getData().addAll(Database.getDao().getRecord(App.user.account, "%" + date + "%"));
                        break;
                    case 1:
                        String date1 = new SimpleDateFormat("yyyy-MM", Locale.CHINA).format(new Date());
                        adapter.getData().addAll(Database.getDao().getRecord(App.user.account, "%" + date1 + "%"));
                        viewBinder.calendarView.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        java.util.Calendar instance = java.util.Calendar.getInstance();
                        instance.set(java.util.Calendar.DAY_OF_WEEK, 1);
                        String date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(instance.getTimeInMillis());
                        Log.d("TAG", "onTabSelected: "+date2);
                        adapter.getData().addAll(Database.getDao().getRecord(App.user.account, date2));
                        instance.set(java.util.Calendar.DAY_OF_WEEK, 2);
                         date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(instance.getTimeInMillis());
                        adapter.getData().addAll(Database.getDao().getRecord(App.user.account, date2));
                        Log.d("TAG", "onTabSelected: "+date2);
                        instance.set(java.util.Calendar.DAY_OF_WEEK, 3);
                         date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(instance.getTimeInMillis());

                        adapter.getData().addAll(Database.getDao().getRecord(App.user.account, date2));
                        Log.d("TAG", "onTabSelected: "+date2);
                        instance.set(java.util.Calendar.DAY_OF_WEEK, 4);
                         date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(instance.getTimeInMillis());

                        adapter.getData().addAll(Database.getDao().getRecord(App.user.account, date2));
                        Log.d("TAG", "onTabSelected: "+date2);
                        instance.set(java.util.Calendar.DAY_OF_WEEK, 5);
                        date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(instance.getTimeInMillis());
                        adapter.getData().addAll(Database.getDao().getRecord(App.user.account, date2));
                        Log.d("TAG", "onTabSelected: "+date2);
                        instance.set(java.util.Calendar.DAY_OF_WEEK, 6);
                        date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(instance.getTimeInMillis());
                        adapter.getData().addAll(Database.getDao().getRecord(App.user.account, date2));
                        Log.d("TAG", "onTabSelected: "+date2);
                        instance.set(java.util.Calendar.DAY_OF_WEEK, 7);
                        date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(instance.getTimeInMillis());
                        adapter.getData().addAll(Database.getDao().getRecord(App.user.account, date2));
                        List<Record> collect = adapter.getData().stream().distinct().collect(Collectors.toList());
                        adapter.getData().clear();
                        adapter.getData().addAll(collect);
                        break;
                    case 3:
                        date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date());
                        adapter.getData().addAll(Database.getDao().getRecord(App.user.account, date2));
                        break;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewBinder.tvMonth.setText(viewBinder.calendarView.getCurYear() + "-" + viewBinder.calendarView.getCurMonth() + "");
        viewBinder.calendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onMonthChange(int year, int month) {
                viewBinder.tvMonth.setText(year + "-" + month + "");
            }
        });

        viewBinder.rvRecord.setAdapter(adapter);
        initListData();
        initMapFlag();
        viewBinder.calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                if (isClick) {
                    change(calendar.getYear(), calendar.getMonth(), calendar.getDay());
                }
            }
        });
    }

    /**
     * 初始化记录列表数据
     * 获取当前日期并格式化
     * 清空适配器数据并添加当天的记录
     */
    private void initListData() {
        java.util.Calendar instance = java.util.Calendar.getInstance();
        int year = instance.get(java.util.Calendar.YEAR);
        int month = instance.get(java.util.Calendar.MONTH) + 1;
        int day = instance.get(java.util.Calendar.DAY_OF_MONTH);
        String date = String.format("%04d-%02d-%02d", year, month, day);
        Log.d("TAG", "change: " + date);
        adapter.getData().clear();
        adapter.getData().addAll(Database.getDao().getRecord(App.user.account, "%" + date + "%"));
        adapter.notifyDataSetChanged();
    }

    /**
     * 根据选择的日期改变记录列表数据
     * 格式化日期并查询当天的记录
     * 清空适配器数据并添加当天的记录
     */
    private void change(int year, int month, int day) {

        String date = String.format("%04d-%02d-%02d", year, month, day);
        Log.d("TAG", "change: " + date);
        adapter.getData().clear();
        adapter.getData().addAll(Database.getDao().getRecord(App.user.account, "%" + date + "%"));
        adapter.notifyDataSetChanged();
    }

    /**
     * 当该Fragment显示时，重新初始化数据
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initData();
        }
    }

    /**
     * 初始化监听器
     * 点击添加按钮跳转到添加记录页面
     */
    @Override
    protected void initListener() {
        viewBinder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), AddRecordActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initMapFlag();
    }

    /**
     * 初始化日历标记
     * 遍历所有记录，将记录的日期标记到日历上
     */
    private void initMapFlag() {
        Map<String, Calendar> map = new HashMap<>();
        //此方法在巨大的数据量上不影响遍历性能，推荐使用
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        List<Record> signs = Database.getDao().getRecord(App.user.account);
        for (Record item : signs) {
            java.util.Calendar instance = java.util.Calendar.getInstance();
            try {
                Date parse = dateFormat.parse(item.time);
                instance.setTime(parse);
                Log.d("TAG", "initMapFlag: " + dateFormat.format(parse));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int year = instance.get(java.util.Calendar.YEAR);
            int month = instance.get(java.util.Calendar.MONTH) + 1;
            int day = instance.get(java.util.Calendar.DAY_OF_MONTH);
            @SuppressLint("DefaultLocale") String date = String.format("%04d-%02d-%02d", year, month, day);
            Log.d("TAG", "initMapFlag: " + date + "---" + item.time);
            map.put(getSchemeCalendar(year, month, day, 0xFF000000, "·").toString(),
                    getSchemeCalendar(year, month, day, 0xFF000000, "·"));
        }
        viewBinder.calendarView.setSchemeDate(map);
    }

    /**
     * 获取标记日期的Calendar对象
     */
    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        return calendar;
    }
}
