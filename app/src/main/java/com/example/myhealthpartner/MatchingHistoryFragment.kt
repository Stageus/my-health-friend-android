package com.example.myhealthpartner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.gson.Gson

class MatchingHistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.matching_history_fragment, container, false)
        initEvent(view)
        return view
    }

    fun initData(): UserData? { //json파일 읽어오기 작업
        val jsonObject : String
        jsonObject = requireActivity().assets.open("userData.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val userData = gson.fromJson(jsonObject, UserData::class.java)
        return userData
    }

    fun setContent(myView: View, userData: UserData){
        val contentBox = myView.findViewById<LinearLayout>(R.id.contentBox)
        val loginData = context?.getSharedPreferences("loginData", 0)
        val userIndex = loginData?.getInt("userIndex", 0)
        for(index in 0 until userData.user[userIndex!!].matchingReceiveList.size){
            val content = layoutInflater.inflate(R.layout.matching_recieve_page_view, contentBox, false)
            content.findViewById<TextView>(R.id.nickname).text = userData.user[userIndex].matchingReceiveList[index].id
            content.findViewById<Button>(R.id.btn1).text = "프로필 이동"
            content.findViewById<Button>(R.id.btn2).text = "운동종료/평가"
            Glide.with(this) //이미지 적용
                .load(R.mipmap.temp)
                .circleCrop()
                .into(content.findViewById(R.id.symbol))
            contentBox.addView(content)
        }

    }


    fun initEvent(myView : View){
        val userData = initData()
        setContent(myView, userData!!)

    }

}