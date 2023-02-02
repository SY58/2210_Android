package com.example.step17example;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.step17example.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    view binding 사용하는 방법

    1. build.gradle 파일에 아래 설정 추가
        buildFeatures {
            viewBinding = true
        }

    2. 우상단의 sync now 링크를 눌러서 설정이 적용되도록 한다.
    3. layout xml 문서의 이름대로 클래스가 자동으로 만들어진다.
       예를 들어 activity_main.xml 문서면 ActivityMainBinding 클래스
                activity_sub.xml 문서면 ActivitySubBinding 클래스
 */
public class MainActivity extends AppCompatActivity implements Util.RequestListener, AdapterView.OnItemLongClickListener {
    //자주 사용하는 문자열은 static final 상수로 만들어두고 사용하면 편리하다.
    public static final String BASE_URL="http://192.168.0.34:9000/boot07/";
    //필드
    List<TodoDto> list;
    TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);

        //R.layout.activity_main.xml 문서를 전개해서 View를 만들기
        ActivityMainBinding binding=ActivityMainBinding.inflate(getLayoutInflater());
        //전개된 layout에서 root를 얻어내서 화면 구성을 한다.(여기서는 LinearLayout 이다.)
        setContentView(binding.getRoot());

        //아답타에 넣어줄 모델 목록
        list=new ArrayList<>();
        //listView에 연결할 아답타
        adapter=new TodoAdapter(this, R.layout.listview_cell, list);
        //listView에 아답타 연결하기
        binding.listView.setAdapter(adapter);


        //UI의 참조값 얻어오기
        EditText inputText=findViewById(R.id.inputText);

        //버튼의 참조값 얻어오기, 리스너 등록
        binding.addBtn.setOnClickListener(v -> {
            //EditText에 입력한 문자열을 읽어와서
            String content = binding.inputText.getText().toString();
            Map<String, String> map=new HashMap<>();
            map.put("content", content);
            //원격지 웹서버에 post 방식으로 전송하기
            Util.sendPostRequest(AppConstants.REQUEST_TODO_INSERT,
                                AppConstants.BASE_URL+"/todo/insert",
                                map,
                                this);
        });

        //ListView를 길게 누르고 있을 때 리스너 등록
        binding.listView.setOnItemLongClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //MainActivity가 활성화되는 시점에 원격지 서버의 데이터를 받아와서 listView에 출력하기
        Util.sendGetRequest(AppConstants.REQUEST_TODO_LIST,
                AppConstants.BASE_URL+"/todo/list",
                null,
                this);
    }

    @Override
    public void onSuccess(int requestId, Map<String, Object> result) {
        //응답된 json 문자열 읽어오기
        String jsonStr=(String)result.get("data");

        //만일 할일 추가 요청에 대한 응답이라면
        if(requestId == AppConstants.REQUEST_TODO_INSERT){
            //jsonStr 은 {"isSuccess":true} 형식의 json 문자열
            Log.d("MainActivity onSuccess()", jsonStr);
            //성공이면 목록을 다시 요청해서 UI가 업데이트되도록 한다.
            Util.sendGetRequest(AppConstants.REQUEST_TODO_LIST,
                    AppConstants.BASE_URL+"/todo/list",
                    null,
                    this);

        }else if(requestId == AppConstants.REQUEST_TODO_LIST){
            //기존 목록은 일단 삭제
            list.clear();
            //jsonStr 은 [{"num":x, "content":"x", "regdate":"x"}, {},{},{},...] 형식의 문자열
            try {
                JSONArray arr=new JSONArray(jsonStr);
                //JSONArray 객체의 방의 개수만큼 반복문 돌면서
                for(int i=0; i<arr.length(); i++){
                    //JSONObject 객체를 하나씩 얻어낸다.
                    JSONObject tmp=arr.getJSONObject(i);
                    //JSONObject 에는 할일 하나가 들어있다. => TodoDto로 변경하면 된다.
                    TodoDto dto=new TodoDto();
                    dto.setNum(tmp.getInt("num"));
                    dto.setContent(tmp.getString("content"));
                    dto.setRegdate(tmp.getString("regdate"));
                    //TodoDto 객체를 list에 누적시킨다.
                    list.add(dto);
                }
                //모두 누적시켰으면 모델이 변경되었다고 아답타에 알려서 ListView가 업데이트 되도록 한다.
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.e("MainActivity onSuccess()", e.getMessage());
            }
        }else if(requestId == AppConstants.REQUEST_TODO_DELETE){
            //성공이면 목록을 다시 요청해서 UI가 업데이트되도록 한다.
            Util.sendGetRequest(AppConstants.REQUEST_TODO_LIST,
                    AppConstants.BASE_URL+"/todo/list",
                    null,
                    this);
        }
    }

    @Override
    public void onFail(int requestId, Map<String, Object> result) {
        //만일 할일 추가 요청에 대한 실패라면
        if(requestId==AppConstants.REQUEST_TODO_INSERT){

        }
    }

    //listView의 cell을 오랫동안 클릭하면 실행되는 메소드
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        new AlertDialog.Builder(this).setTitle("알림")
                .setMessage("삭제하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*
                            view는 클릭한 cell의 view
                            position은 클릭한 cell의 인덱스
                            id는 클릭한 cell 모델의 primary key 값
                         */
                        //삭제할 할일의 primary key 값을 Map에 담고
                        Map<String, String> map = new HashMap<>();
                        map.put("num", Long.toString(id));
                        //Util을 사용해서 삭제
                        Util.sendPostRequest(AppConstants.REQUEST_TODO_DELETE,
                                AppConstants.BASE_URL+"/todo/delete",
                                map,
                                MainActivity.this);
                    }
                })
                .setNegativeButton("아니오", null)
                .create()
                .show();


        return false;

    }
}