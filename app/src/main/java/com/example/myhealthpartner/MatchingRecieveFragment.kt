package com.example.myhealthpartner

import android.app.AlertDialog
import android.content.Intent
import android.location.Location
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
import kotlin.math.roundToInt

class MatchingRecieveFragment : Fragment() {
    var latTemp : Double? = null
    var lngTemp : Double? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.matching_recieve_page_fragment, container, false)
        val loginData = context?.getSharedPreferences("loginData", 0)
        latTemp = loginData!!.getString("Lat","")!!.toDouble()
        lngTemp = loginData!!.getString("Lng","")!!.toDouble()
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

    fun sortNew(userData: UserData, myView: View){
        val contentBox = myView.findViewById<LinearLayout>(R.id.contentBox)
        val contentList = ArrayList<View>()
        val loginData = context?.getSharedPreferences("loginData", 0)
        val userIndex = loginData?.getInt("userIndex", 0)
        val dateList = ArrayList<Long>()
        for(index in 0 until userData.user[userIndex!!].matchingReceiveList.size){
            dateList.add(userData.user[userIndex].matchingReceiveList[index].date)
        }
        dateList.sort()

        for(index in 0 until dateList.size){
            for(index2 in 0 until dateList.size){
                Log.d(dateList[index].toString(), userData.user[userIndex].matchingReceiveList[index2].date.toString())
                if(dateList[index] == userData.user[userIndex].matchingReceiveList[index2].date){
                    val content = layoutInflater.inflate(R.layout.matching_recieve_page_view, contentBox, false)
                    content.findViewById<Button>(R.id.btn1).text = "프로필 이동"
                    content.findViewById<Button>(R.id.btn2).text = "메세지 보기"
                    content.findViewById<Button>(R.id.btn2).setOnClickListener {
                        setMsg(index2, userIndex, userData, contentBox, content)
                    }

                    var friendIndex = -1
                    //아이디로 서칭한 닉네임을 줘야하죠
                    Log.d("??? is ","${userData.user[userIndex].matchingReceiveList[index2].id}")
                    for(index3 in 0  until userData.user.size) {
                        if(userData.user[index3].id == userData.user[userIndex].matchingReceiveList[index2].id) {
                            friendIndex = index3
                            break
                        }
                    }
                    content.findViewById<TextView>(R.id.nickname).text = userData.user[friendIndex].findUserDataList[0].nickname
                    content.findViewById<TextView>(R.id.text2).text = userData.user[friendIndex].findUserDataList[0].ability


                    content.findViewById<Button>(R.id.btn1).setOnClickListener {
                        val intent = Intent(context, OtherProfilePageActivity::class.java)
                        intent.putExtra("userIndex",friendIndex)
                        startActivity(intent)
                    }

                    val newTime = userData.user[userIndex].matchingReceiveList[index2].date.toString()
                    val year = newTime.slice(IntRange(0,3))
                    val month = newTime.slice(IntRange(4,5))
                    val day = newTime.slice(IntRange(6,7))
                    val hour = newTime.slice(IntRange(8,9))
                    val minute = newTime.slice(IntRange(10,11))
                    content.findViewById<TextView>(R.id.informationText).text = month +"월 " + day +"일  " + hour +"시 "+minute + "분"

                    contentList.add(content)
                    Glide.with(this) //이미지 적용
                        .load(R.mipmap.temp)
                        .circleCrop()
                        .into(content.findViewById(R.id.symbol))

                    contentBox.addView(content)
                    content.findViewById<Button>(R.id.btn2).setOnClickListener {
                        setMsg(index2, userIndex, userData,contentBox, content)
                    }
                }
            }
        }
    }

    fun sortOld(userData: UserData, myView: View){
        val contentBox = myView.findViewById<LinearLayout>(R.id.contentBox)
        val contentList = ArrayList<View>()
        val loginData = context?.getSharedPreferences("loginData", 0)
        val userIndex = loginData?.getInt("userIndex", 0)
        val dateList = ArrayList<Long>()
        for(index in 0 until userData.user[userIndex!!].matchingReceiveList.size){
            dateList.add(userData.user[userIndex].matchingReceiveList[index].date)
        }
        dateList.reverse()

        for(index in 0 until dateList.size){
            for(index2 in 0 until dateList.size){
                Log.d(dateList[index].toString(), userData.user[userIndex].matchingReceiveList[index2].date.toString())
                if(dateList[index] == userData.user[userIndex].matchingReceiveList[index2].date){
                    val content = layoutInflater.inflate(R.layout.matching_recieve_page_view, contentBox, false)
                    content.findViewById<Button>(R.id.btn1).text = "프로필 이동"
                    content.findViewById<Button>(R.id.btn2).text = "메세지 보기"
                    content.findViewById<Button>(R.id.btn2).setOnClickListener {
                        setMsg(index2, userIndex, userData, contentBox, content)
                    }

                    var friendIndex = -1
                    //아이디로 서칭한 닉네임을 줘야하죠
                    Log.d("??? is ","${userData.user[userIndex].matchingReceiveList[index2].id}")
                    for(index3 in 0  until userData.user.size) {
                        if(userData.user[index3].id == userData.user[userIndex].matchingReceiveList[index2].id) {
                            friendIndex = index3
                            break
                        }
                    }
                    content.findViewById<TextView>(R.id.nickname).text = userData.user[friendIndex].findUserDataList[0].nickname
                    content.findViewById<TextView>(R.id.text2).text = userData.user[friendIndex].findUserDataList[0].ability


                    content.findViewById<Button>(R.id.btn1).setOnClickListener {
                        val intent = Intent(context, OtherProfilePageActivity::class.java)
                        intent.putExtra("userIndex",friendIndex)
                        startActivity(intent)
                    }

                    val newTime = userData.user[userIndex].matchingReceiveList[index2].date.toString()
                    val year = newTime.slice(IntRange(0,3))
                    val month = newTime.slice(IntRange(4,5))
                    val day = newTime.slice(IntRange(6,7))
                    val hour = newTime.slice(IntRange(8,9))
                    val minute = newTime.slice(IntRange(10,11))
                    content.findViewById<TextView>(R.id.informationText).text = month +"월 " + day +"일  " + hour +"시 "+minute + "분"

                    contentList.add(content)
                    Glide.with(this) //이미지 적용
                        .load(R.mipmap.temp)
                        .circleCrop()
                        .into(content.findViewById(R.id.symbol))

                    contentBox.addView(content)
                    content.findViewById<Button>(R.id.btn2).setOnClickListener {
                        setMsg(index2, userIndex, userData,contentBox, content)
                    }
                }
            }
        }
    }



    fun sortNear(userData : UserData,myView: View) : ArrayList<View>{
        val loginData = context?.getSharedPreferences("loginData", 0)
        val userIndex = loginData?.getInt("userIndex", 0)
        val viewArrayList = arrayListOf<View>()

        val fragmentBox = myView.findViewById<LinearLayout>(R.id.contentBox)


        for(index in 0 until userData.user[userIndex!!].matchingReceiveList.size){
            val content = layoutInflater.inflate(R.layout.matching_recieve_page_view, fragmentBox, false)

            Glide.with(this) //이미지 적용
                .load(R.mipmap.temp)
                .circleCrop()
                .into(content.findViewById(R.id.symbol))

            var friendIndex = -1
            //  거리 적어넣기
            for(index2 in 0 until userData.user.size) {
                if(userData.user[index2].id == userData.user[userIndex!!].matchingReceiveList[index].id) {
                    friendIndex = index2
                    break
                }

            }
            val locationOrigin = Location("myLocation")
            locationOrigin.latitude = latTemp!!
            locationOrigin.longitude = lngTemp!!

            val locationObject = Location("myLocation")
            locationObject.latitude = userData.user[friendIndex].findUserDataList[0].lat
            locationObject.longitude = userData.user[friendIndex].findUserDataList[0].lng

            val distance = locationOrigin.distanceTo(locationObject).toInt().toString()
            content.findViewById<TextView>(R.id.informationText).setText(distance)
            content.findViewById<TextView>(R.id.nickname).setText(userData.user[friendIndex].findUserDataList[0].nickname)
            content.findViewById<TextView>(R.id.text2).setText(userData.user[friendIndex].findUserDataList[0].ability)

            //생성과 동시에 이벤트 등록
            val goProfileBtn = content.findViewById<Button>(R.id.btn1)
            val showMessage = content.findViewById<Button>(R.id.btn2)

            goProfileBtn.setOnClickListener{
                val intent = Intent(context, OtherProfilePageActivity::class.java)
                intent.putExtra("userIndex",friendIndex)
                startActivity(intent)
            }
            showMessage.setOnClickListener {
                setMsg(index, userIndex, userData, fragmentBox, content)
            }

            viewArrayList.add(content)
        }
        return viewArrayList
    }



    fun createReceiveView(myView: View, viewArrayList : ArrayList<View>){
        //ui데이터 기반으로 데이터 정렬함 -  실제로는 DB나 json데이터를 정렬해야함, 근데, 거리를 db에서 가져오는게 아니라 여기서 해야하는데 어떻게 할 지 잘 모르겠음
        //위에서 거리 측정한걸 따로 데이터를 정렬해서 그걸 적용해야할 듯
        val contentBox = myView.findViewById<LinearLayout>(R.id.contentBox)
        val newIndexList = arrayListOf<Int>()
        val content = layoutInflater.inflate(R.layout.matching_recieve_page_view, contentBox, false)
        for (index in 0 until viewArrayList.size){
            if(newIndexList.size == 0){
                newIndexList.add(index)
            }
            else{
                val newViewDistance = viewArrayList[index].findViewById<TextView>(R.id.informationText).text.toString().toDouble()
                var indexTemp = -1
                for (index2 in 0 until newIndexList.size){
                    val tempViewDistance = viewArrayList[newIndexList[index2]].findViewById<TextView>(R.id.informationText).text.toString().toDouble()
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
            viewTemp.findViewById<TextView>(R.id.informationText).text = distanceToString(viewTemp.findViewById<TextView>(R.id.informationText).text.toString().toDouble())
            contentBox.addView(viewTemp)
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



    fun initEvent(myView: View){
        val loginData = context?.getSharedPreferences("loginData", 0)
        val userIndex = loginData?.getInt("userIndex", 0)

        val sort = arguments?.getInt("sort")
        val userData = initData()
        Log.d("help me : ","${userData!!.user[userIndex!!].matchingReceiveList}")
        if(userData!!.user[userIndex!!].matchingReceiveList != null){
            if(sort == 0) sortNew(userData!!, myView)
            else if(sort == 1) sortOld(userData!!, myView)
            else if(sort == 2) {
                createReceiveView(myView,sortNear(userData!!, myView))
            }
        }
    }

    fun setMsg(index : Int, userIndex : Int, userData: UserData, contentBox : LinearLayout, content : View)
    {
        val dialogTemp = AlertDialog.Builder(context)
        val dialog = dialogTemp.create()
        val dialogViewTemp = layoutInflater.inflate(R.layout.matching_message_dialog1,null)
        val alertMessage = dialogViewTemp.findViewById<TextView>(R.id.alertMessage)
        alertMessage.text = userData.user[userIndex].matchingReceiveList[index].message.messageDetailed
        val newTime = userData.user[userIndex].matchingReceiveList[index].message.promisedate.toString()
        val year = newTime.slice(IntRange(0,3))
        val month = newTime.slice(IntRange(4,5))
        val day = newTime.slice(IntRange(6,7))
        val hour = newTime.slice(IntRange(8,9))
        val minute = newTime.slice(IntRange(10,11))


        dialogViewTemp.findViewById<TextView>(R.id.timeTextView).text = month +"월 " + day +"일  " + hour +"시 "+minute + "분"
        dialogViewTemp.findViewById<TextView>(R.id.placeTextView).text = userData.user[userIndex].matchingReceiveList[index].message.location
        dialog.setView(dialogViewTemp)
        dialog.show()
        dialogViewTemp.findViewById<Button>(R.id.acceptBtn).setOnClickListener{
            dialog.dismiss()
        }
        dialogViewTemp.findViewById<Button>(R.id.declineBtn).setOnClickListener {
            contentBox.removeView(content)

            dialog.dismiss()
        }
        dialogViewTemp.findViewById<Button>(R.id.replyBtn).setOnClickListener {
            dialog.dismiss()
            val dialogTemp2 = AlertDialog.Builder(context)
            val dialog2 = dialogTemp2.create()
            val dialogViewTemp2 = layoutInflater.inflate(R.layout.matching_message_dialog2,null)
            dialog2.setView(dialogViewTemp2)
            dialog2.show()
            dialogViewTemp2.findViewById<TextView>(R.id.timeEditText).text = month +"월 " + day +"일  " + hour +"시 "+minute + "분"
            dialogViewTemp2.findViewById<TextView>(R.id.placeEditText).text = userData.user[userIndex].matchingReceiveList[index].message.location
            dialogViewTemp2.findViewById<Button>(R.id.sendMessageBtn).setOnClickListener{
                dialog2.dismiss()
            }

        }
    }

}