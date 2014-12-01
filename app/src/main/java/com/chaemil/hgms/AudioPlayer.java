package com.chaemil.hgms;




import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.MediaController.MediaPlayerControl;


import com.chaemil.hgms.db.AudioDBHelper;
import com.chaemil.hgms.utils.MusicController;
import com.chaemil.hgms.utils.Basic;

import static com.chaemil.hgms.db.AudioDBHelper.deleteAudioDBRecord;

public class AudioPlayer extends Activity implements OnPreparedListener/*, MediaPlayerControl TODO*/ {

    private ImageButton btnPlay;
    private ImageButton btnPouse;
    private ImageButton btnFf;
    private ImageButton btnRew;
    private int current = 0;
    private boolean   running = true;
    private	int duration = 0;
    private MediaPlayer mPlayer;
    private SeekBar mSeekBarPlayer;
    private TextView mMediaTime;
    private ImageView audioThumb;
    private TextView audioName;
    private TextView audioDate;
    private MusicController controller;
    NotificationCompat.Builder builder;

    private void setController(){
        controller = new MusicController(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.seekTo(mPlayer.getCurrentPosition()+20000);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.seekTo(mPlayer.getCurrentPosition()-20000);
            }
        });
    }

    public String file() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString(Basic.AUDIO_FILE);
    }

    public String audioThumb() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString(Basic.AUDIO_FILE_THUMB);
    }

    public String audioName() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString(Basic.AUDIO_FILE_NAME);
    }

    public String audioDate() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString(Basic.AUDIO_FILE_DATE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_player);
        btnPlay = (ImageButton) findViewById(R.id.play);
        btnPouse = (ImageButton) findViewById(R.id.pause);
        btnFf = (ImageButton) findViewById(R.id.ff);
        btnRew = (ImageButton) findViewById(R.id.rew);
        mMediaTime = (TextView)findViewById(R.id.mediaTime);
        mSeekBarPlayer = (SeekBar)findViewById(R.id.seekBar);
        audioName = (TextView) findViewById(R.id.audioName);
        audioThumb = (ImageView) findViewById(R.id.audioThumb);
        audioDate = (TextView) findViewById(R.id.audioDate);

        Log.i(Basic.AUDIO_FILE_THUMB, audioThumb().substring(audioThumb().lastIndexOf("/")+1));
        Log.i(Basic.AUDIO_FILE, file());

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        audioName.setText(audioName());
        audioThumb.setImageURI(Uri.parse(audioThumb()));
        audioDate.setText(audioDate());

        /*controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.song_list));
        controller.setEnabled(true);*/
        //TODO

        setController();

        mPlayer = new MediaPlayer();
        mPlayer = MediaPlayer.create(this, Uri.parse(getExternalFilesDir(null) + "/" + file()));
        mPlayer.setOnPreparedListener(this);
        mPlayer.start();

        Log.i("filePath", getExternalFilesDir(null) + "/" + file());

        mSeekBarPlayer.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mPlayer.seekTo(progress);
                    updateTime();
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    mPlayer.prepare();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mPlayer.start();
                mSeekBarPlayer.postDelayed(onEverySecond, 1000);
            }
        });


        btnPouse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPlayer.pause();
            }
        });

        btnFf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPlayer.seekTo(mPlayer.getCurrentPosition()+20000);
            }
        });

        btnRew.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mPlayer.seekTo(mPlayer.getCurrentPosition()-20000);
            }
        });
    }

    public void goToVideo() {
        /*new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(getString(R.string.showOnlineVideo)+"?")
                .setMessage(getString(R.string.showOnlineVideoLong))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitPlayer();
                        finish();
                        Intent VideoLaunch = new Intent(OfflinePlayer.this, VideoLaunch.class);
                        VideoLaunch.putExtra("videoLink", file().substring(file().lastIndexOf("/")));
                        OfflinePlayer.this.startActivity(VideoLaunch);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
                    }

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();*/
    }

    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run(){
            if(running){
                if(mSeekBarPlayer != null) {
                    mSeekBarPlayer.setProgress(mPlayer.getCurrentPosition());
                }

                if(mPlayer.isPlaying()) {
                    mSeekBarPlayer.postDelayed(onEverySecond, 1000);
                    updateTime();
                }
            }
        }
    };

    private void updateTime(){
        do {
            current = mPlayer.getCurrentPosition();
            /*System.out.println("duration - " + duration + " current- "
                    + current);*/
            int dSeconds = (duration / 1000) % 60 ;
            int dMinutes = ((duration / (1000*60)) % 60);
            int dHours   = ((duration / (1000*60*60)) % 24);

            int cSeconds = (current / 1000) % 60 ;
            int cMinutes = ((current / (1000*60)) % 60);
            int cHours   = ((current / (1000*60*60)) % 24);

            if(dHours == 0){
                mMediaTime.setText(String.format("%02d:%02d / %02d:%02d", cMinutes, cSeconds, dMinutes, dSeconds));
            }else{
                mMediaTime.setText(String.format("%02d:%02d:%02d / %02d:%02d:%02d", cHours, cMinutes, cSeconds, dHours, dMinutes, dSeconds));
            }

            try{
                Log.d("Value: ", String.valueOf((int) (current * 100 / duration)));
                if(mSeekBarPlayer.getProgress() >= 100){
                    break;
                }
            }catch (Exception e) {}
        }while (mSeekBarPlayer.getProgress() <= 100);
    }

    @Override
    public void onPrepared(MediaPlayer arg0) {
        duration = mPlayer.getDuration();
        mSeekBarPlayer.setMax(duration);
        mSeekBarPlayer.postDelayed(onEverySecond, 1000);
    }

    public void exitPlayer() {
        //mVideoView.stopPlayback();
        mPlayer.stop();
        Intent i = new Intent(AudioPlayer.this, ListDownloadedAudio.class);
        startActivity(i);
        finish();
    }

    public void deleteAudio() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(getString(R.string.deleteAudio)+"?")
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitPlayer();
                        File audioFile = new File(getExternalFilesDir(null), file());
                        File audioThumb = new File(getExternalFilesDir(null), audioThumb().substring(audioThumb().lastIndexOf("/") + 1));
                        //Toast.makeText(getApplicationContext(), file.toString(), Toast.LENGTH_LONG).show();
                        audioFile.delete();
                        audioThumb.delete();
                        AudioDBHelper helper = new AudioDBHelper(getApplicationContext());
                        SQLiteDatabase db = helper.getWritableDatabase();
                        deleteAudioDBRecord(db, file());
                        //overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right);
                        exitPlayer();
                    }

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    exitPlayer();
                    //overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right);
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                exitPlayer();
                //overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right);
                return true;
            case R.id.show_video:
                goToVideo();
                return true;
            case R.id.delete_action:
                deleteAudio();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.audio_player, menu);
        return true;
    }
}