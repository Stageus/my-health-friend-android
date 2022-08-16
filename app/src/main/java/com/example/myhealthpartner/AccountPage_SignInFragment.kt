package com.example.myhealthpartner

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import org.w3c.dom.Text
import java.net.Inet4Address
import kotlin.math.log

class AccountPage_SignInFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_page_sign_in_fragment, container, false)
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

    //경고다이얼로그 함수
    fun alertDialog(alertMessageTemp :String){
        val dialogTemp = AlertDialog.Builder(context)
        val dialog = dialogTemp.create()
        val dialogViewTemp = layoutInflater.inflate(R.layout.common_alert_dialog,null)
        val alertMessage = dialogViewTemp.findViewById<TextView>(R.id.alertMessage)
        alertMessage.text = alertMessageTemp
        dialog.setView(dialogViewTemp)
        dialog.show()
        dialogViewTemp.findViewById<Button>(R.id.confirmButton).setOnClickListener {
            dialog.dismiss()
        }
    }

    fun initEvent(myView: View, userData : UserData){
        val loginData = context?.getSharedPreferences("loginData", 0)
        var loginsuccess = false
        val changeFragment = context as ChangeFragment
        val signinBtn = myView.findViewById<Button>(R.id.signInBtn)
        val signupBtn = myView.findViewById<Button>(R.id.signUpBtn)
        val findPwText = myView.findViewById<TextView>(R.id.findPwText)

        signinBtn.setOnClickListener {
            val enterId = myView.findViewById<EditText>(R.id.idEditText).text
            val enterPw = myView.findViewById<EditText>(R.id.pwEditText).text

            for(index in 0 until userData.user.size){
                Log.d("msg", userData.user[1].findUserDataList[0].nickname)
                if(userData.user[index].id == enterId.toString()){
                    if(userData.user[index].pw == enterPw.toString()){
                        if(userData.user[index].findUserDataList[0].nickname==""){
                            changeFragment.change(4)
                        }
                        else{ //user의 프로필이 생성되어있는 경우
                            setLoginData(loginData, userData, index)
                            changeFragment.change(5)
                        }
                        enterId.clear()
                        enterPw.clear()
                        loginsuccess = true
                    }
                }
            }
            //로그인 실패시
            if(loginsuccess == false){
                alertDialog("아이디 또는 비밀번호를 \n확인해주세요")
            }

        }
        signupBtn.setOnClickListener {
            changeFragment.change(1)
        }
        findPwText.setOnClickListener{
            changeFragment.change(6)
        }
    }

    //sharedpreference 설정
    fun setLoginData(loginData : SharedPreferences?, userData : UserData, index : Int){
        loginData?.edit()?.putString("id", userData.user[index].id)?.apply()
        loginData?.edit()?.putString("pw", userData.user[index].pw)?.apply()
        loginData?.edit()?.putString("nickname", userData.user[index].findUserDataList[0].nickname)?.apply()
        loginData?.edit()?.putString("career", userData.user[index].findUserDataList[0].career)?.apply()
        loginData?.edit()?.putString("ability", userData.user[index].findUserDataList[0].ability)?.apply()
        loginData?.edit()?.putString("address", userData.user[index].findUserDataList[0].address)?.apply()
        loginData?.edit()?.putString("exerciseType", userData.user[index].findUserDataList[0].exerciseType)?.apply()
        loginData?.edit()?.putInt("rpm", userData.user[index].findUserDataList[0].rpm)?.apply()
        loginData?.edit()?.putInt("age", userData.user[index].findUserDataList[0].age)
        loginData?.edit()?.putString("exerciseTime", userData.user[index].findUserDataList[0].exerciseTime)?.apply()
    }
    //gson을 위한 data class

}