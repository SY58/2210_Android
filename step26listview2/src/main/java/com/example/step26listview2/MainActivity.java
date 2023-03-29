package com.example.step26listview2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;

import com.example.step26listview2.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements MemberAdapter.ItemClickListener{

    private TextView mTextView;
    private ActivityMainBinding binding;
    MemberAdapter adapter;
    List<MemberDto> members;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Sample Model
        members=new ArrayList<>();
        members.add(new MemberDto(1, "바나나", "서울"));
        members.add(new MemberDto(2, "딸기", "부산"));
        members.add(new MemberDto(3, "복숭아", "대전"));
        members.add(new MemberDto(4, "사과", "서울"));
        members.add(new MemberDto(5, "오렌지", "서울"));

        WearableRecyclerView recyclerView=binding.recyView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setEdgeItemsCenteringEnabled(true);
        recyclerView.setLayoutManager(new WearableLinearLayoutManager(this));
        //아답타 연결
        adapter=new MemberAdapter(members);
        //RecyclerView에 연결
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void clicked(int index) {
        //Model을 변경하고
        members.get(index).setName(index+" clicked!");
        //Adapter에 Model이 변경되었다고 알린다.
        adapter.notifyDataSetChanged();
    }
}