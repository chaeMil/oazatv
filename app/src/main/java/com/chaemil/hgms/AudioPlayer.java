package com.chaemil.hgms;




import java.io.IOException;

import android.app.Activity;
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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AudioPlayer extends Activity implements OnPreparedListener {

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
    NotificationCompat.Builder builder;

    public String file() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getString("audioFile");
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
                // TODO Auto-generated method stub
                try {
                    mPlayer.prepare();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mPlayer.start();
                mSeekBarPlayer.postDelayed(onEverySecond, 1000);
            }
        });


        btnPouse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPlayer.pause();
            }
        });

        btnFf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPlayer.seekTo(mPlayer.getCurrentPosition()+20000);
            }
        });

        btnRew.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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
            if(true == running){
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
            System.out.println("duration - " + duration + " current- "
                    + current);
            int dSeconds = (int) (duration / 1000) % 60 ;
            int dMinutes = (int) ((duration / (1000*60)) % 60);
            int dHours   = (int) ((duration / (1000*60*60)) % 24);

            int cSeconds = (int) (current / 1000) % 60 ;
            int cMinutes = (int) ((current / (1000*60)) % 60);
            int cHours   = (int) ((current / (1000*60*60)) % 24);

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
        // TODO Auto-generated method stub
        duration = mPlayer.getDuration();
        mSeekBarPlayer.setMax(duration);
        mSeekBarPlayer.postDelayed(onEverySecond, 1000);
    }

    public void exitPlayer() {
        //mVideoView.stopPlayback();
        mPlayer.stop();
    }

    public void deleteAudio() {
        /*new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(getString(R.string.deleteAudio)+"?")
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitPlayer();
                        String path = new File(file().replace("file:", "")).getParent();
                        File file = new File(path, file().substring(file().lastIndexOf("/")+1));
                        //Toast.makeText(getApplicationContext(), file.toString(), Toast.LENGTH_LONG).show();
                        file.delete();
                        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right);
                        finish();
                    }

                })
                .setNegativeButton(getString(R.string.no), null)
                .show();*/
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    exitPlayer();
                    //overridePendingTransition(R.anim.fade_in, R.anim.slide_out_right);
                    finish();
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
                finish();
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