package com.example.myhealthpartner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.gson.Gson

class BoardPageExerciseFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.board_page_list_fragment, container, false)
        initEvent(view)

        return view
    }

    fun initData() : UserData{
        val jsonObject : String
        jsonObject = requireActivity().assets.open("userData.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val userData = gson.fromJson(jsonObject, UserData::class.java)
        return userData
    }

    fun initEvent(myView : View){
        val userData = initData()
        val contentBox = myView.findViewById<LinearLayout>(R.id.contentBox)

        for(index in 0 until 10){
            val content = layoutInflater.inflate(R.layout.board_page_list_view, contentBox, false)
            contentBox.addView(content)

        }





    }
}