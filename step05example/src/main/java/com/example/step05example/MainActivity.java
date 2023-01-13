package com.example.step05example;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //필요한 필드 선언
    EditText editText;
    List<String> names;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //필요한 UI의 참조값을 UI에 부여된 id를 이용해서 얻어오기
        listView=findViewById(R.id.listView);
        editText=findViewById(R.id.editText);
        Button addBtn=findViewById(R.id.addBtn);
        //버튼 리스너 등록
        addBtn.setOnClickListener(this);
        //model생성
        names=new ArrayList<>();
        //adapter 생성
        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                names);
        //listView에 아답타 연결하기
        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        //1. EditText에 입력한 문자열을 읽어온다.
        String inputName=editText.getText().toString();
        //2. 모델에 데이터 추가
        names.add(inputName);
        //3. ListView가 업데이트할 수 있도록 아답타에 모델이 수정되었다고 전달
        adapter.notifyDataSetChanged();
        //4. EditText에 입력한 문자열 삭제
        editText.setText("");
    }
}