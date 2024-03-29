package com.example.step17httprequest2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements Util.RequestListener {
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.editText);

        EditText inputUrl=findViewById(R.id.inputUrl);
        Button requestBtn=findViewById(R.id.requestBtn);
        //요청 버튼을 클릭했을때 동작할 준비
        requestBtn.setOnClickListener(v -> {
            //입력한 요청 url을 읽어온다.
            String url=inputUrl.getText().toString();
            //Util 클래스의 static 메소드를 활용해서 GET방식 요청하고 결과 받아오기
            Util.sendGetRequest(999, url, null, this);
        });
    }

    @Override
    public void onSuccess(int requestId, Map<String, Object> result) {
        if(requestId == 999){
            //Map에 data라는 키값으로 담긴 String type을 읽어온다.
            String data=(String)result.get("data");
            editText.setText(data);
        }
    }

    @Override
    public void onFail(int requestId, Map<String, Object> result) {
        //에러 메시지를 읽어와서 EditText에 출력하기
        String data=(String) result.get("data");
        editText.setText(data);
    }
}