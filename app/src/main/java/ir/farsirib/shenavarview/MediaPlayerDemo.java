package ir.farsirib.shenavarview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;

import java.io.IOException;

import ir.farsirib.R;

@SuppressLint("Registered")
class MediaPlayerDemo extends Activity implements TextureView.SurfaceTextureListener {

    private MediaPlayer mMediaPlayer;

    private TextureView mPreview;

    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);

        mPreview = (TextureView) findViewById(R.id.surface);
        mPreview.setSurfaceTextureListener(this);
        Bundle extras = getIntent().getExtras();
        setContentView(mPreview);
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Surface s = new Surface(surface);

        try {
            mMediaPlayer = new MediaPlayer();
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.asl);
            mMediaPlayer.setDataSource(this, uri);
            mMediaPlayer.setSurface(s);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnBufferingUpdateListener((MediaPlayer.OnBufferingUpdateListener) this);
            mMediaPlayer.setOnCompletionListener((MediaPlayer.OnCompletionListener) this);
            mMediaPlayer.setOnPreparedListener((MediaPlayer.OnPreparedListener) this);
            mMediaPlayer.setOnVideoSizeChangedListener((MediaPlayer.OnVideoSizeChangedListener) this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}