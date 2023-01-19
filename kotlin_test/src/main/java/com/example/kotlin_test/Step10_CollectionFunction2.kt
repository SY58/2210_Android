package com.example.kotlin_test

val nums2= listOf<Int>(1,2,3,4,5,6,7,8,9,10)

//친구의 나이와 이름을 저장하는 데이터 클래스
data class Friend(val age:Int, val name:String)

fun main(){
    //연속으로 함수를 호출할 수 있다.
    val result=nums2.filter { it>5 }.map { it*2 }
    println(result)

    //nums2 목록에서 짝수만 골라서 제곱을 한 결과 목록을 얻어내 보세요.
    val result2=nums2.filter { it%2 == 0 }.map { it*it }
    println(result2)

    val f1=Friend(30,"바나나")
    val f2=Friend(42,"딸기")
    val f3=Friend(38,"복숭아")
    val f4=Friend(25,"사과")
    val f5=Friend(29, "오렌지")

    val friends= listOf(f1,f2,f3,f4,f5)

    //나이가 30 이상인 친구의 이름에 "님"을 붙인 새로운 목록
    val result3=friends.filter { it.age>=30 }.map { it.name+"님" }
    println(result3)
}