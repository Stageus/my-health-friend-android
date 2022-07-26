package com.example.myhealthpartner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson

class BoardPageMyTownFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.board_page_list_fragment, container, false)
        initEvent(view)
        return view
    }

    fun initData() : BoardData{
        val jsonObject : String
        jsonObject = requireActivity().assets.open("boardData.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val boardData = gson.fromJson(jsonObject, BoardData::class.java)
        return boardData
    }

    fun createBoardList(boardData: BoardData, myView : View){
        val contentBox = myView.findViewById<LinearLayout>(R.id.contentBox)
        for(index in 0 until boardData.myTownBoard.postList.size){
            val content = layoutInflater.inflate(R.layout.board_page_list_view, contentBox, false)
            content.findViewById<TextView>(R.id.postTitle).text = boardData.myTownBoard.postList[index].postTitle
            content.findViewById<TextView>(R.id.postContent).text = boardData.myTownBoard.postList[index].postContent
            content.findViewById<TextView>(R.id.postRecommend).text = boardData.myTownBoard.postList[index].postRecommend.toString()
//            content.findViewById<TextView>(R.id.postCommentNumber).text = boardData.myTownBoard.postList[index].postComment.size.toString()
            content.findViewById<TextView>(R.id.postTime).text = boardData.myTownBoard.postList[index].postTime
            content.setOnClickListener{
                val intent = Intent(context, InboardPageActivity::class.java)
                intent.putExtra("boardNo", index)
                intent.putExtra("boardType", 0)
                startActivity(intent)

            }

            contentBox.addView(content)

        }
    }

    fun initEvent(myView : View){
        val boardData = initData()
        val contentBox = myView.findViewById<LinearLayout>(R.id.contentBox)

        createBoardList(boardData, myView)

    }


}