package com.is1423.music_player.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.is1423.music_player.R;
import com.is1423.music_player.activity.SongPlayerActivity;
import com.is1423.music_player.adapter.SongPlayerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllSongPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllSongPlayerFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private View view;
    private RecyclerView recyclerViewSongPlayer;
    private SongPlayerAdapter adapter;
    private String mParam1;
    private String mParam2;

    public AllSongPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListAllSongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllSongPlayerFragment newInstance(String param1, String param2) {
        AllSongPlayerFragment fragment = new AllSongPlayerFragment();
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
        view = inflater.inflate(R.layout.fragment_list_all_song, container, false);
        bindingView();
        if (SongPlayerActivity.songs.size() > 0) {
            adapter = new SongPlayerAdapter(getActivity(), SongPlayerActivity.songs);
            recyclerViewSongPlayer.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewSongPlayer.setAdapter(adapter);

        }
        return view;
    }

    private void bindingView() {
        recyclerViewSongPlayer = view.findViewById(R.id.recyclerSongPlayer);
    }
}