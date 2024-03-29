package com.example.step17example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TodoAdapter extends BaseAdapter {

    //필드
    private Context context;
    private int layoutRes;
    private List<TodoDto> list;

    //생성자
    public TodoAdapter(Context context, int layoutRes, List<TodoDto> list) {
        this.context = context; //액티비티의 참조값
        this.layoutRes = layoutRes; //listView의 cell layout 리소스 아이디
        this.list = list; //listView에 출력할 데이터를 가지고 있는 모델
    }

    //모델의 개수 리턴
    @Override
    public int getCount() {
        return list.size();
    }
    //position에 해당하는 모델을 리턴
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    //position에 해당하는 아이템의 아이디(primary key)
    @Override
    public long getItemId(int position) {
        //position에 해당하는 TodoDto의 번호를 리턴하는 것
        return list.get(position).getNum();
    }
    //position에 해당하는 cell View를 리턴해주어야 한다. (listView가 호출할 예정인 메소드)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //만일 view를 새로 만들어야 한다면
        if(convertView == null){
            //여기서 만들고 (res/layout/listView_cell.xml 문서를 전개해서)
            //해당 문서의 리소스 아이디는 생성자의 인자로 전달되어서 필드에 저장해 놓은 상태
            convertView=LayoutInflater.from(context).inflate(layoutRes, parent, false);
        }
        //View에 모델이 가지고 있는 데이터를 출력하고
        TodoDto dto=list.get(position);
        //listView_cell.xml 안에 있는 TextView의 참조값 얻어와서 문자열 출력
        TextView text_content=convertView.findViewById(R.id.text_content);
        TextView text_regdate=convertView.findViewById(R.id.text_regdate);
        text_content.setText(dto.getContent());
        text_regdate.setText(dto.getRegdate());
        //View를 리턴해준다.
        return convertView;
    }
}
