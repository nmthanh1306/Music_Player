package com.is1423.music_player.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.is1423.music_player.R;
import com.is1423.music_player.activity.AllTypeByThemeActivity;
import com.is1423.music_player.model.response.ThemeResponseDTO;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AllThemeAdapter extends RecyclerView.Adapter<AllThemeAdapter.ViewHolder> {

    private Context context;
    private List<ThemeResponseDTO> themes;

    public AllThemeAdapter(Context context, List<ThemeResponseDTO> themes) {
        this.context = context;
        this.themes = themes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.line_all_theme, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThemeResponseDTO theme = themes.get(position);
        Picasso.with(context).load(theme.getThemeImage()).into(holder.ivLineThemes);
    }

    @Override
    public int getItemCount() {
        return themes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivLineThemes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLineThemes = itemView.findViewById(R.id.ivLineThemes);
            ivLineThemes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AllTypeByThemeActivity.class);
                    intent.putExtra("theme",themes.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
