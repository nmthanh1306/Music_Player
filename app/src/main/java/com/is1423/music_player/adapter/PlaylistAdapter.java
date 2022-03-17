package com.is1423.music_player.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.is1423.music_player.R;
import com.is1423.music_player.model.response.PlaylistResponseDTO;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistAdapter extends ArrayAdapter<PlaylistResponseDTO> {

    public PlaylistAdapter(@NonNull Context context, int resource, @NonNull List<PlaylistResponseDTO> objects) {
        super(context, resource, objects);
    }

    class ViewHolder{
        TextView tvPlaylistName;
        ImageView ivBackgroundPlaylist;
        ImageView ivPlaylist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = null;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.line_playlist,null);

            viewHolder = new ViewHolder();
            viewHolder.tvPlaylistName = convertView.findViewById(R.id.tvPlaylistName);
            viewHolder.ivBackgroundPlaylist = convertView.findViewById(R.id.ivBackgroundPlaylist);
            viewHolder.ivPlaylist = convertView.findViewById(R.id.imageViewPlaylist);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PlaylistResponseDTO responseDTO = getItem(position);
        Picasso.with(getContext()).load(responseDTO.getPlaylistImage()).into(viewHolder.ivBackgroundPlaylist);
        Picasso.with(getContext()).load(responseDTO.getPlaylistIcon()).into(viewHolder.ivPlaylist);
        viewHolder.tvPlaylistName.setText(responseDTO.getPlayListName());
        return convertView;
    }
}
