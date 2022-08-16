package com.example.myhealthpartner

data class UserData(
    val user : ArrayList<User>
)


data class User(
    val id : String,
    val pw : String,
    val userDataList : ArrayList<UserDataList>,
    val findUserDataList : ArrayList<FindUserDataList>

)

data class UserDataList(
    val name : String,
    val tel : String,
    val email : String
)

data class FindUserDataList(
    val nickname : String,
    val career : String,
    val ability : String,
    val address : String,
    val exerciseType : String,
    val rpm : Int,
    val age : Int,
    val exerciseTime : String
)

