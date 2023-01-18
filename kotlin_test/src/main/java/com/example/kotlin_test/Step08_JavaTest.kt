package com.example.kotlin_test
import com.example.kotlin_test.java.MemberDto

fun main(){
    //kotlin에서 java 클래스도 자유롭게 import해서 사용할 수 있다.
    val mem1=com.example.kotlin_test.java.Member()
    mem1.num=1
    mem1.name="바나나"
    mem1.addr="서울"
    mem1.showInfo()

    val mem2=MemberDto()
    //내부적으로 java의 setter 메소드가 호출된다.
    mem2.num=2
    mem2.name="딸기"
    mem2.addr="부산"

    //내부적으로 java의 getter 메소드가
    val a = mem2.num;
    val b = mem2.name;
    val c = mem2.addr;
}