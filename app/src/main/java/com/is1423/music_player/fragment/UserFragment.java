package com.is1423.music_player.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.is1423.music_player.R;
import com.is1423.music_player.activity.AllPlaylistActivity;
import com.is1423.music_player.activity.ListSongActivity;
import com.is1423.music_player.model.request.UserRequestDTO;
import com.is1423.music_player.model.response.PlaylistResponseDTO;
import com.is1423.music_player.model.response.UserResponseDTO;
import com.is1423.music_player.service.DataServicePlaylist;
import com.is1423.music_player.service.DataServiceUser;
import com.is1423.music_player.service.intagration.ApiServicePlaylist;
import com.is1423.music_player.service.intagration.ApiServiceUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    private View view;
    private ImageView ivUser;
    private TextView tvUsername;
    private TextView tvMyPlaylist;
    private TextView tvMyFavouriteSong;
    private Button btnLogout;
    private Button btnLogin;
    private Button btnRegister;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLoginPopup;
    private EditText edtRePassword;
    private Button btnRegisterPopup;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        view = inflater.inflate(R.layout.fragment_user, container, false);
        bindingView(view);
        bindingAction();
        return view;
    }

    private void bindingAction() {
        btnLogin.setOnClickListener(this::onClickBtnLogin);
        btnRegister.setOnClickListener(this::onClickBtnRegister);
        btnLogout.setOnClickListener(this::onClickBtnLogout);
        tvMyPlaylist.setOnClickListener(this::onClickTextMyPlaylist);
        tvMyFavouriteSong.setOnClickListener(this::onClickTextMyFavouriteSong);
    }

    private void onClickTextMyFavouriteSong(View view) {
        Intent intent = new Intent(getActivity(), ListSongActivity.class);
        String userId = sharedPreferences.getString("userId", null);
        intent.putExtra("myFavouriteSong", userId);
        startActivity(intent);
    }

    private void onClickTextMyPlaylist(View view) {
        Intent intent = new Intent(getActivity(), AllPlaylistActivity.class);
        intent.putExtra("MyAllPlaylist", "");
        startActivity(intent);
    }

    private void onClickBtnLogout(View view) {
        prefEditor.clear();
        prefEditor.apply();
        hideInfoOnProfile();
    }

    private void onClickBtnRegisterPopup(Dialog dialog) {
        btnRegisterPopup.setOnClickListener(view -> {

            String sUsername = edtUsername.getText().toString();
            String sPassword = edtPassword.getText().toString();
            String sRePassword = edtRePassword.getText().toString();

            if (isEmptyFields(sUsername, sPassword, sRePassword)) {
                Toast.makeText(getContext(), "Xin hãy điền các ô còn trống!", Toast.LENGTH_SHORT).show();
            } else {
                if (!isMatchPassword()) {
                    Toast.makeText(getContext(), "Mật khẩu không khớp, hãy thử lại", Toast.LENGTH_SHORT).show();
                } else {
                    DataServiceUser serviceUser = ApiServiceUser.getService();
                    UserRequestDTO userRequest = new UserRequestDTO(sUsername, sPassword);

                    Call<UserResponseDTO> callback = serviceUser.createAccount(userRequest);
                    callback.enqueue(new Callback<UserResponseDTO>() {
                        @Override
                        public void onResponse(Call<UserResponseDTO> call, Response<UserResponseDTO> response) {
                            if (response.code() == 400) {
                                Toast.makeText(getContext(), "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(getContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<UserResponseDTO> call, Throwable t) {

                        }
                    });
                }
            }
        });

    }

    private boolean isEmptyFields(String... args) {
        for (String s : args) {
            if (s.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean isMatchPassword() {
        return edtPassword.getText().toString().equals(edtRePassword.getText().toString());
    }


    private void onClickBtnRegister(View view) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.style_register);
        edtUsername = dialog.findViewById(R.id.edtUsername);
        edtPassword = dialog.findViewById(R.id.edtPassword);
        edtRePassword = dialog.findViewById(R.id.edtRePassword);
        btnRegisterPopup = dialog.findViewById(R.id.btnRegisterPopup);
        onClickBtnRegisterPopup(dialog);
        eventClickCloseDialogButton(dialog);
    }

    private void onClickBtnLogin(View view) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.style_login);
        edtUsername = dialog.findViewById(R.id.edtUsername);
        edtPassword = dialog.findViewById(R.id.edtPassword);
        btnLoginPopup = dialog.findViewById(R.id.btnLoginPopup);
        onClickBtnLoginPopup(dialog);
        eventClickCloseDialogButton(dialog);
    }

    private void onClickBtnLoginPopup(Dialog dialog) {
        btnLoginPopup.setOnClickListener(view -> {
            String sUsername = edtUsername.getText().toString();
            String sPassword = edtPassword.getText().toString();

            if (isEmptyFields(sUsername, sPassword)) {
                Toast.makeText(getContext(), "Xin hãy điền các ô còn trống!", Toast.LENGTH_SHORT).show();
            } else {
                DataServiceUser serviceUser = ApiServiceUser.getService();
                UserRequestDTO userRequest = new UserRequestDTO(sUsername, sPassword);

                Call<UserResponseDTO> callback = serviceUser.login(userRequest);
                callback.enqueue(new Callback<UserResponseDTO>() {
                    @Override
                    public void onResponse(Call<UserResponseDTO> call, Response<UserResponseDTO> response) {
                        if (response.code() == 401) {
                            Toast.makeText(getContext(), "Sai thông tin, vui lòng kiểm tra lại", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        UserResponseDTO user = response.body();
                        if (response.code() == 200 && user != null) {
                            Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            showInfoOnProfile();
                            setDataAfterLogin(user.getUserName(), user.getId().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponseDTO> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void eventClickCloseDialogButton(Dialog dialog) {
        ImageView ivClose = dialog.findViewById(R.id.btnClose);
        ivClose.setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }

    private void bindingView(View view) {
        sharedPreferences = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();

        ivUser = view.findViewById(R.id.ivUser);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvMyPlaylist = view.findViewById(R.id.tvMyPlaylist);
        tvMyFavouriteSong = view.findViewById(R.id.tvMyFavouriteSong);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);

        String username = sharedPreferences.getString("username", null);
        String userId = sharedPreferences.getString("userId", null);
        setDataAfterLogin(username, userId);
    }

    private void showInfoOnProfile() {
        btnLogin.setVisibility(View.GONE);
        btnRegister.setVisibility(View.GONE);
        tvUsername.setVisibility(View.VISIBLE);
        tvMyFavouriteSong.setVisibility(View.VISIBLE);
        tvMyPlaylist.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.VISIBLE);

    }

    private void hideInfoOnProfile() {
        btnLogin.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
        tvUsername.setVisibility(View.GONE);
        tvMyFavouriteSong.setVisibility(View.GONE);
        tvMyPlaylist.setVisibility(View.GONE);
        btnLogout.setVisibility(View.GONE);
    }

    private void setDataAfterLogin(String username, String userId) {
        if (username == null || userId == null) {
            hideInfoOnProfile();
        } else {
            showInfoOnProfile();
            tvUsername.setText("Hi, " + username);
            prefEditor.putString("username", username);
            prefEditor.putString("userId", userId);
            prefEditor.apply();
        }
    }
}