package com.example.step23mp3player;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;
import java.util.concurrent.TimeUnit;

/*
    MusicService를 이용해서 음악을 재생하는 방법

    - initMusic() 메소드를 호출하면서 음원의 위치를 넣어주고
    - 음원 로딩이 완료되면 자동으로 play 된다.
 */
public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    //다음 곡으로 자동 이동했는지 감시할 리스너 인터페이스
    public interface OnMoveToListener{
        public void moved(int index);
    }

    //필요한 필드 정의하기
    MediaPlayer mp;
    boolean isPrepared; //음원 재생 준비가 완료되었는지 여부
    //음악 재생 목록
    List<MusicDto> musicList;
    //현재 재생중인 음악 목록 인덱스
    int currentIndex;

    public OnMoveToListener listener;
    //현재 재생 위치를 리턴하는 메소드(액티비티가 호출해서 받앙갈 예정)
    public int getCurrentIndex(){
        return currentIndex;
    }

    //MainActivity의 참조값이 OnMoveToListener type 으로 전달되는 메소드
    public void setOnMoveToListener(OnMoveToListener listener){
        this.listener=listener;
    }

    //액티비티로부터 재생할 음악목록을 전달받는 메소드
    public void setMusicList(List<MusicDto> musicList) {
        this.musicList = musicList;
    }

    //음원을 로딩하는 메소드 url을 넣어주면 해당 url의 음악을 로딩하는 메소드
    public void initMusic(int index) {
        //현재 재생중인 인덱스 수정
        currentIndex=index;
        isPrepared = false;
        if (mp == null) {
            mp = new MediaPlayer();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setOnPreparedListener(this); //음원 로딩이 완료되었는지 감시할 리스너 등록
            mp.setOnCompletionListener(this);
            mp.setLooping(false);
        }
        //만일 현재 재생중이면
        if (mp.isPlaying()) {
            mp.stop(); //재생을 중지하고
        }
        mp.reset(); //초기화
        try {
            //로딩할 음원의 위치 구성하기
            String url=AppConstants.MUSIC_URL+musicList.get(index).getSaveFileName();
            //로딩할 음원의 위치를 넣어주고
            mp.setDataSource(url);
        } catch (Exception e) {
            Log.e("initMusic()", e.getMessage());
        }
        //비동기로 로딩을 시킨다.
        mp.prepareAsync();
    }

    //재생하는 메소드
    public void playMusic() {
        //만일 음악이 준비되지 않았다면
        if(!isPrepared)return; //메소드를 여기서 끝내라
        mp.start();
    }

    //일시정지하는 메소드
    public void pauseMusic() {
        //만일 음악이 준비되지 않았다면
        if(!isPrepared)return; //메소드를 여기서 끝내라
        mp.pause();
    }

    //정지하는 메소드
    public void stopMusic() {
        //만일 음악이 준비되지 않았다면
        if(!isPrepared)return; //메소드를 여기서 끝내라
        mp.stop();
    }
    //뒤로 되감는 기능
    public void rewMusic(){
        //만일 음악이 준비되지 않았다면
        if(!isPrepared)return; //메소드를 여기서 끝내라
        //현재 재생 위치에서 뒤로 10초
        int current=mp.getCurrentPosition();
        int backPoint=current-10*1000;
        //음수가 되면 안되기 때문에 backPoint가 0 이상일 때만 동작하도록 한다.
        if(backPoint >= 0){
            mp.seekTo(backPoint);
        }
    }
    //앞으로 감는 기능
    public void ffMusic(){
        //만일 음악이 준비되지 않았다면
        if(!isPrepared)return; //메소드를 여기서 끝내라
        //현재 재생 위치에서 앞으로 10초
        int current=mp.getCurrentPosition();
        int frontPoint=current+10*1000;
        //전체 재생 시간보다는 작아야 되기 때문에
        if(frontPoint <= mp.getDuration()){
            mp.seekTo(frontPoint);
        }
    }

    //재생이 준비되었는지 여부를 리턴하는 메소드
    public boolean isPrepared() {
        return isPrepared;
    }

    //MediaPlayer 객체의 참조값을 리턴하는 메소드
    public MediaPlayer getMp() {
        return mp;
    }

    //서비스가 최초 활성화될 때 한번 호출되는 메소드
    @Override
    public void onCreate() {
        super.onCreate();
    }

    //최초 활성화 혹은 이미 활성화된 이후 이 서비스를 활성화 하는 Intent가 도착하면 호출되는 메소드
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //알림에 띄워진 액션 버튼을 눌렀을때 분기해서 필요한 동작을 한다.
        switch (intent.getAction()) {
            case AppConstants.ACTION_PLAY:
                Log.d("onStartCommand()", "play!");
                playMusic();
                mp.seekTo(200000);
                break;
            case AppConstants.ACTION_PAUSE:
                Log.d("onStartCommand()", "pause!");
                pauseMusic();
                break;
            case AppConstants.ACTION_STOP:
                Log.d("onStartCommand()", "stop!");
                stopMusic();
                break;
        }
        return START_NOT_STICKY;
    }
    //음원 재생이 완료되었을 때 호출되는 메소드
    @Override
    public void onCompletion(MediaPlayer mp) {
        //재생할 음악 목록의 마지막 인덱스
        int lastIndex=musicList.size() - 1;
        //만일 현재 재생중인 인덱스가 마지막 번째 인덱스보다 작다면(마지막 인덱스가 아니라면)
        if(currentIndex < lastIndex){
            currentIndex++;
            initMusic(currentIndex);
        }else{
            //만일 무한 플레이를 하려면
            currentIndex=0;
            initMusic(currentIndex);
        }
        if(listener != null){
            // OnMoveToListener(MainActivity) 객체의 moved 메소드를 호출하면서 현재 위치 전달
            listener.moved(currentIndex);
        }
    }

    //Binder 클래스를 상속받아서 LocalBinder 클래스를 정의한다.
    public class LocalBinder extends Binder {
        //서비스의 참조값을 리턴해주는 메소드
        public MusicService getService() {
            Log.e("####", "리턴함");
            return MusicService.this;
        }
    }

    //필드에 바인더 객체의 참조값 넣어두기
    final IBinder binder = new LocalBinder();

    //어디에선가(액티비티) 바인딩(연결)이 되면 호출되는 메소드
    @Override
    public IBinder onBind(Intent intent) {

        return binder;
    }
    //어디에선가(액티비티) 바인딩(연결)이 해제되면 호출되는 메소드
    @Override
    public boolean onUnbind(Intent intent) {
        //OnMoveToListener 를 제거한다.
        listener=null;
        return super.onUnbind(intent);
    }

    //새로운 음원 로딩이 완료되면 호출되는 메소드
    @Override
    public void onPrepared(MediaPlayer mp) {
        //재생할 준비가 되었다고 상태값을 바꿔준다.
        isPrepared = true;
        //준비가 되면 자동으로 재생을 시작한다.
        playMusic();
        handler.removeMessages(0);
        handler.sendEmptyMessageDelayed(0,100);
    }

    @Override
    public void onDestroy() {
        if(mp != null){
            //MediaPlayer 해제하기
            mp.stop();
            mp.release();
            mp = null;
        }
        handler.removeMessages(0);
        super.onDestroy();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //음악을 control 할 수 있는 알림을 띄운다.
            makeManualCancelNoti();
            handler.sendEmptyMessageDelayed(0,100);
        }
    };


    //수동으로 취소하는 알림을 띄우는 메소드
    public void makeManualCancelNoti() {

        if(!isPrepared)return;
        //현재 재생 시간을 문자열로 얻어낸다.
        int currentTime = mp.getCurrentPosition();
        String info = String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(currentTime),
                TimeUnit.MILLISECONDS.toSeconds(currentTime)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentTime)));

        Intent iPlay = new Intent(this, MusicService.class);
        iPlay.setAction(AppConstants.ACTION_PLAY);
        PendingIntent pIntentPlay = PendingIntent.getService(this, 1, iPlay, PendingIntent.FLAG_MUTABLE);

        Intent iPause = new Intent(this, MusicService.class);
        iPlay.setAction(AppConstants.ACTION_PAUSE);
        PendingIntent pIntentPause = PendingIntent.getService(this, 1, iPlay, PendingIntent.FLAG_MUTABLE);

        Intent iStop = new Intent(this, MusicService.class);
        iPlay.setAction(AppConstants.ACTION_STOP);
        PendingIntent pIntentStop = PendingIntent.getService(this, 1, iPlay, PendingIntent.FLAG_MUTABLE);

        //재생중인 음악의 제목
        String songTitle=musicList.get(currentIndex).getTitle();
        //띄울 알림을 구성하기
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, AppConstants.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.star_on) //알림의 아이콘
                .setContentTitle(songTitle) //알림의 제목
                .setContentText(info)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) //알림의 우선순위
                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", pIntentPlay))
                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play, "Pause", pIntentPause))
                .addAction(new NotificationCompat.Action(android.R.drawable.ic_media_play, "Stop", pIntentStop))
                .setProgress(mp.getDuration(), mp.getCurrentPosition(), false)
                //.setContentIntent(pendingIntent)  //인텐트 전달자 객체
                .setAutoCancel(false); //자동 취소 되는 알림인지 여부

        //알림 만들기
        Notification noti = builder.build();

        //만일 알림 권한이 없다면
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            //메소드를 여기서 종료
            return;
        }
        //알림 매니저를 이용해서 알림을 띄운다.
        NotificationManagerCompat.from(this).notify(AppConstants.NOTI_ID, noti);
    }
}