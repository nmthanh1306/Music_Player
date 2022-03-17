package com.is1423.music_player.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.is1423.music_player.R;
import com.is1423.music_player.activity.ListSongActivity;
import com.is1423.music_player.model.response.TypeResponseDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllTypeByThemeAdapter extends RecyclerView.Adapter<AllTypeByThemeAdapter.ViewHolder> {

    private Context context;

    public AllTypeByThemeAdapter(Context context, List<TypeResponseDTO> types) {
        this.context = context;
        this.types = types;
    }

    private List<TypeResponseDTO> types;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.line_all_type_by_theme, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TypeResponseDTO type = types.get(position);
        Picasso.with(context).load(type.getTypeImage()).into(holder.ivTypeByTheme);
        holder.tvTypeNameByTheme.setText(type.getTypeName());
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTypeNameByTheme;
        private ImageView ivTypeByTheme;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTypeNameByTheme = itemView.findViewById(R.id.tvTypeNameByTheme);
            ivTypeByTheme = itemView.findViewById(R.id.ivTypeByTheme);
            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, ListSongActivity.class);
                intent.putExtra("typeId", types.get(getPosition()));
                context.startActivity(intent);
            });
        }
    }
}
