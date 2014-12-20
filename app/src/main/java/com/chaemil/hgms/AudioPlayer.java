package com.chaemil.hgms;




import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


import com.chaemil.hgms.db.AudioDBHelper;
import com.chaemil.hgms.utils.MusicController;
import com.chaemil.hgms.utils.Basic;
import com.chaemil.hgms.utils.NotificationPanel;
import com.chaemil.hgms.utils.Utils;

import static com.chaemil.hgms.db.AudioDBHelper.deleteAudioDBRecord;
import static com.chaemil.hgms.utils.Utils.getScreenHeight;
import static com.chaemil.hgms.utils.Utils.getScreenWidth;
import static com.chaemil.hgms.utils.Utils.setActionStatusBarTint;

public class AudioPlayer extends Activity implements OnPreparedListener/*, MediaPlayerControl TODO*/ {

    private at.markushi.ui.CircleButton btnPlay;
    private at.markushi.ui.CircleButton btnPause;
    private at.markushi.ui.CircleButton btnFf;
    private at.markushi.ui.CircleButton btnRew;
    private int current = 0;
    private boolean   running = true;
    private	int duration = 0;
    private static MediaPlayer mPlayer;
    private SeekBar mSeekBarPlayer;
    private TextView mMediaTime;
    private ImageView audioThumb;
    private TextView audioName;
    private TextView audioDate;
    private MusicController controller;
    private int audioThumbWidth;
    private int audioThumbHeight;
    private NotificationPanel nPanel;
    private NoisyAudioStreamReceiver mNoisyAudioStreamReceiver;

    private class NoisyAudioStreamReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                pause();
            }
        }
    }

    private IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);


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

    public static void pause() {
        mPlayer.pause();
        Log.i("mPlayer","pause");
    }

    public static void play() {
        mPlayer.start();
        Log.i("mPlayer","play");
    }

    public void saveAudioTime(SQLiteDatabase db, String fileName, int time) {
        AudioDBHelper.saveAudioTime(db, fileName, time);
    }

    public int loadAudioTime(SQLiteDatabase db, String fileName) {
        return AudioDBHelper.loadAudioTime(db, fileName);
    }

    public void calculateAudioThumb() {
        if (getScreenHeight(getApplicationContext()) > getScreenWidth(getApplicationContext())) {
            audioThumbWidth = Integer.valueOf((int) (getScreenWidth(getApplicationContext()) * 0.8));
            audioThumbHeight = Integer.valueOf((int) (audioThumbWidth * 0.5625));
        }
        else {
            audioThumbWidth = Integer.valueOf((int) (getScreenHeight(getApplicationContext()) * 0.4));
            audioThumbHeight = Integer.valueOf((int) (audioThumbWidth * 0.5625));
        }
        audioThumb.getLayoutParams().width = audioThumbWidth;
        audioThumb.getLayoutParams().height = audioThumbHeight;
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioDBHelper helper = new AudioDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        saveAudioTime(db,file(),mPlayer.getCurrentPosition());
        Utils.goBackwardAnimation(this);
        nPanel = new NotificationPanel(this, audioName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (nPanel != null) {
            nPanel.notificationCancel();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (nPanel != null) {
            nPanel.notificationCancel();
        }
        unregisterReceiver(mNoisyAudioStreamReceiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_player);
        btnPlay = (at.markushi.ui.CircleButton) findViewById(R.id.play);
        btnPause = (at.markushi.ui.CircleButton) findViewById(R.id.pause);
        btnFf = (at.markushi.ui.CircleButton) findViewById(R.id.ff);
        btnRew = (at.markushi.ui.CircleButton) findViewById(R.id.rew);
        mMediaTime = (TextView)findViewById(R.id.mediaTime);
        mSeekBarPlayer = (SeekBar)findViewById(R.id.seekBar);
        audioName = (TextView) findViewById(R.id.audioName);
        audioThumb = (ImageView) findViewById(R.id.audioThumb);
        audioDate = (TextView) findViewById(R.id.audioDate);

        calculateAudioThumb();

        Utils.submitStatistics(getApplicationContext());

        AudioDBHelper helper = new AudioDBHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();
        Log.d("loadAudioTime", String.valueOf(loadAudioTime(db,file())));


        mNoisyAudioStreamReceiver = new NoisyAudioStreamReceiver();

        registerReceiver(mNoisyAudioStreamReceiver, intentFilter);

        if (nPanel != null) {
            nPanel.notificationCancel();
        }

        Log.i(Basic.AUDIO_FILE_THUMB, audioThumb().substring(audioThumb().lastIndexOf("/") + 1));
        Log.i(Basic.AUDIO_FILE, file());

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);

        }

        setActionStatusBarTint(getWindow(), this , Basic.AUDIOPLAYER_STATUSBAR_COLOR,
                Basic.AUDIOPLAYER_ACTIONBAR_COLOR);

        audioName.setText(audioName());
        audioThumb.setImageURI(Uri.parse(audioThumb()));
        audioDate.setText(audioDate());

        /*controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.song_list));
        controller.setEnabled(true);*/

        setController();

        mPlayer = new MediaPlayer();
        mPlayer = MediaPlayer.create(this, Uri.parse(getExternalFilesDir(null) + "/" + file()));
        mPlayer.setOnPreparedListener(this);
        mPlayer.start();
        mPlayer.seekTo(loadAudioTime(db,file()));

        Log.i("filePath", getExternalFilesDir(null) + "/" + file());

        mSeekBarPlayer.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
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


        btnPause.setOnClickListener(new View.OnClickListener() {

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
                mMediaTime.setText(String.format("%02d:%02d / %02d:%02d", cMinutes,
                        cSeconds, dMinutes, dSeconds));
            }else{
                mMediaTime.setText(String.format("%02d:%02d:%02d / %02d:%02d:%02d",
                        cHours, cMinutes, cSeconds, dHours, dMinutes, dSeconds));
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
        if (nPanel != null) {
            nPanel.notificationCancel();
        }
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
                        File audioThumb = new File(getExternalFilesDir(null),
                                audioThumb().substring(audioThumb().lastIndexOf("/") + 1));
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
                Utils.goBackwardAnimation(this);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            calculateAudioThumb();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            calculateAudioThumb();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.audio_player, menu);
        return true;
    }
}