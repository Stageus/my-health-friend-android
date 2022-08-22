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
import kotlin.math.roundToInt

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

        val viewArrayList = arrayListOf<View>()
        val exerciseChecked = arguments?.getString("exerciseChecked")
        val timeChecked = arguments?.getString("timeChecked")
        val fragmentBox = myView.findViewById<LinearLayout>(R.id.contentBox)

        for(index in 0 until userData.user.size){
            if(checkExercise(index, userData) == true && checkTime(index, userData) == true){
                val content = layoutInflater.inflate(R.layout.matching_data_customview, fragmentBox, false)
                content.findViewById<TextView>(R.id.text1).setText(userData.user[index].findUserDataList[0].nickname)
                content.findViewById<TextView>(R.id.text2).setText(userData.user[index].findUserDataList[0].ability)

                //  순서대로 정렬하기
                val locationOrigin = Location("myLocation")
                locationOrigin.latitude = latTemp!!
                locationOrigin.longitude = lngTemp!!


                val locationObject = Location("myLocation")
                locationObject.latitude = userData.user[index].findUserDataList[0].lat
                locationObject.longitude = userData.user[index].findUserDataList[0].lng

                val distance = locationOrigin.distanceTo(locationObject).toInt().toString()
                content.findViewById<TextView>(R.id.distanceText).setText(distance)
                viewArrayList.add(content)
            }
        }
        return viewArrayList
    }

    fun createReceiveView(myView: View, viewArrayList : ArrayList<View>, userData: UserData){
        //ui데이터 기반으로 데이터 정렬함 -  실제로는 DB나 json데이터를 정렬해야함, 근데, 거리를 db에서 가져오는게 아니라 여기서 해야하는데 어떻게 할 지 잘 모르겠음
        //위에서 거리 측정한걸 따로 데이터를 정렬해서 그걸 적용해야할 듯
        val fragmentBox = myView.findViewById<LinearLayout>(R.id.contentBox)
        val newIndexList = arrayListOf<Int>()
        for (index in 0 until viewArrayList.size){
            if(newIndexList.size == 0){
                newIndexList.add(index)
            }
            else{
                val newViewDistance = viewArrayList[index].findViewById<TextView>(R.id.distanceText).text.toString().toDouble()
                var indexTemp = -1
                for (index2 in 0 until newIndexList.size){
                    val tempViewDistance = viewArrayList[newIndexList[index2]].findViewById<TextView>(R.id.distanceText).text.toString().toDouble()
                    if (newViewDistance < tempViewDistance){
                        indexTemp = index2
                        break
                    }
                    indexTemp = newIndexList.size
                }
                newIndexList.add(indexTemp,index)
            }
        }

        for(index in 0 until newIndexList!!.size){
            val viewTemp = viewArrayList[newIndexList[index]]
            viewTemp.findViewById<TextView>(R.id.distanceText).text = distanceToString(viewTemp.findViewById<TextView>(R.id.distanceText).text.toString().toDouble())
            fragmentBox.addView(viewTemp)
        }

    }

    fun distanceToString(distance : Double):String{
        var distance = distance
        var distanceString = ""
        if (distance < 50){
            distanceString = "주변에 있음"
        }
        else if(distance < 100){
            val cuttedDistance = distance.toInt()
            distanceString = distance.toString() + "m"
        }
        else if(distance < 1000){
            val cuttedDistance = distance.toInt()
            distanceString = distance.toString() + "m"
        }
        else if(distance < 10000){
            distance = (distance * 0.001)
            val cuttedDistance = (distance*100.0).roundToInt() / 100.0
            distanceString = cuttedDistance.toString() + "km"
        }
        else{
            distance = (distance * 0.001)
            val cuttedDistance = (distance*100.0).roundToInt() / 100.0
            distanceString = cuttedDistance.toString() + "km"
        }

        return distanceString
    }

    fun initEvent(myView: View, userData: UserData){
        val viewArrayList = checkCondition(myView,userData)
        Log.d("msg", viewArrayList.toString())
        createReceiveView(myView, viewArrayList, userData)
    }
}