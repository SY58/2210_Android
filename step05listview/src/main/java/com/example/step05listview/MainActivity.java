package com.example.step05listview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    List<String> names;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //listView의 참조값
        ListView listview=findViewById(R.id.listView);
        
        //listView에 출력할 sample data
        names=new ArrayList<>();
        names.add("바나나");
        names.add("딸기");
        names.add("복숭아");
        for(int i=0;i<100;i++){
            names.add("아무개"+i);
        }
        //listView 에 연결할 아답타 객체
        //new ArrayAdapter<>( Context , layout resource, 모델 ) 
        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                names);
        //listView에 아답타 연결하기
        listview.setAdapter(adapter);
        //Activity를 아이템 클릭 리스너로 등록하기
        listview.setOnItemClickListener(this);
        listview.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // int i는 클릭한 아이템의 인덱스가 들어있다.
        String name=names.get(i);
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        //오랫동안 클릭한 셀에 출력된 이름 읽어오기
        String name=names.get(i);
        //아래의 익명 클래스에서 참조 가능하도록
        int SelectedIndex=i;

        /*AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage(name+" 을 삭제 하시 겠습니까?");
        builder.setNegativeButton("아니요", null);
        builder.setPositiveButton("네", null);
        AlertDialog dialog=builder.create();
        dialog.show();*/

        new AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage(name+"을 삭제하시겠습니까?")
                .setNegativeButton("아니요", null)
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //모델에서 해당 인덱스의 데이터를 삭제한다.
                        names.remove(SelectedIndex);
                        //아답타에 모델에 변화가 생겼다고 알리기
                        adapter.notifyDataSetChanged();
                    }
                })
                .create()
                .show();
        //이벤트 전파를 여기서 막기
        return true;
    }
}