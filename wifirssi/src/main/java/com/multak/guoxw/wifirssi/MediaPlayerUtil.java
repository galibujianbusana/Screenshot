package com.multak.guoxw.wifirssi;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaPlayerUtil {
    private  MediaPlayer player;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private List<VideoInfo>data=new ArrayList<>();

    private void init(){
        player=new MediaPlayer();
    }

   public MediaPlayerUtil(Context context, SurfaceView surfaceView){
        init();
        this.context=context;
        this.surfaceView=surfaceView;
        this.surfaceHolder=this.surfaceView.getHolder();
        this.surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                player.setDisplay(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                surfaceHolder.setFixedSize(width,height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
               // play("");
                Log.d("VIDEO::", "onCompletion: "+data.size());
                if(data.size()>0){
                    playLocal(data.get(0).getPath());
                }else{
                    play("");
                }
            }
        });
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });


   }



   public void play(String url){


       try {
           if(player.isPlaying()){
               player.stop();
               player.release();

           }
           if(player==null){
               player=new MediaPlayer();
           }
           player.stop();
           player.reset();
           AssetFileDescriptor fileDescriptor = context.getAssets().openFd("test.mp4");
           player.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(), fileDescriptor.getLength());
           player.prepare();


       } catch (IOException e) {
           e.printStackTrace();
       }
   }


   public void playLocal(String local){


       try {
           if(player.isPlaying()){
               player.stop();
               player.release();

           }
           if(player==null){
               player=new MediaPlayer();
           }
           player.stop();
           player.reset();
           player.setDataSource(local);
           player.prepare();


       } catch (IOException e) {
           e.printStackTrace();
       }

   }

   public void setData(List<VideoInfo>data1){
        this.data=data1;
   }
}
