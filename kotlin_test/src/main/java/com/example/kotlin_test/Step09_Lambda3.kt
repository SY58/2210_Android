package com.example.kotlin_test

//함수 type을 매개변수로 전달받는 함수
fun useFunc(f:()->Unit){
    //인자로 전달받은 함수 호출하기
    f()
}

//인터페이스 정의하기
interface Drill{
    fun hole()
}

fun useDrill(d:Drill){
    d.hole()
}

fun main(){

    useDrill(object:Drill{
        override fun hole() {
            println("구멍을 뚨어요!")
        }
    })

    //원래 모양
    useFunc(fun(){
        println("익명함수 호출됨! 1")
    })
    //fun() 생략
    useFunc({
        println("익명함수 호출됨! 2")
    })
    //위를 좀더 간단히 한 최종 모양
    useFunc {
        println("익명함수 호출됨! 3")
    }


}