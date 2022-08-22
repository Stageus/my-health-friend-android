package com.example.myhealthpartner

data class UserData(
    val user : ArrayList<User>
)


data class User(
    val id : String,
    val pw : String,
    val userDataList : ArrayList<UserDataList>,
    val findUserDataList : ArrayList<FindUserDataList>,
    val matchingReceiveList: ArrayList<MatchingReceiveList>

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
    val lat : Double,
    val lng : Double,
    val exerciseType : String,
    val rpm : Int,
    val age : Int,
    val exerciseTime : String,
    val introduce : String
)

data class MatchingReceiveList(
    val id : String,
    val date : Long,
    val msg : ArrayList<message>,
)
data class message(
    val promisedate : Long,
    val location : String,
    val messageDetailed :String
)



