package com.example.tqapp.ui.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tqapp.R;
import com.example.tqapp.common.BaseActivity;
import com.example.tqapp.common.bean.Music;
import com.example.tqapp.databinding.ActivityPlayBinding;

import java.io.IOException;
import java.util.List;

public class PlayActivity extends BaseActivity<ActivityPlayBinding> {
    private MediaPlayer player;
    private List<Music> musicList;
    private int index;
    private boolean play = true;
    private ActivityPlayBinding viewBinding;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (isDestroyed()){
                return;
            }
            viewBinding.seekbar.setMax(player.getDuration());
            viewBinding.seekbar.setProgress(player.getCurrentPosition());
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    };

    @Override
    protected void initUI(ActivityPlayBinding viewBinding) {
        this.viewBinding = viewBinding;
        getWindow().setStatusBarColor(Color.parseColor("#f6f6f6"));
        musicList = getIntent().getParcelableArrayListExtra("music");
        index = getIntent().getIntExtra("index", 0);
        player = new MediaPlayer();
        change();
        handler.sendEmptyMessageDelayed(1, 1000);
        viewBinding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index++;
                if (index > musicList.size() - 1) {
                    index = 0;
                }
                change();
            }
        });
        viewBinding.tvUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index--;
                if (index < 0) {
                    index = musicList.size() - 1;
                }
                change();
            }
        });
        viewBinding.tvPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player == null) return;
                play = !play;
                if (play) {
                    viewBinding.tvPlay.setImageResource(R.drawable.ic_baseline_pause_24);
                    player.start();
                } else {
                    viewBinding.tvPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    player.pause();
                }
            }
        });
        viewBinding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void change() {
        Music music = musicList.get(index);
        viewBinding.tvAuthor.setText(music.getSinger());
        viewBinding.tvName.setText(music.getName());
        Glide.with(this)
                .asBitmap()
                .load(music.getImage()).into(new BitmapImageViewTarget(viewBinding.ivImage) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);
                        Palette generate = Palette.from(resource).generate();
                        int dominantColor = generate.getDominantColor(Color.parseColor("#f6f6f6"));
                        getWindow().setStatusBarColor(dominantColor);
                        viewBinding.getRoot().setBackgroundColor(dominantColor);
                    }
                });
        try {
            player.reset();
            player.setDataSource(music.getUrl());
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ActivityPlayBinding getViewBinding(LayoutInflater inflater) {
        return ActivityPlayBinding.inflate(inflater);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.pause();
            player.release();
            player = null;
        }
    }
}