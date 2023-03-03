package com.example.step25imagecapture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    //저장된 이미지의 전체 경로
    String imagePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //사진을 출력할 ImageView의 참조값 필드에 저장하기
        imageView=findViewById(R.id.imageView);

        Button takePicture=findViewById(R.id.takePicture);
        takePicture.setOnClickListener(v -> {
            //사진을 찍고 싶다는 Intent 객체 작성하기
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //운영체제에 intent를 처리할 수 있는 App을 실행시켜 달라고 하고, 결과 값도 받아올 수 있도록 한다.
            startActivityForResult(intent, 0);
        });

        Button takePicture2=findViewById(R.id.takePicture2);
        takePicture2.setOnClickListener(v -> {
            //사진을 찍고 싶다는 Intent 객체 작성하기
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //외부 저장 장치의 절대 경로
            String absolutePath=getExternalFilesDir(null).getAbsolutePath();
            //파일명 구성
            String fileName= UUID.randomUUID().toString()+".jpg";
            //생성할 이미지의 전체 경로
            imagePath=absolutePath+"/"+fileName;
            //이미지 파일을 저장할 File 객체
            File photoFile=new File(imagePath);
            //File객체를 Uri로 포장한다.
            //Uri uri=Uri.fromFile(photoFile); //오류 발생!
            Uri uri= FileProvider.getUriForFile(this,
                    "com.example.step25imagecapture.fileprovider",
                            photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent,1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //만일 위에서 요청한 요청과 같고 결과가 성공적이라면
        if(requestCode == 0 && resultCode == RESULT_OK){
            //data Intent 객체에 결과값(썸네일 이미지 데이터) 가 들어 있다.
            Bitmap image=(Bitmap)data.getExtras().get("data");
            //ImageView에 출력하기
            imageView.setImageBitmap(image);
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            //만일 여기가 실행된다면 imagePath 경로에 이미지 파일이 성공적으로 만들어진 것이다.
            Bitmap image=BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(image);
        }

    }
}