package com.example.step07fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements MyFragment.MyFragmentListener {
    MyFragment mf1, mf2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main2);
        //전개된 view에는 MyFragment 객체가 2개 있다. 만일 해당 객체의 참조값이 액티비티에서 필요하다면?

        //FragmentManager 객체의 참조값을 얻어내서
        FragmentManager fm=getSupportFragmentManager();
        //해당 객체의 메소드를 활용해서 프래그먼트의 참조값을 얻어낸다.
        mf1=(MyFragment) fm.findFragmentById(R.id.fragment1);
        mf2=(MyFragment) fm.findFragmentById(R.id.fragment2);

        //만일 FragmentContainerView의 참조값이 필요하다면 액티비티의 메소드를 이용해서 얻어낼 수 있다.
        FragmentContainerView container1=findViewById(R.id.fragment1);
        FragmentContainerView container2=findViewById(R.id.fragment2);
    }

    @Override
    public void sendMsg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public void resetFragment(View v){
        mf1.reset();
        mf2.reset();
    }
}