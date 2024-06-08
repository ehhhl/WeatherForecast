package com.example.tqapp.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.tqapp.common.BaseBindingFragment;
import com.example.tqapp.common.BindAdapter;
import com.example.tqapp.common.bean.Music;
import com.example.tqapp.databinding.FragmentMusicBinding;
import com.example.tqapp.databinding.ItemMusicBinding;
import com.example.tqapp.ui.activity.PlayActivity;

import java.util.ArrayList;

public class MusicFragment extends BaseBindingFragment<FragmentMusicBinding> {
    private BindAdapter<ItemMusicBinding, Music> adapter = new BindAdapter<ItemMusicBinding, Music>() {
        @Override
        public ItemMusicBinding createHolder(ViewGroup parent) {
            return ItemMusicBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemMusicBinding item, Music data, int position) {
            item.tvName.setText(data.getName());
            item.tvSign.setText(data.getSinger());
            Glide.with(item.getRoot().getContext()).load(data.getImage()).into(item.ivImage);
            item.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MusicFragment.this.requireContext(), PlayActivity.class);
                    intent.putParcelableArrayListExtra("music", new ArrayList<>(getData()));
                    startActivity(intent);
                }
            });

        }
    };


    @Override
    protected void initData() {
        String baseUrl = "http://124.220.35.123:8911/common/upload/";
        adapter.getData().add(new Music(baseUrl + "mus1.mp3", "张杰", "https://bkimg.cdn.bcebos.com/pic/bba1cd11728b4710b912c50f6298d4fdfc03934585e0?x-bce-process=image/format,f_auto/quality,Q_70/resize,m_lfit,limit_1,w_536", "明天过后"));
        adapter.getData().add(new Music(baseUrl + "music2.m4a", "张杰", "https://bkimg.cdn.bcebos.com/pic/0d338744ebf81a4c7d23c74fd52a6059252da626?x-bce-process=image/format,f_auto/resize,m_lfit,limit_1,w_440", "逆战"));

        viewBinder.rvRecord.setAdapter(adapter);

    }

    @Override
    protected void initListener() {

    }

}
