package com.example.myhealthpartner

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class MatchingRecieveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.matching_recieve_page_layout)
        val matchingReceiveFragment = MatchingRecieveFragment()
        val bundle = Bundle()
        bundle.putInt("sort", 0)
        matchingReceiveFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, matchingReceiveFragment).commit()


        initEvent()
    }


    fun initEvent(){
        val sortNewTextView = findViewById<TextView>(R.id.sortNewTextview)
        val sortOldTextView = findViewById<TextView>(R.id.sortOldTextview)

        sortNewTextView.setOnClickListener{
            val bundle = Bundle()
            val matchingReceiveFragment = MatchingRecieveFragment()
            sortNewTextView.typeface = resources.getFont(R.font.nanum_square_extrabold)
            sortOldTextView.typeface = resources.getFont(R.font.nanum_square_regular)
            bundle.putInt("sort", 0)
            matchingReceiveFragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, matchingReceiveFragment).commit()
        }

        sortOldTextView.setOnClickListener{
            val bundle = Bundle()
            val matchingReceiveFragment = MatchingRecieveFragment()
            sortNewTextView.typeface = resources.getFont(R.font.nanum_square_regular)
            sortOldTextView.typeface = resources.getFont(R.font.nanum_square_extrabold)
            bundle.putInt("sort", 1)
            matchingReceiveFragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.fragmentBox, matchingReceiveFragment).commit()
        }

    }
}