package com.is1423.music_player.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.is1423.music_player.R;
import com.is1423.music_player.fragment.AllSongPlayerFragment;
import com.is1423.music_player.fragment.MusicDiscFragment;
import com.is1423.music_player.fragment.ViewPagerPlaylistSongs;
import com.is1423.music_player.model.response.SongResponseDTO;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SongPlayerActivity extends AppCompatActivity {

    private Toolbar toolbarSongPlayer;
    private TextView tvTimeSong;
    private TextView tvTotalTimeSong;
    private SeekBar seekBarTime;
    private ImageView ivPlay;
    private ImageView ivRepeat;
    private ImageView ivNext;
    private ImageView ivPre;
    private ImageView ivRandom;
    private ViewPager viewPagerSongPlayer;
    public static List<SongResponseDTO> songs = new ArrayList<>();
    public static ViewPagerPlaylistSongs musicAdapter;
    private MusicDiscFragment musicDiscFragment;
    private AllSongPlayerFragment allSongPlayerFragment;
    private MediaPlayer mediaPlayer;
    private int position = 0;
    private boolean repeat = false;
    private boolean random = false;
    private boolean next = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); // check network signal
        StrictMode.setThreadPolicy(policy);
        getDataFromIntent();
        bindingView();
        bindingAction();
        eventClick();
    }

    private void eventClick() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (musicAdapter.getItem(1) != null) {
                    if (!songs.isEmpty()) {
                        musicDiscFragment.PlayMusic(songs.get(0).getSongImage());
                        handler.removeCallbacks(this);
                    } else {
                        handler.postDelayed(this, 300);
                    }
                }
            }
        }, 500);

        ivPlay.setOnClickListener(view -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    ivPlay.setImageResource(R.drawable.iconplay);
                } else {
                    mediaPlayer.start();
                    ivPlay.setImageResource(R.drawable.iconpause);
                }
            }
        });

        ivRepeat.setOnClickListener(this::onClickImageViewRepeat);
        ivRandom.setOnClickListener(this::onClickImageViewRandom);
        ivNext.setOnClickListener(view -> onCLickImageViewNext());
        ivPre.setOnClickListener(this::onCLickImageViewPre);

        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // TODO document why this method is empty
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO document why this method is empty
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });
    }

    private void randomSong() {
        if (random) {
            Random rand = new Random();
            int index = rand.nextInt(songs.size());
            if (index == position) {
                position = index - 1;
            }
            position = index;
        }
    }

    private void onCLickImageViewPre(View view) {
        if (!songs.isEmpty()) {
            if (mediaPlayer.isPlaying() && mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if (position < songs.size()) {
                ivPlay.setImageResource(R.drawable.iconpause);
                position--;
                if (position < 0) {
                    position = (songs.size() - 1);
                }
                if (repeat) {
                    if (position == 0) {
                        position = songs.size();
                    }
                    position++;
                }
                randomSong();
                new PlayMp3().execute(songs.get(position).getLinkSong());
                musicDiscFragment.PlayMusic(songs.get(position).getSongImage());
                getSupportActionBar().setTitle(songs.get(position).getSongName());
                updateTime();
            }
        }
        ivPre.setClickable(false);
        ivNext.setClickable(false);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            ivPre.setClickable(true);
            ivNext.setClickable(true);
        }, 5000);
    }

    private void onCLickImageViewNext() {
        if (!songs.isEmpty()) {
            if (mediaPlayer.isPlaying() && mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if (position < songs.size()) {
                ivPlay.setImageResource(R.drawable.iconpause);
                position++;
                if (repeat) {
                    if (position == 0) {
                        position = songs.size();
                    }
                    position--;
                }

                randomSong();

                if (position > (songs.size() - 1)) {
                    position = 0;
                }
                new PlayMp3().execute(songs.get(position).getLinkSong());
                musicDiscFragment.PlayMusic(songs.get(position).getSongImage());
                getSupportActionBar().setTitle(songs.get(position).getSongName());
                updateTime();
            }
        }
        ivPre.setClickable(false);
        ivNext.setClickable(false);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            ivPre.setClickable(true);
            ivNext.setClickable(true);
        }, 5000);
    }

    private void onClickImageViewRandom(View view) {
        if (!random) {
            if (repeat) {
                repeat = false;
                ivRandom.setImageResource(R.drawable.iconshuffled);
                ivRepeat.setImageResource(R.drawable.iconrepeat);
            }
            ivRandom.setImageResource(R.drawable.iconshuffled);
            random = true;
        } else {
            ivRandom.setImageResource(R.drawable.iconsuffle);
            random = false;
        }
    }

    private void onClickImageViewRepeat(View view) {
        if (!repeat) {
            if (random) {
                random = false;
                ivRepeat.setImageResource(R.drawable.iconsyned);
                ivRandom.setImageResource(R.drawable.iconsuffle);
            }
            ivRepeat.setImageResource(R.drawable.iconsyned);
            repeat = true;
        } else {
            ivRepeat.setImageResource(R.drawable.iconrepeat);
            repeat = false;
        }
    }

    private void getDataFromIntent() {
        songs.clear();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("song")) {
                SongResponseDTO song = intent.getParcelableExtra("song");
                songs.add(song);
            }
            if (intent.hasExtra("songs")) {
                songs = intent.getParcelableArrayListExtra("songs");
            }
        }
    }

    private void bindingAction() {
        setSupportActionBar(toolbarSongPlayer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarSongPlayer.setTitleTextColor(Color.WHITE);

        if (!songs.isEmpty()) {
            getSupportActionBar().setTitle(songs.get(0).getSongName());
            new PlayMp3().execute(songs.get(0).getLinkSong());
            ivPlay.setImageResource(R.drawable.iconpause);

        }

        toolbarSongPlayer.setNavigationOnClickListener(view -> {
            finish();
            mediaPlayer.stop();
            songs.clear();
        });
    }

    private void bindingView() {
        toolbarSongPlayer = findViewById(R.id.toolBarSongPlayer);
        tvTimeSong = findViewById(R.id.tvTimeSong);
        tvTotalTimeSong = findViewById(R.id.tvTotalTimeSong);
        seekBarTime = findViewById(R.id.seekBarTimeSong);
        ivPlay = findViewById(R.id.imageButtonPlay);
        ivRepeat = findViewById(R.id.imageButtonRepeat);
        ivNext = findViewById(R.id.imageButtonNext);
        ivPre = findViewById(R.id.imageButtonPre);
        ivRandom = findViewById(R.id.imageButtonShuffle);
        viewPagerSongPlayer = findViewById(R.id.viewPagerSongPlayer);

        allSongPlayerFragment = new AllSongPlayerFragment();
        musicDiscFragment = new MusicDiscFragment();

        musicAdapter = new ViewPagerPlaylistSongs(getSupportFragmentManager());
        musicAdapter.addFragment(allSongPlayerFragment);
        musicAdapter.addFragment(musicDiscFragment);
        viewPagerSongPlayer.setAdapter(musicAdapter);

        musicDiscFragment = (MusicDiscFragment) musicAdapter.getItem(1);
    }

    class PlayMp3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String song) {
            super.onPostExecute(song);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                // tranh truong hop thoi gian tai bi lau
                mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                });

                mediaPlayer.setDataSource(song);
                mediaPlayer.prepare(); // play
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            timingSong();
            updateTime();
        }

    }

    private void timingSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        tvTotalTimeSong.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekBarTime.setMax(mediaPlayer.getDuration());
    }

    private void updateTime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    seekBarTime.setProgress(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    tvTimeSong.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this,300);
                    mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                        next = true;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        },300);
        Handler nextSong = new Handler();
        nextSong.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(next){
                    onCLickImageViewNext();
                    next = false;
                    nextSong.removeCallbacks(this);
                }else{
                    nextSong.postDelayed(this,1000);
                }
            }
        },1000);
    }

}