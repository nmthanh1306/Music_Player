package com.is1423.music_player.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.is1423.music_player.R;
import com.is1423.music_player.adapter.HotSongAdapter;
import com.is1423.music_player.model.response.SongResponseDTO;
import com.is1423.music_player.service.DataServiceSong;
import com.is1423.music_player.service.intagration.ApiServiceSong;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HotSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HotSongFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private View view;
    private RecyclerView recyclerViewHotSong;
    private HotSongAdapter hotSongAdapter;
    private String mParam1;
    private String mParam2;

    private SharedPreferences sharedPreferences;

    public HotSongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HotSongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HotSongFragment newInstance(String param1, String param2) {
        HotSongFragment fragment = new HotSongFragment();
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
        view = inflater.inflate(R.layout.fragment_hot_song, container, false);
        bindingView();
        getData();
        return view;
    }

    private void getData() {
        DataServiceSong serviceSong = ApiServiceSong.getService();
        String userId = sharedPreferences.getString("userId",null);
        Call<List<SongResponseDTO>> callBack = serviceSong.getTopFavouriteSongs(userId);
        callBack.enqueue(new Callback<List<SongResponseDTO>>() {
            @Override
            public void onResponse(Call<List<SongResponseDTO>> call, Response<List<SongResponseDTO>> response) {
                List<SongResponseDTO> hotSongs = response.body();
                Log.d("fav",userId + hotSongs.get(0).getSongName() + hotSongs.get(0).isUserFavourite());
                //Log.d("hot song", hotSongs.get(0).getSongName());
                hotSongAdapter = new HotSongAdapter(getActivity(), hotSongs);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerViewHotSong.setLayoutManager(layoutManager);
                recyclerViewHotSong.setAdapter(hotSongAdapter);
            }

            @Override
            public void onFailure(Call<List<SongResponseDTO>> call, Throwable t) {

            }
        });
    }

    private void bindingView() {
        recyclerViewHotSong = view.findViewById(R.id.recyclerViewHotSong);
        sharedPreferences = getContext().getSharedPreferences("userInfo",Context.MODE_PRIVATE);
    }
}