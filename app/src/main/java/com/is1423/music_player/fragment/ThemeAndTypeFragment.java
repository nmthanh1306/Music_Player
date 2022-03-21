package com.is1423.music_player.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.is1423.music_player.R;
import com.is1423.music_player.activity.AllThemeActivity;
import com.is1423.music_player.activity.AllTypeByThemeActivity;
import com.is1423.music_player.activity.ListSongActivity;
import com.is1423.music_player.model.response.ThemeResponseDTO;
import com.is1423.music_player.model.response.TypeResponseDTO;
import com.is1423.music_player.service.DataServiceTheme;
import com.is1423.music_player.service.DataServiceType;
import com.is1423.music_player.service.intagration.ApiServiceTheme;
import com.is1423.music_player.service.intagration.ApiServiceType;
import com.squareup.picasso.Picasso;

import java.util.List;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThemeAndTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThemeAndTypeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private View view;
    private List<ThemeResponseDTO> themeDTOs;
    private List<TypeResponseDTO> typeDTOs;
    private HorizontalScrollView horizontalScrollView;
    private TextView tvViewMoreThemeAndType;


    private String mParam1;
    private String mParam2;

    public ThemeAndTypeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThemeAndTypeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThemeAndTypeFragment newInstance(String param1, String param2) {
        ThemeAndTypeFragment fragment = new ThemeAndTypeFragment();
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
        Log.d("CREATE", "ThemAndTypeFragment");
        view = inflater.inflate(R.layout.fragment_theme_and_type, container, false);
        bindingView();
        fetchDataThemeAndType();
        bindingAction();
        return view;
    }

    private void fetchDataThemeAndType() {

        fetchDataMusicType();

        DataServiceTheme serviceTheme = ApiServiceTheme.getService();

        Call<List<ThemeResponseDTO>> callBackTheme = serviceTheme.getRandom4Theme();
        callBackTheme.enqueue(new Callback<List<ThemeResponseDTO>>() {
            @Override
            public void onResponse(Call<List<ThemeResponseDTO>> call, Response<List<ThemeResponseDTO>> response) {
                if (response.code() == 200) {
                    themeDTOs = response.body();

                    Log.d("themeResponse", "=================Is Running==================");

                    LinearLayout linearLayout = new LinearLayout(getActivity());
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                    LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(580, 250);
                    layout.setMargins(10, 20, 10, 30);
                    if (themeDTOs != null && typeDTOs != null) {
                        for (int i = 0; i < themeDTOs.size(); i++) {
                            CardView cardView = new CardView(getActivity());
                            cardView.setRadius(10);
                            ImageView imageView = new ImageView(getActivity());
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            if (themeDTOs.get(i).getThemeImage() != null) {
                                Picasso.with(getActivity()).load(themeDTOs.get(i).getThemeImage()).into(imageView);
                            }
                            cardView.setLayoutParams(layout);
                            cardView.addView(imageView);
                            linearLayout.addView(cardView);
                            int finalI = i;
                            imageView.setOnClickListener(view -> {
                                Intent intent = new Intent(getActivity(), AllTypeByThemeActivity.class);
                                intent.putExtra("theme", themeDTOs.get(finalI));
                                startActivity(intent);
                            });
                        }

                        for (int j = 0; j < typeDTOs.size(); j++) {
                            CardView cardView = new CardView(getActivity());
                            cardView.setRadius(10);
                            ImageView imageView = new ImageView(getActivity());
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            if (typeDTOs.get(j).getTypeImage() != null) {
                                Picasso.with(getActivity()).load(typeDTOs.get(j).getTypeImage()).into(imageView);
                            }
                            cardView.setLayoutParams(layout);
                            cardView.addView(imageView);
                            linearLayout.addView(cardView);
                            int finalJ = j;
                            imageView.setOnClickListener(view -> {
                                Intent intent = new Intent(getActivity(), ListSongActivity.class);
                                intent.putExtra("typeId", typeDTOs.get(finalJ));
                                startActivity(intent);
                            });
                        }
                        horizontalScrollView.addView(linearLayout);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ThemeResponseDTO>> call, Throwable t) {
                Log.e("error", "fail:" + t.getMessage());
                Log.e("error", t.toString());
            }
        });
    }

    private void fetchDataMusicType() {
        DataServiceType serviceType = ApiServiceType.getService();
        Call<List<TypeResponseDTO>> callBackType = serviceType.getRandom4Types();
        callBackType.enqueue(new Callback<List<TypeResponseDTO>>() {
            @Override
            public void onResponse(Call<List<TypeResponseDTO>> call, Response<List<TypeResponseDTO>> response) {
                if (response.code() == 200) {
                    typeDTOs = response.body();
                }
            }

            @Override
            public void onFailure(Call<List<TypeResponseDTO>> call, Throwable t) {
                Log.e("error", "fail:" + t.getMessage());
                Log.e("error", t.toString());
            }
        });
    }

    private void bindingView() {
        horizontalScrollView = view.findViewById(R.id.horizontalScrollView);
        tvViewMoreThemeAndType = view.findViewById(R.id.tvViewMoreThemeAndType);
    }

    private void bindingAction() {
        tvViewMoreThemeAndType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllThemeActivity.class);
                startActivity(intent);
            }
        });
    }
}