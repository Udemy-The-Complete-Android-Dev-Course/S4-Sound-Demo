package ldansorean.s4sounddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.util.Timer;
import java.util.TimerTask;

import static android.media.AudioManager.STREAM_MUSIC;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(this, R.raw.rainforest);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        setupVolumeSeeker();
        setupProgressSeeker();
    }

    private void setupProgressSeeker() {
        final SeekBar progressSeekBar = findViewById(R.id.progress);
        progressSeekBar.setMax(mediaPlayer.getDuration());

        //seek bar should be updated as song keeps playing
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                progressSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        }, 0, 1000);

        //when manually updating seekbar should jump to the position in the audio file
        progressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.start();
            }
        });
    }

    private void setupVolumeSeeker() {
        SeekBar volumeSeekBar = findViewById(R.id.volume);

        int maxVolume = audioManager.getStreamMaxVolume(STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(STREAM_MUSIC);
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //noop
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //noop
            }
        });
    }

    public void play(View view) {
        mediaPlayer.start();
    }

    public void pause(View view) {
        mediaPlayer.pause();
    }
}
