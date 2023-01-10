package com.example.myandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

//app을 실행했을 때 처음 사용자를 대면하는 MainActivity
//manifests/AndroideManifests.xml 문서에 해당 정보가 있다.
public class MainActivity extends AppCompatActivity {

    //액티비티가 활성화될 때 onCreate() 메소드가 호출된다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //res/layout/activity_main.xml 문서를 전개해서 화면 구성하기
        setContentView(R.layout.activity_main);
        //액티비티가 활성화되는 시점에 원하는 작업이 있으면 여기에 코딩한다.
    }
    /*
        activity_main.xml에 정의된 버튼을 눌렀을 때 호출되는 메소드 정의하기
        메소드의 인자로 View type을 전달받도록 만들어야 한다.
     */
    public void sendClicked(View v){
        //로그 창에 문자열 출력하기(System.out.println() 이 아니다.)
        Log.d("나의 tag","전송 버튼이 눌러졌네?");
        //사용자를 방해하지 않고 잠시 떴다가 사라지는 토스트 메세지
        Toast.makeText(this, "전송 버튼을 눌렀네?",Toast.LENGTH_SHORT).show();
    }
    /*
        Delete 버튼을 하나 원하는 위치에 배치하고
        해당 버튼을 눌렀을 때 "삭제 버튼을 눌렀네?" 라는 토스트 메시지가 길게 출력되도록 해 보세요.
     */
    public void deleteClicked(View v){
        Toast.makeText(this, "삭제 버튼을 눌렀네?", Toast.LENGTH_LONG).show();
    }
}