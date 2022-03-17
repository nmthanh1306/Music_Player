package com.is1423.music_player.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.is1423.music_player.R;
import com.is1423.music_player.activity.AllAlbumActivity;
import com.is1423.music_player.adapter.AlbumAdapter;
import com.is1423.music_player.model.response.AlbumResponseDTO;
import com.is1423.music_player.service.DataServiceAlbum;
import com.is1423.music_player.service.intagration.APIServiceAlbum;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumHotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumHotFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private View view;
    private RecyclerView recyclerViewAlbum;
    private TextView tvViewMoreAlbum;
    AlbumAdapter adapter;
    private String mParam1;
    private String mParam2;

    public AlbumHotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumHotFragment newInstance(String param1, String param2) {
        AlbumHotFragment fragment = new AlbumHotFragment();
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
        view = inflater.inflate(R.layout.fragment_album_hot, container, false);
        bindingView();
        bindingAction();
        fetchData();
        return view;
    }

    private void bindingAction() {
        tvViewMoreAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllAlbumActivity.class);
                startActivity(intent);
            }
        });
    }

    private void bindingView() {
        recyclerViewAlbum = view.findViewById(R.id.recyclerViewAlbum);
        tvViewMoreAlbum = view.findViewById(R.id.tvViewMoreAlbum);
    }

    private void fetchData() {
        DataServiceAlbum serviceAlbum = APIServiceAlbum.getService();

        Call<List<AlbumResponseDTO>> callBack = serviceAlbum.getRandom4AlbumPerDay();
        callBack.enqueue(new Callback<List<AlbumResponseDTO>>() {
            @Override
            public void onResponse(Call<List<AlbumResponseDTO>> call, Response<List<AlbumResponseDTO>> response) {
                List<AlbumResponseDTO> albumResponseDTOS = response.body();

                adapter = new AlbumAdapter(getActivity(),albumResponseDTOS);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(RecyclerView.HORIZONTAL);
                recyclerViewAlbum.setLayoutManager(layoutManager);
                recyclerViewAlbum.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<AlbumResponseDTO>> call, Throwable t) {
                Log.e("error", "fail:" + t.getMessage());
                Log.e("error", t.toString());
            }
        });
    }
}