package com.is1423.music_player.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.is1423.music_player.R;
import com.is1423.music_player.activity.ListSongActivity;
import com.is1423.music_player.model.response.AdvertisementResponseDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerAdapter extends PagerAdapter {

    private Context context;
    private List<AdvertisementResponseDTO> dtoList;

    public BannerAdapter(Context context, List<AdvertisementResponseDTO> dtoList) {
        this.context = context;
        this.dtoList = dtoList;
    }

    @Override
    public int getCount() {
        return dtoList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view == object;
    }

    //dinh hinh va gan du lieu cho object
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.line_banner,null);

        ImageView imageViewBackgroundBanner = view.findViewById(R.id.imageViewBackgroundBanner);
        ImageView imageViewSongBanner = view.findViewById(R.id.imageViewSongBanner);
        TextView txtTitleSongBanner = view.findViewById(R.id.txtTitleSongBanner);
        TextView txtContentSongBanner = view.findViewById(R.id.txtContentSongBanner);

        Picasso.with(context).load(dtoList.get(position).getAdvertisementImage()).into(imageViewBackgroundBanner);
        Picasso.with(context).load(dtoList.get(position).getSongImage()).into(imageViewSongBanner);

        txtTitleSongBanner.setText(dtoList.get(position).getSongName());
        txtContentSongBanner.setText(dtoList.get(position).getAdvertisementContent());

        view.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, ListSongActivity.class);
            intent.putExtra("banner",dtoList.get(position));
            context.startActivity(intent);
        });

        container.addView(view);
        return view;
    }


    //huy neu Pager scroll den cuoi
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
