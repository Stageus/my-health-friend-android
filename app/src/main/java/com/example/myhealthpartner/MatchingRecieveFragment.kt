package com.example.myhealthpartner

import android.app.AlertDialog
import android.content.Intent
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

class MatchingRecieveFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.matching_recieve_page_fragment, container, false)
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

        Log.d("msg", dateList.toString())

        for(index in 0 until dateList.size){
            for(index2 in 0 until dateList.size){
                Log.d(dateList[index].toString(), userData.user[userIndex].matchingReceiveList[index2].date.toString())
                if(dateList[index] == userData.user[userIndex].matchingReceiveList[index2].date){
                    val content = layoutInflater.inflate(R.layout.matching_recieve_page_view, contentBox, false)
                    content.findViewById<Button>(R.id.btn1).text = "프로필 이동"
                    content.findViewById<Button>(R.id.btn1).setOnClickListener {
                        val intent = Intent(context, OtherProfilePageActivity::class.java)
                        intent.putExtra("userIndex",index)
                        startActivity(intent)
                    }
                    content.findViewById<Button>(R.id.btn2).text = "메세지 보기"

                    Glide.with(this) //이미지 적용
                        .load(R.mipmap.temp)
                        .circleCrop()
                        .into(content.findViewById(R.id.symbol))
                    content.findViewById<TextView>(R.id.nickname).text = userData.user[userIndex].matchingReceiveList[index2].id
                    contentList.add(content)
                    contentBox.addView(content)
                    content.findViewById<Button>(R.id.btn2).setOnClickListener {
                        setMsg(index2, userIndex, userData, contentBox, content)
                    }
                    Log.d("msg", "addview")
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

                    content.findViewById<Button>(R.id.btn1).setOnClickListener {
                        val intent = Intent(context, OtherProfilePageActivity::class.java)
                        intent.putExtra("userIndex",index)
                        startActivity(intent)
                    }
                    content.findViewById<Button>(R.id.btn2).setOnClickListener {
                        setMsg(index2, userIndex, userData, contentBox, content)
                    }
                    content.findViewById<TextView>(R.id.nickname).text = userData.user[userIndex].matchingReceiveList[index2].id
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



    fun initEvent(myView: View){
        val sort = arguments?.getInt("sort")
        val userData = initData()
        if(sort == 0) sortNew(userData!!, myView)
        else if(sort == 1) sortOld(userData!!, myView)
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