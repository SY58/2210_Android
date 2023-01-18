package com.example.step07fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/*
    [Fragment]
    - 액티비티의 제어 하에 존재하는 미니 Controller
    - 재활용을 염두에 두고 만드는 경우가 많다
    - 재활용이라는 것은 여러개의 액티비티에서 활용된다는 의미

    [ Fragment 만드는 방법 ]
    1. Fragment 클래스를 상속받는다.
    2. Fragment 의 layout xml 문서를 만든다.
    3. onCreateView() 메소드를 오버라이딩 한다.
 */

public class MyFragment extends Fragment implements View.OnClickListener {
    //필드
    TextView textView;
    int count=0;

    // layout 으로 사용할 View를 만들어서 리턴해주어야 한다.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //1. fragment_my.xml 문서를 전개해서 View를 만든 다음
        View view=inflater.inflate(R.layout.fragment_my, container, false);
        //만든 View에서 TextView의 참조값을 알아낸다.
        textView=view.findViewById(R.id.textView);
        textView.setOnClickListener(this);
        //2. 해당 View 를 리턴해 준다.
        return view;
    }

    @Override
    public void onClick(View v) {
        //카운트 값을 1 증가시키고
        count++;
        //정수를 문자열로 만들어서 TextView에 출력하기
        textView.setText(Integer.toString(count));

        /*
            만일 count 값이 10의 배수이면 이 fragment를 제어하고 있는 액티비티에 해당 정보를 알려주기!
            - 액티비티의 특정 메소드를 호출하면서 문자열 전달하기
            - 단 특정 액티비티의 의존성은 없어야 된다.
         */

        //이 fragment 를 제어하고 있는 액티비티의 참조값 알아내기.
        FragmentActivity fa = getActivity();

        //혹시 액티비티가 MyFragmentListener 인터페이스를 구현하지않았을 수도 있기 때문에 type을 확인해서 캐스팅한다.
        if(count%10==0 && fa instanceof MyFragmentListener){
            MyFragmentListener ma=(MyFragmentListener)fa;
                    ma.sendMsg(count+" 입니다!");
        }


    }

    //액티비티에서 특정시점에 호출할 예정인 메소드
    public void reset(){
        count=0;
        textView.setText("0");
    }

    //이 fragment 에서 전달하는 메세지를 받을 액티비티에서 구현할 인터페이스를 클래스 안에 정의하기
    public interface MyFragmentListener{
        public void sendMsg(String msg);
    }
}
