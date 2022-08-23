package com.example.myhealthpartner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView




class MyProfilePageVideoFragment : Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.myprofile_page_video_fragment, container, false)
        initEvent("nc-t4oHfw",view)
        return view
    }
    private fun initEvent(key: String,view: View) {
        //추후 데이터 변경
        val youTubePlayerView: YouTubePlayerView = view.findViewById(R.id.youtube_player_view)
        lifecycle.addObserver(youTubePlayerView)
    }
}