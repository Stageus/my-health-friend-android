package com.example.myhealthpartner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import org.w3c.dom.Text

class InboardPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.board_page_inboard_layout)
        initEvent()
    }

    fun setBoardContent(boardData: BoardData, boardNo : Int){
        val boardType = intent.getIntExtra("boardType", 0)
        if(boardType == 0){
            findViewById<TextView>(R.id.postTitle).text = boardData.myTownBoard.postList[boardNo].postTitle
            findViewById<TextView>(R.id.postContent).text = boardData.myTownBoard.postList[boardNo].postContent
            findViewById<TextView>(R.id.postRecommend).text = "${boardData.myTownBoard.postList[boardNo].postRecommend}추천"
            findViewById<TextView>(R.id.postCommentNumber).text = "댓글 ${boardData.myTownBoard.postList[boardNo].postComment.size}"
        }
        else{
            findViewById<TextView>(R.id.postTitle).text = boardData.exerciseTipBoard.postList[boardNo].postTitle
            findViewById<TextView>(R.id.postContent).text = boardData.exerciseTipBoard.postList[boardNo].postContent
            findViewById<TextView>(R.id.postRecommend).text = "${boardData.exerciseTipBoard.postList[boardNo].postRecommend}추천"
            findViewById<TextView>(R.id.postCommentNumber).text = "댓글 ${boardData.exerciseTipBoard.postList[boardNo].postComment.size}"
        }
    }

    fun setCommentView(boardData: BoardData, boardNo: Int){
        val contentBox = findViewById<LinearLayout>(R.id.contentBox)
        val boardType = intent.getIntExtra("boardType", 0)
        if(boardType == 0){
            for(index in 0 until boardData.myTownBoard.postList[boardNo].postComment.size){
                val content = layoutInflater.inflate(R.layout.board_page_inboard_view, contentBox, false)
                content.findViewById<TextView>(R.id.commentNickname).text = boardData.myTownBoard.postList[boardNo].postComment[index].nickname
                content.findViewById<TextView>(R.id.commentContent).text = boardData.myTownBoard.postList[boardNo].postComment[index].comment
                contentBox.addView(content)
            }
        }
        else {
            for (index in 0 until boardData.exerciseTipBoard.postList[boardNo].postComment.size) {
                val content =
                    layoutInflater.inflate(R.layout.board_page_inboard_view, contentBox, false)
                content.findViewById<TextView>(R.id.commentNickname).text =
                    boardData.exerciseTipBoard.postList[boardNo].postComment[index].nickname
                content.findViewById<TextView>(R.id.commentContent).text =
                    boardData.exerciseTipBoard.postList[boardNo].postComment[index].comment
                contentBox.addView(content)

            }

        }
    }

    fun initData() : BoardData{
        val jsonObject : String
        jsonObject = this.assets.open("boardData.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val boardData = gson.fromJson(jsonObject, BoardData::class.java)
        return boardData
    }

    fun initEvent(){
        val boardData = initData()
        val boardNo = intent.getIntExtra("boardNo", 0)
        setBoardContent(boardData, boardNo)
        setCommentView(boardData, boardNo)


    }
}