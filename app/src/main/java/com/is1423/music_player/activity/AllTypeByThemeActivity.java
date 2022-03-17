package com.is1423.music_player.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.is1423.music_player.R;
import com.is1423.music_player.adapter.AllTypeByThemeAdapter;
import com.is1423.music_player.model.response.ThemeResponseDTO;
import com.is1423.music_player.model.response.TypeResponseDTO;
import com.is1423.music_player.service.DataServiceType;
import com.is1423.music_player.service.intagration.ApiServiceType;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllTypeByThemeActivity extends AppCompatActivity {

    private ThemeResponseDTO theme;
    private RecyclerView recyclerAllTypeByTheme;
    private Toolbar toolbarAllTypeByTheme;
    private AllTypeByThemeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_type_by_theme);
        getDataFromIntent();
        bindingView();
        bindingAction();
        fetchData(theme.getThemeId());
    }

    private void fetchData(Long themeId) {
        DataServiceType serviceType = ApiServiceType.getService();
        Call<List<TypeResponseDTO>> callBack = serviceType.getTypesByThemeId(themeId);
        callBack.enqueue(new Callback<List<TypeResponseDTO>>() {
            @Override
            public void onResponse(Call<List<TypeResponseDTO>> call, Response<List<TypeResponseDTO>> response) {
                List<TypeResponseDTO> types = response.body();

                adapter = new AllTypeByThemeAdapter(AllTypeByThemeActivity.this, types);
                recyclerAllTypeByTheme.setLayoutManager(new GridLayoutManager(
                        AllTypeByThemeActivity.this,2));
                recyclerAllTypeByTheme.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<TypeResponseDTO>> call, Throwable t) {

            }
        });
    }

    private void bindingAction() {
        toolbarAllTypeByTheme.setNavigationOnClickListener(view -> finish());
    }

    private void bindingView() {
        recyclerAllTypeByTheme = findViewById(R.id.recyclerAllTypeByTheme);
        toolbarAllTypeByTheme = findViewById(R.id.toolBarAllTypeByTheme);
        setSupportActionBar(toolbarAllTypeByTheme);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(theme.getThemeName());
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra("theme")){
            theme = (ThemeResponseDTO) intent.getSerializableExtra("theme");
        }
    }
}