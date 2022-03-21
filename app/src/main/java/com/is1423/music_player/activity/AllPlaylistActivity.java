package com.is1423.music_player.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.is1423.music_player.R;
import com.is1423.music_player.adapter.AllPlaylistAdapter;
import com.is1423.music_player.adapter.MyAllPlaylistAdapter;
import com.is1423.music_player.model.request.PlaylistRequestDTO;
import com.is1423.music_player.model.response.PlaylistResponseDTO;
import com.is1423.music_player.service.DataServicePlaylist;
import com.is1423.music_player.service.intagration.ApiServicePlaylist;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPlaylistActivity extends AppCompatActivity {

    private Toolbar toolbarAllPlaylist;
    private RecyclerView recyclerViewAllPlaylist;
    private List<PlaylistResponseDTO> playlists;
    private AllPlaylistAdapter adapter;
    private List<PlaylistResponseDTO> myPlaylists;
    private MyAllPlaylistAdapter myAllPlaylistAdapter;
    private SharedPreferences sharedPreferences;
    private String userId;
    private PlaylistRequestDTO playlistRequestDTO;

    private EditText edtPlaylistName;
    private Button btnConfirmRename;
    private ImageView btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_playlist);
        bindingView();
        bindingAction();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("AppAllPlaylist")) {
                fetchData();
            }
            if (intent.hasExtra("MyAllPlaylist")) {
                fetchDataToMyPlaylist();
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = ((MyAllPlaylistAdapter) recyclerViewAllPlaylist.getAdapter()).getPosition();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.context_rename:
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.style_rename_playlist);
                dialog.show();
                bindingViewDialog(dialog);
                playlistRequestDTO = new PlaylistRequestDTO();
                playlistRequestDTO.setPlaylistId(myPlaylists.get(position).getPlaylistId());
                playlistRequestDTO.setPlaylistImage(myPlaylists.get(position).getPlaylistImage());
                playlistRequestDTO.setPlaylistIcon(myPlaylists.get(position).getPlaylistIcon());

                bindingActionDialog(dialog);

                break;
            case R.id.context_delete:
                playlistRequestDTO = new PlaylistRequestDTO();
                playlistRequestDTO.setPlaylistId(myPlaylists.get(position).getPlaylistId());
                deletePlaylist(playlistRequestDTO);
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void deletePlaylist(PlaylistRequestDTO request){

        DataServicePlaylist servicePlaylist = ApiServicePlaylist.getService();
        Call<Void> callback = servicePlaylist.deleteMyPlaylist(request.getPlaylistId(), userId);
        callback.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 204){
                    Toast.makeText(AllPlaylistActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(AllPlaylistActivity.this, AllPlaylistActivity.class);
                    intent.putExtra("MyAllPlaylist", "");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void bindingActionDialog(Dialog dialog) {
        btnClose.setOnClickListener(view -> dialog.dismiss());
        btnConfirmRename.setOnClickListener(this::eventClickBtnRename);
    }

    private void eventClickBtnRename(View view) {
        String sPlaylistName = edtPlaylistName.getText().toString();
        if (sPlaylistName.isEmpty()) {
            Toast.makeText(this, "Tên không được để trống", Toast.LENGTH_SHORT).show();
        } else {
            if (isDuplicatePlaylistName(sPlaylistName)){
                Toast.makeText(this, "Playlist " + sPlaylistName + " đã tồn tại", Toast.LENGTH_SHORT).show();
            }else{
                fetchDataUpdatePlaylist(playlistRequestDTO, sPlaylistName);
                this.finish();
            }
        }
    }

    private void fetchDataUpdatePlaylist(PlaylistRequestDTO request, String replacement){
        playlistRequestDTO.setPlaylistName(replacement);
        DataServicePlaylist servicePlaylist = ApiServicePlaylist.getService();
        Call<PlaylistResponseDTO> callback = servicePlaylist.updateMyPlaylist(request, userId);
        callback.enqueue(new Callback<PlaylistResponseDTO>() {
            @Override
            public void onResponse(Call<PlaylistResponseDTO> call, Response<PlaylistResponseDTO> response) {
                if(response.code() == 200){
                    Toast.makeText(AllPlaylistActivity.this, "Đổi tên thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AllPlaylistActivity.this, AllPlaylistActivity.class);
                    intent.putExtra("MyAllPlaylist", "");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<PlaylistResponseDTO> call, Throwable t) {

            }
        });
    }

    private boolean isDuplicatePlaylistName(String playlistName) {
        for (PlaylistResponseDTO playlist : myPlaylists) {
            if(playlist.getPlaylistName().equalsIgnoreCase(playlistName)){
                return true;
            }
        }
        return false;
    }

    private void bindingViewDialog(Dialog dialog) {
        btnClose = dialog.findViewById(R.id.btnClose);
        btnConfirmRename = dialog.findViewById(R.id.btnConfirmRename);
        edtPlaylistName = dialog.findViewById(R.id.edtPlaylistName);
    }


//    private void eventClickCloseDialogButton(Dialog dialog) {
//        ImageView ivClose = dialog.findViewById(R.id.btnClose);
//        ivClose.setOnClickListener(view -> dialog.dismiss());
//        dialog.show();
//    }

    private void fetchDataToMyPlaylist() {
        DataServicePlaylist dataServicePlaylist = ApiServicePlaylist.getService();
        Call<List<PlaylistResponseDTO>> callBack = dataServicePlaylist.getAllPlaylists(userId);
        callBack.enqueue(new Callback<List<PlaylistResponseDTO>>() {
            @Override
            public void onResponse(Call<List<PlaylistResponseDTO>> call, Response<List<PlaylistResponseDTO>> response) {
                myPlaylists = response.body();

                myAllPlaylistAdapter = new MyAllPlaylistAdapter(AllPlaylistActivity.this, myPlaylists);
                recyclerViewAllPlaylist.setLayoutManager(new GridLayoutManager
                        (AllPlaylistActivity.this, 2));
                recyclerViewAllPlaylist.setAdapter(myAllPlaylistAdapter);
            }

            @Override
            public void onFailure(Call<List<PlaylistResponseDTO>> call, Throwable t) {

            }
        });
    }

    private void fetchData() {
        DataServicePlaylist dataServicePlaylist = ApiServicePlaylist.getService();

        Call<List<PlaylistResponseDTO>> callBack = dataServicePlaylist.getAllPlaylists(null);
        callBack.enqueue(new Callback<List<PlaylistResponseDTO>>() {
            @Override
            public void onResponse(Call<List<PlaylistResponseDTO>> call, Response<List<PlaylistResponseDTO>> response) {
                playlists = response.body();

                adapter = new AllPlaylistAdapter(AllPlaylistActivity.this, playlists);
                recyclerViewAllPlaylist.setLayoutManager(new GridLayoutManager
                        (AllPlaylistActivity.this, 2));
                recyclerViewAllPlaylist.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<PlaylistResponseDTO>> call, Throwable t) {

            }
        });
    }

    private void bindingAction() {
        setSupportActionBar(toolbarAllPlaylist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Playlists");
        toolbarAllPlaylist.setTitleTextColor(getResources().getColor(R.color.purple_500));
        toolbarAllPlaylist.setNavigationOnClickListener(view -> finish());
    }

    private void bindingView() {
        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", null);
        toolbarAllPlaylist = findViewById(R.id.toolBarAllPlaylist);
        recyclerViewAllPlaylist = findViewById(R.id.recyclerAllPlaylist);
    }
}