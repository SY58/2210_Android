package com.example.step10viewpager.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.step10viewpager.databinding.FragmentMainBinding;

/**
 * A placeholder fragment containing a simple view.
 */

public class PlaceholderFragment extends Fragment {

    //private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private FragmentMainBinding binding;

    //인자로 전달하는 인덱스에 해당하는 새 Fragment(PlaceholderFragment) 객체를 리턴하는 메소드
    public static PlaceholderFragment newInstance(String ownerName) {
        //fragment 객체를 생성하고
        PlaceholderFragment fragment = new PlaceholderFragment();
        //Bundle 객체를 생성해서
        Bundle bundle = new Bundle();
        //"ownerName"이라는 키값으로 전달된 이름을 담고
        bundle.putString("ownerName", ownerName);
        //Fragment 에 전달하고
        fragment.setArguments(bundle);
        //해당 fragment 객체를 리턴해준다.
       return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //PageViewModel 을 사용할 준비하기
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);

        //Fragment에 전달받은 인자(Bundle)을 얻어낸다.
        Bundle bundle=getArguments();
        //Bundle 객체에 "ownerName"이라는 키값으로 담겨있는 이름을 얻어낸다.
        String ownerName=bundle.getString("ownerName");
        //MutableLiveData를 수정한다.
        pageViewModel.setOwnerName(ownerName);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        //fragment_main.xml 문서를 전개해서 만든 view의 참조값
        View root = binding.getRoot();
        //textview의 참조값을 얻어와서
        final TextView textView = binding.sectionLabel;
        //PageView 모델이 가지고 있는 데이터를 관찰하고 있다가 혹시 변경이 되면 UI를 업데이트할 옵저버 등록
        //단, 이 뷰의 주인(프래그먼트 혹은 액티비티)가 활성화된 상태에서만 동작하겠다는 의미
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            //가공된 문자열이 들어온다.
            @Override
            public void onChanged(@Nullable String s) {
                //textView 문자열을 출력하기
                textView.setText(s);
            }
        });
        //버튼을 눌렀을 때 동작할 리스너 등록
        binding.changeBtn.setOnClickListener(view -> {
            //입력한 이름을 읽어와서
            String newName=binding.inputName.getText().toString();
            //PageViewModel 이 가지고 있는 라이브 데이터를 업데이트한다.
            pageViewModel.setOwnerName(newName);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}