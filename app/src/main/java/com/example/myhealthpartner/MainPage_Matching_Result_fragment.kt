package com.example.myhealthpartner

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson

class MainPage_Matching_Result_fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_page_matching_result_fragment, container, false)
        val scale = resources.displayMetrics.density
        val padding_5p = (5 * scale + 0.5f).toInt()
        view.setPadding(padding_5p,0,padding_5p,0)
        val exerciseChecked = arguments?.getString("exerciseChecked")
        val timeChecked = arguments?.getString("timeChecked")
        Log.d(exerciseChecked, timeChecked!!)
        val userData = initData()
        initEvent(view, userData!!)
        return view
    }

    fun initData(): UserData? { //json파일 읽어오기 작업
        val jsonObject : String
        jsonObject = requireActivity().assets.open("userData.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val userData = gson.fromJson(jsonObject, UserData::class.java)
        return userData
    }

    fun checkExercise(index : Int, userData: UserData) : Boolean{
        val exerciseChecked = arguments?.getString("exerciseChecked")
        var check = false
        for(i in 0 until 4){
            if(exerciseChecked!![i] == userData.user[index].findUserDataList[0].exerciseType[i] && exerciseChecked!![i] == 'T'){
                check = true
            }
        }
        return check
    }

    fun checkTime(index : Int, userData: UserData) : Boolean{
        val timeChecked = arguments?.getString("timeChecked")
        var check = false
        for(i in 0 until 4){
            if(timeChecked!![i] == userData.user[index].findUserDataList[0].exerciseTime[i] && timeChecked!![i] == 'T'){
                check = true
            }
        }
        return check
    }

    fun checkCondition(userData : UserData) : ArrayList<Int>{
        val loginData = context?.getSharedPreferences("loginData", 0)

        val completeIndex = arrayListOf<Int>()
        val exerciseChecked = arguments?.getString("exerciseChecked")
        val timeChecked = arguments?.getString("timeChecked")

        for(index in 0 until userData.user.size){
            if(loginData!!.getString("address", "") == userData.user[index].findUserDataList[0].address &&
                    checkExercise(index, userData) == true && checkTime(index, userData) == true){
                completeIndex.add(index)
            }
        }
        return completeIndex
    }

    fun createReceiveView(myView: View, completeIndex : ArrayList<Int>, userData: UserData){
        val fragmentBox = myView.findViewById<LinearLayout>(R.id.contentBox)

        for(index in 0 until completeIndex.size){
            val content = layoutInflater.inflate(R.layout.matching_data_customview, fragmentBox, false)
            content.findViewById<TextView>(R.id.text1).setText(userData.user[completeIndex[index]].findUserDataList[0].nickname)
            Log.d("msg", "t")
            fragmentBox.addView(content)
        }

    }

    fun initEvent(myView: View, userData: UserData){
        val completeIndex = checkCondition(userData)
        Log.d("msg", completeIndex.toString())
        createReceiveView(myView, completeIndex, userData)
    }
}