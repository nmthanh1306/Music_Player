package com.is1423.music_player.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.is1423.music_player.R;
import com.is1423.music_player.adapter.AllThemeAdapter;
import com.is1423.music_player.model.response.ThemeResponseDTO;
import com.is1423.music_player.service.DataServiceTheme;
import com.is1423.music_player.service.intagration.ApiServiceTheme;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllThemeActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAllTheme;
    private Toolbar toolbarAllTheme;
    private AllThemeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_theme);
        bindingView();
        bindingAction();
        fetchData();
    }

    private void fetchData() {
        DataServiceTheme serviceTheme = ApiServiceTheme.getService();
        Call<List<ThemeResponseDTO>> callBack = serviceTheme.getAllThemes();
        callBack.enqueue(new Callback<List<ThemeResponseDTO>>() {
            @Override
            public void onResponse(Call<List<ThemeResponseDTO>> call, Response<List<ThemeResponseDTO>> response) {
                List<ThemeResponseDTO> themes = response.body();
                //Log.d("Theme",themes.get(0).getThemeName());
                adapter = new AllThemeAdapter(AllThemeActivity.this,themes);
                recyclerViewAllTheme.setLayoutManager(new GridLayoutManager(AllThemeActivity.this,1));
                recyclerViewAllTheme.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ThemeResponseDTO>> call, Throwable t) {

            }
        });
    }

    private void bindingAction() {
        // click to back arrow to back activity
        toolbarAllTheme.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void bindingView() {
        recyclerViewAllTheme = findViewById(R.id.recyclerAllTheme);
        toolbarAllTheme = findViewById(R.id.toolBarAllTheme);
        setSupportActionBar(toolbarAllTheme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tất Cả Chủ Đề");

    }
}