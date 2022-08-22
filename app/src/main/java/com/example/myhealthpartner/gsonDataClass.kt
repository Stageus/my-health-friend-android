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
    val message : Message,
)
data class Message(
    val promisedate : Long,
    val location : String,
    val messageDetailed :String
)

data class BoardData(
    val myTownBoard : MyTownBoard,
    val exerciseTipBoard : ExerciseTipBoard
)

data class ExerciseTipBoard(
    val postList: ArrayList<PostList>
)

data class MyTownBoard(
    val postList : ArrayList<PostList>
)

data class PostList(
    val postTitle : String,
    val postContent : String,
    val postRecommend : Int,
    val postTime : String,
    val postComment : ArrayList<PostComment>
)

data class PostComment(
    val nickname :String,
    val comment : String
)

