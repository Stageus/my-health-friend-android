package com.example.myhealthpartner

import android.app.AlertDialog
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
    data class UserData(
        val user : ArrayList<User>
    )

    data class User(
        val id : String,
        val pw : String,
        val userDataList : ArrayList<UserDataList>,
        val findUserDataList : ArrayList<FindUserDataList>

    )
    data class UserDataList(
        val name : String,
        val tel : String,
        val email : String
    )

    data class FindUserDataList(
        val nickname : String,
        val address : String,
        val exerciseType : String,
        val age : Int,
        val exerciseTime : String
    )

    fun initEvent(myView: View, userData : UserData){
        var loginsuccess = false
        Log.d("size", userData.user.size.toString())
        val changeFragment = context as ChangeFragment
        val singinBtn = myView.findViewById<Button>(R.id.signInBtn)
        val signupBtn = myView.findViewById<Button>(R.id.signUpBtn)
        val findPwText = myView.findViewById<TextView>(R.id.findPwText)

        singinBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(context)
            val dialog2 = dialog.create()

            val enterId = myView.findViewById<EditText>(R.id.idEditText).text
            val enterPw = myView.findViewById<EditText>(R.id.pwEditText).text
            for(index in 0 until userData.user.size){
                Log.d("msg", userData.user[1].findUserDataList[0].nickname)

                if(userData.user[index].id == enterId.toString()){
                    if(userData.user[index].pw == enterPw.toString()){
                        if(userData.user[index].findUserDataList[0].nickname==""){
                            changeFragment.change(4)
                        }
                        else{
                            changeFragment.change(5)
                        }

                        enterId.clear()
                        enterPw.clear()
                        loginsuccess = true
                    }
                    else{
                        val dialogTemp2 = AlertDialog.Builder(context)
                        val dialog2 = dialogTemp2.create()
                        val dialogViewTemp = layoutInflater.inflate(R.layout.common_alert_dialog,null)
                        val alertMessage = dialogViewTemp.findViewById<TextView>(R.id.alertMessage)
                        alertMessage.text = "아이디 또는 비밀번호를 확인해주세요"
                        dialog2.setView(dialogViewTemp)
                        dialog2.show()
                        dialogViewTemp.findViewById<Button>(R.id.confirmButton).setOnClickListener{
                            dialog2.dismiss()
                        }

                    }
                }
            }
            if(loginsuccess == false){
                val dialogTemp2 = AlertDialog.Builder(context)
                val dialog2 = dialogTemp2.create()
                val dialogViewTemp = layoutInflater.inflate(R.layout.common_alert_dialog,null)
                val alertMessage = dialogViewTemp.findViewById<TextView>(R.id.alertMessage)
                alertMessage.text = "아이디 또는 비밀번호를 확인해주세요"
                dialog2.setView(dialogViewTemp)
                dialog2.show()
                dialogViewTemp.findViewById<Button>(R.id.confirmButton).setOnClickListener{
                    dialog2.dismiss()
                }

            }

        }
        signupBtn.setOnClickListener {
            changeFragment.change(1)
        }
        findPwText.setOnClickListener{
            changeFragment.change(4)
        }
    }
}