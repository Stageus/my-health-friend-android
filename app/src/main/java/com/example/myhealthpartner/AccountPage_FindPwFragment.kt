package com.example.myhealthpartner

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.gson.Gson

class AccountPage_FindPwFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_page_find_pw_fragment, container, false)
        val certificationLayout = view.findViewById<LinearLayout>(R.id.certificationLayout)
        certificationLayout.visibility = View.GONE
        initEvent(view)
        return view
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

    fun certification(view: View) : Boolean{
        var returnValue =false
        val idEdit = view.findViewById<EditText>(R.id.idEditText).text
        val nameEdit = view.findViewById<EditText>(R.id.nameEditText).text
        val phNumberEdit = view.findViewById<EditText>(R.id.phoneNumEditText).text


        //백엔드 들어갈 부분(로딩 넣기)
        val jsonObject : String
        jsonObject = requireActivity().assets.open("userData.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        val userData = gson.fromJson(jsonObject, UserData::class.java)

        for(index in 0 until userData.user.size){
            Log.d("msg", userData.user[1].findUserDataList[0].nickname)
            if(userData.user[index].id == idEdit.toString()){
                if(userData.user[index].userDataList[0].name ==nameEdit.toString()){
                    if(userData.user[index].userDataList[0].tel == phNumberEdit.toString())
                    {
                        //3개가 모두 일치하면
                        returnValue = true
                    }
                }
            }
            //최대한 낭비 없애기
        }
        return returnValue
    }

    fun activateInvisibleLayout(view: View){
        val idEdit = view.findViewById<EditText>(R.id.idEditText)
        val nameEdit = view.findViewById<EditText>(R.id.nameEditText)
        val phNumberEdit = view.findViewById<EditText>(R.id.phoneNumEditText)
        idEdit.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.bright_silver))
        nameEdit.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.bright_silver))
        phNumberEdit.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.bright_silver))

        idEdit.isEnabled =false
        nameEdit.isEnabled =false
        phNumberEdit.isEnabled =false

        val certificationLayout = view.findViewById<LinearLayout>(R.id.certificationLayout)
        certificationLayout.visibility = View.VISIBLE
    }

    fun initEvent(view: View){
        var authentication = false //다음 페이지 가기 위한 변수
        val certificationBtn = view.findViewById<Button>(R.id.certificationBtn)
        val confirmBtn = view.findViewById<Button>(R.id.confirmBtn)
        val changePwBtn = view.findViewById<Button>(R.id.changePwBtn)

        val cuf = CommonUsedFunctionClass()
        cuf.changeBtnColor(view,certificationBtn,R.drawable.rounded_gray,R.drawable.rounded_silver)
        cuf.changeBtnColor(view,confirmBtn,R.drawable.rounded_gray,R.drawable.rounded_silver)
        cuf.changeBtnColor(view,changePwBtn,R.drawable.rounded_gray,R.drawable.rounded_silver)


        certificationBtn.setOnClickListener{
            if(certification(view) == true){
                activateInvisibleLayout(view)
            }
            else {
                alertDialog("개인정보가 \n 일치하지 않습니다")
            }
        }
        confirmBtn.setOnClickListener{
            authentication = true
        }

        changePwBtn.setOnClickListener{
            if(authentication == true){



                //전화번호인증 api 들어갈 곳
                val changeFragment = context as ChangeFragment
                changeFragment.change(7)
                //이부분입니다.
                alertDialog("본인인증 완료")



            }
            else{
                alertDialog("본인인증이 되지 \n않았습니다!")
            }
        }
    }
}