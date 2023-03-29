package com.example.step26listview;

import android.app.Activity;
import android.os.Bundle;

import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.example.step26listview.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ActivityMainBinding binding;

    WearableRecyclerView recyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyView=binding.recyView;

        //아이템이 가운데 정렬되도록 설정
        recyView.setEdgeItemsCenteringEnabled(true);
        //레이아웃 매니저 설정
        recyView.setLayoutManager(new WearableLinearLayoutManager(this));

        //출력할 Data(모델)
        List<String> names=new ArrayList<>();
        names.add("바나나");
        names.add("딸기");
        names.add("복숭아");
        names.add("사과");
        names.add("오렌지");
        names.add("무화과");
        names.add("포도");
        names.add("레몬");
        names.add("라임");
        //RecyclerView에 연결할 아답타 객체 생성
        RecyAdapter adapter=new RecyAdapter(names);
        //RecyclerView에 아답타 연결
        recyView.setAdapter(adapter);

        recyView.setHasFixedSize(true);
    }
}