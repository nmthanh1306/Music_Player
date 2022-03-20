package com.is1423.music_player.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.is1423.music_player.R;
import com.is1423.music_player.adapter.SearchAdapter;
import com.is1423.music_player.model.response.SongResponseDTO;
import com.is1423.music_player.service.DataServiceSong;
import com.is1423.music_player.service.intagration.ApiServiceSong;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private View view;
    private Toolbar toolbarSearch;
    private RecyclerView recyclerViewSearch;
    private TextView tvNoDataSearch;
    private SearchAdapter adapter;

    private SharedPreferences sharedPreferences;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        bindingView();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            onResume();
        }
    }

    private void bindingView() {
        sharedPreferences = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        toolbarSearch = view.findViewById(R.id.toolBarSearch);
        recyclerViewSearch = view.findViewById(R.id.recyclerSearch);
        tvNoDataSearch = view.findViewById(R.id.tvNoDataSearch);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarSearch);
        toolbarSearch.setTitle("");
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_view, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
                searchSongByNameOrSinger(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchSongByNameOrSinger(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchSongByNameOrSinger(String fulltext) {
        DataServiceSong serviceSong = ApiServiceSong.getService();
        String userId = sharedPreferences.getString("userId", null);
        Call<List<SongResponseDTO>> callback = serviceSong.searchSongByNameOrSinger(fulltext, userId);
        callback.enqueue(new Callback<List<SongResponseDTO>>() {
            @Override
            public void onResponse(Call<List<SongResponseDTO>> call, Response<List<SongResponseDTO>> response) {
                List<SongResponseDTO> songs = response.body();
                if (songs.size() > 0) {
                    adapter = new SearchAdapter(getActivity(), songs);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    recyclerViewSearch.setLayoutManager(layoutManager);
                    recyclerViewSearch.setAdapter(adapter);
                    tvNoDataSearch.setVisibility(View.GONE);
                    recyclerViewSearch.setVisibility(View.VISIBLE);
                } else {
                    recyclerViewSearch.setVisibility(View.GONE);
                    tvNoDataSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<SongResponseDTO>> call, Throwable t) {

            }
        });
    }
}