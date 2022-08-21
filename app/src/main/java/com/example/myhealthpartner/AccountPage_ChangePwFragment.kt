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

class AccountPage_ChangePwFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_page_change_pw_fragment, container, false)
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

    fun initEvent(view: View){
        val changePwBtn = view.findViewById<Button>(R.id.changePwBtn)
        val pwEditText = view.findViewById<EditText>(R.id.pwEditText)
        val pwCheckEditText = view.findViewById<EditText>(R.id.pwCheckEditText)

        val cuf = CommonUsedFunctionClass()
        cuf.changeBtnColor(view,changePwBtn,R.drawable.rounded_signature_purple2,R.drawable.rounded_signature_purple)

        changePwBtn.setOnClickListener {
            if(pwEditText.text.length < 6){
                alertDialog("비밀번호가 너무 짧습니다.")
            }
            else if(pwEditText.text.toString() != pwCheckEditText.text.toString()){

                alertDialog("비밀번호가 \n일치하지 않습니다.")
            }
            else{
                alertDialog("비밀번호 변경 완료!")
                val changeFragment = context as ChangeFragment
                changeFragment.change(0)
            }
        }
    }
}