package com.is1423.music_player.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.is1423.music_player.R;
import com.is1423.music_player.adapter.BannerAdapter;
import com.is1423.music_player.model.response.AdvertisementResponseDTO;
import com.is1423.music_player.service.intagration.ApiServiceAdvertisement;
import com.is1423.music_player.service.DataServiceAdvertisement;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BannerFragment extends Fragment {

    private View view;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private BannerAdapter bannerAdapter;
    private Runnable runnable;
    private Handler handler;
    private int currentItem;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final DataServiceAdvertisement dataServiceAdvertisement = ApiServiceAdvertisement.getService();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BannerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BannerFragment newInstance(String param1, String param2) {
        BannerFragment fragment = new BannerFragment();
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
        view = inflater.inflate(R.layout.fragment_banner, container, false);

        bindingView();
        getData();
        // Inflate the layout for this fragment
        return view;
    }

    private void bindingView() {
        viewPager = view.findViewById(R.id.viewPager);
        circleIndicator = view.findViewById(R.id.indicatorDefault);

    }

    private void getData() {

        Call<List<AdvertisementResponseDTO>> callBack = dataServiceAdvertisement.getAllAdvertisements();
        callBack.enqueue(new Callback<List<AdvertisementResponseDTO>>() {
            @Override
            public void onResponse(Call<List<AdvertisementResponseDTO>> call, Response<List<AdvertisementResponseDTO>> response) {
                List<AdvertisementResponseDTO> ads = response.body();
                Log.d("BannerFragment", "=================Is Running==================");
                bannerAdapter = new BannerAdapter(getActivity(), ads);
                viewPager.setAdapter(bannerAdapter);
                circleIndicator.setViewPager(viewPager);

                handler = new Handler();
                runnable = () -> {
                    currentItem = viewPager.getCurrentItem();
                    currentItem++;
                    if(currentItem >= viewPager.getAdapter().getCount()){
                        currentItem = 0;
                    }
                    viewPager.setCurrentItem(currentItem,true);
                    handler.postDelayed(runnable,5000);
                };
                handler.postDelayed(runnable,5000);
            }


            @Override
            public void onFailure(Call<List<AdvertisementResponseDTO>> call, Throwable t) {
                Log.e("error", "fail:" + t.getMessage());
                Log.e("error", t.toString());
            }
        });
    }
}