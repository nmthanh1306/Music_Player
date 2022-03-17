package com.is1423.music_player.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.is1423.music_player.R;
import com.is1423.music_player.activity.AllPlaylistActivity;
import com.is1423.music_player.activity.ListSongActivity;
import com.is1423.music_player.adapter.PlaylistAdapter;
import com.is1423.music_player.model.response.PlaylistResponseDTO;
import com.is1423.music_player.service.DataServicePlaylist;
import com.is1423.music_player.service.intagration.ApiServicePlaylist;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private ListView listViewPlaylist;
    private TextView tvPlaylistTitle;
    private TextView tvViewMorePlaylist;
    private PlaylistAdapter adapter;
    private List<PlaylistResponseDTO> dtoList;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistFragment newInstance(String param1, String param2) {
        PlaylistFragment fragment = new PlaylistFragment();
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
        view = inflater.inflate(R.layout.fragment_playlist, container, false);
        bindingView();
        getData();
        bindingAction();
        // Inflate the layout for this fragment
        return view;
    }

    private void bindingAction() {
        tvViewMorePlaylist.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AllPlaylistActivity.class);
            startActivity(intent);
        });
    }

    private void bindingView() {
        listViewPlaylist = view.findViewById(R.id.listViewPlaylist);
        tvPlaylistTitle = view.findViewById(R.id.textViewTitlePlaylist);
        tvViewMorePlaylist = view.findViewById(R.id.tvViewMorePlaylist);
    }

    private void getData() {
        DataServicePlaylist service = ApiServicePlaylist.getService();
        Call<List<PlaylistResponseDTO>> callBack = service.get3PlaylistWasRandomCurrentDate();
        callBack.enqueue(new Callback<List<PlaylistResponseDTO>>() {
            @Override
            public void onResponse(Call<List<PlaylistResponseDTO>> call, Response<List<PlaylistResponseDTO>> response) {
                 dtoList = response.body();
                 Log.d("PlaylistFragment", "=================Is Running==================");
                 adapter = new PlaylistAdapter(getActivity(), android.R.layout.simple_list_item_1,dtoList);
                 listViewPlaylist.setAdapter(adapter);
                 setListViewHeightBasedOnChildren(listViewPlaylist);

                 listViewPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                         Intent intent = new Intent(getActivity(), ListSongActivity.class);
                         intent.putExtra("itemPlaylist",dtoList.get(i));
                         startActivity(intent);
                     }
                 });
            }

            @Override
            public void onFailure(Call<List<PlaylistResponseDTO>> call, Throwable t) {
                Log.e("error", "fail:" + t.getMessage());
                Log.e("error", t.toString());
            }
        });
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if(listItem != null){
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}