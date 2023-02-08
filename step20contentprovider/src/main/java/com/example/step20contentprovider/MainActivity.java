package com.example.step20contentprovider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    //필요한 필드 정의하기
    EditText inputName, console;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //EditText 객체의 참조값 얻어내서 필드에 저장하기
        inputName=findViewById(R.id.inputName);
        console=findViewById(R.id.console);

        Button getBtn=findViewById(R.id.getBtn);
        //연락처 정보 얻어오기 버튼을 눌렀을 때
        getBtn.setOnClickListener(v -> {
            //연락처 정보 얻어오기 권한이 체크되었는지 상수값 얻어오기
            int permissionCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

            if(permissionCheck != PackageManager.PERMISSION_GRANTED){
                //권한이 필요한 목록을 배열에 담는다.
                String[] permissions={Manifest.permission.READ_CONTACTS};
                //배열을 전달해서 해당 권한을 부여하도록 요청한다.
                ActivityCompat.requestPermissions(this,
                        permissions,
                        0); //요청의 아이디
                return; //메소드는 여기서 종료
            }
            //연락처 정보 얻어오기
            getContacts();
        });
    }
    //퍼미션 요청의 결과가 전달되는 메소드 재정의하기
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        switch (requestCode){
            case 0: //0번 요청인 경우
                //권한을 부여했다면
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //연락처 정보 얻어오기
                    getContacts();
                }else{//권한을 부여 하지 않았다면
                    Toast.makeText(this, "연락처 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //ContentProvider 로부터 ContentResolver 객체를 이용해서 연락처 정보를 얻어내는 메소드
    public void getContacts(){
        //입력한 검색어
        String keyword=inputName.getText().toString();
        //ContentResolver 객체의 참조값을 얻어오기
        ContentResolver resolver=getContentResolver();
        //연락처 정보를 지칭할 수 있는 Uri 객체 얻어내기
        Uri contactUri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        //select할 칼럼
        String[] columns={
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        };
        //where display_name like '%키워드%'
        String where=ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" LIKE ? ";
        //order by contact_id asc
        String order=ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" ASC";
        //?에 바인딩할 인자
        String[] args = {"%"+keyword+"%"};
        //원하는 정보를 얻어낸다.
        Cursor cursor=resolver.query(contactUri,  //table name
                columns,  //column name
                where,  //where
                args,  //selection args
                order);  //order by
        while (cursor.moveToNext()){
                int id=(int)cursor.getLong(0);
                String phoneNumber=cursor.getString(1);
                String name=cursor.getString(2);
                //결과를 한줄의 문자열로 구성해서
                String result=id+" | "+phoneNumber+" | "+name;
                console.append(result+"\n");

        }
    }
}