package com.example.myhealthpartner

import android.location.Location
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

    var latTemp : Double? = null
    var lngTemp : Double? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_page_matching_result_fragment, container, false)
        val scale = resources.displayMetrics.density


        val padding_5p = (10 * scale + 0.5f).toInt()
        view.setPadding(padding_5p,0,padding_5p,0)
        val exerciseChecked = arguments?.getString("exerciseChecked")
        val timeChecked = arguments?.getString("timeChecked")
        latTemp = arguments?.getDouble("Lat")
        lngTemp = arguments?.getDouble("Lng")
        Log.d("Latlng on  result : ","${latTemp}")

        Log.d(exerciseChecked, timeChecked!!)
        val userData = initData()
        initEvent(view, userData!!)
        Log.d("userData on  result : ","${userData.user[0].id}")

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

    fun checkCondition(myView: View,userData : UserData) : ArrayList<View>{
        val loginData = context?.getSharedPreferences("loginData", 0)

        val completeIndex = arrayListOf<View>()
        val exerciseChecked = arguments?.getString("exerciseChecked")
        val timeChecked = arguments?.getString("timeChecked")
        val fragmentBox = myView.findViewById<LinearLayout>(R.id.contentBox)

        for(index in 0 until userData.user.size){
            if(checkExercise(index, userData) == true && checkTime(index, userData) == true){
                val content = layoutInflater.inflate(R.layout.matching_data_customview, fragmentBox, false)
                content.findViewById<TextView>(R.id.text1).setText(userData.user[index].findUserDataList[0].nickname)
                content.findViewById<TextView>(R.id.text2).setText(userData.user[index].findUserDataList[0].ability)

                //  순서대로 정렬하기
                var locationOrigin = Location("myLocation")
//                locationOrigin.latitude = latTemp!!
//                locationOrigin.longitude = lngTemp!!
                locationOrigin.latitude = 37.44907588444528
                locationOrigin.longitude = 126.64336960762739

                var locationObject = Location("myLocation")
                locationObject.latitude = userData.user[index].findUserDataList[0].lat
                locationObject.longitude = userData.user[index].findUserDataList[0].lng

                Log.d("distance : ","${locationOrigin.distanceTo(locationObject)}")

                content.findViewById<TextView>(R.id.distanceText).setText(userData.user[index].findUserDataList[0].ability)
                fragmentBox.addView(content)
                completeIndex.add(content)
            }
        }
        return completeIndex
    }

    fun createReceiveView(myView: View, completeIndex : ArrayList<View>, userData: UserData){
        val fragmentBox = myView.findViewById<LinearLayout>(R.id.contentBox)


        for(index in 0 until completeIndex.size){
//            val content = layoutInflater.inflate(R.layout.matching_data_customview, fragmentBox, false)
//            content.findViewById<TextView>(R.id.text1).setText(userData.user[completeIndex[index]].findUserDataList[0].nickname)
            //fragmentBox.addView(content)
        }

    }

    fun initEvent(myView: View, userData: UserData){
        val completeIndex = checkCondition(myView,userData)
        Log.d("msg", completeIndex.toString())
        createReceiveView(myView, completeIndex, userData)
    }
}