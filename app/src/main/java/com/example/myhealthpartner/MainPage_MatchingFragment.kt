package com.example.myhealthpartner

import android.app.AlertDialog
import android.media.Image
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

class MainPage_MatchingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_page_matching_fragment, container, false)
        initEvent(view)
        return view
    }


    fun initEvent(myView: View) {
        val fitnessListBtn = myView.findViewById<ImageButton>(R.id.fitnessListBtn)
        fitnessListBtn.setOnClickListener {
            choiceDialog()

        }
    }

    fun choiceDialog() {
        val builder = AlertDialog.Builder(ContextThemeWrapper(context, R.style.AlertDialogCustom))
        val selectionItem: ArrayList<String> = arrayListOf()
        builder.setTitle("Test")
        builder.setMultiChoiceItems(R.array.fitnessList, null) { p0, which, isChecked ->

            val fitness: Array<String> = resources.getStringArray(R.array.fitnessList)

            if (isChecked) {
                selectionItem.add(fitness[which])
            } else {
                selectionItem.remove(fitness[which])
            }
        }

        builder.setPositiveButton("ok") { p0, p1 ->
            var selections = ArrayList<String>()
            for (item: String in selectionItem) {
                selections.add(item)
            }
        }

        builder.setNegativeButton("cancel") { dialog, p1 ->
            dialog.cancel()
        }
        builder.show()
    }
}


