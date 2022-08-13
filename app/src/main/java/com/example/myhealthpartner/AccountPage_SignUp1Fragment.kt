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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson

class AccountPage_SignUp1Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_page_sign_up1_fragment, container, false)
        val userData = initData()
        initEvent(view, userData)
        return view
    }

    fun initData(): UserData { //json파일 읽어오기 작업
        val jsonObject : String
        jsonObject = requireActivity().assets.open("userData.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val userData = gson.fromJson(jsonObject, UserData::class.java)
        return userData
    }

    fun confirmPw(myView: View):Boolean{
        val pw1 = myView.findViewById<EditText>(R.id.pwEditText).text.toString()
        val pw2 = myView.findViewById<EditText>(R.id.pwCheckEditText).text.toString()
        if(pw1 == pw2) {
            if(pw1.length >= 6) {
                return true
            }
            else {
                alertDialog("패스워드는 6자 이상입니다.")
                return false
            }
        }
        else{
            alertDialog("비밀번호가 일치하지 않습니다.")
            return false
        }
    }

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


    fun initEvent(myView : View, userData : UserData){
        var dupCheck = false
        val dupCheckBtn = myView.findViewById<Button>(R.id.dupCheckBtn)
        val changeFragment = context as ChangeFragment
        val nextBtn = myView.findViewById<Button>(R.id.nextBtn)
        val idEditText = myView.findViewById<EditText>(R.id.idEditText)

        dupCheckBtn.setOnClickListener {
            if(idEditText.text.toString().length >= 4) {//크기체크
                //중복체크
                var count = 0
                for (index in 0 until userData.user.size) {
                    if(idEditText.text.toString() == userData.user[index].id){
                        count += 1
                    }
                }
                Log.d("test", count.toString())
                if(count == 0){
                    dupCheck = true
                }
                else dupCheck = false

                if(dupCheck == true){
                    alertDialog("사용가능한 아이디입니다.")
                    idEditText.isEnabled =false
                    idEditText.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.bright_silver))
                }
                else{
                    alertDialog("이미 사용중인 아이디입니다.")
                }
            }
            else{
                alertDialog("아이디는 4자 이상입니다")
            }

        }

        nextBtn.setOnClickListener {
            if(dupCheck == false)
            {
                alertDialog("아이디 중복확인을 해주세요")
            }
            else{
                if(confirmPw(myView) == true) {
                    changeFragment.change(2)
                }
            }
        }
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
}