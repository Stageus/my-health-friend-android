package com.example.myhealthpartner

import android.app.AlertDialog
import android.os.Bundle
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

    fun initEvent(view: View){
        var authentication:Boolean = false
        val certificationBtn = view.findViewById<Button>(R.id.certificationBtn)
        certificationBtn.setOnClickListener{
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

        val certificationConfirmBtn = view.findViewById<Button>(R.id.confirmButton)
        certificationConfirmBtn.setOnClickListener{
            authentication = true
        }

        val changePwBtn = view.findViewById<Button>(R.id.changePwBtn)
        changePwBtn.setOnClickListener{
            if(authentication == true){

            }
            else{
                alertDialog("본인인증이 되지 않았습니다!")
            }
        }
    }
}