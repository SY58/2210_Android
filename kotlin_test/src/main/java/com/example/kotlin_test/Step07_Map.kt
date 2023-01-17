package com.example.kotlin_test

fun main(){
    //수정 불가능한 Map
    val mem=mapOf<String, Any>("num" to 1, "name" to "바나나", "isMan" to false)

    //Map 에 저장된 데이터 참조하는 방법1
    val num=mem.get("num")
    val name=mem.get("name")
    val isMan=mem.get("isMan")

    //Map 에 저장된 데이터 참조하는 방법2
    val num2=mem["num"]
    val name2=mem["name"]
    val isMan2=mem["isMan"]

    //수정 가능한 Map
    val mem2= mutableMapOf<String, Any>()
    //빈 Map에 데이터 넣기 방법1
    mem2.put("num",2)
    mem2.put("name","딸기")
    mem2.put("isMan", true)

    val mem3:MutableMap<String,Any> = mutableMapOf()
    //빈 Map에 데이터 넣기 방법2
    mem3["num"]=3
    mem3["name"]="복숭아"
    mem3["isMan"]=false
}