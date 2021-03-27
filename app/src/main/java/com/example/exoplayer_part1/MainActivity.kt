package com.example.exoplayer_part1

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ExoPlayerFactory.newSimpleInstance
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class MainActivity : AppCompatActivity() {
    lateinit var playerView:PlayerView
    lateinit var player: SimpleExoPlayer
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerView=findViewById(R.id.playerView)
        progressBar=findViewById(R.id.progressBar)
        initPlayer()
       // loadVod("https://drive.google.com/drive/folders/0BzL6M5WpOxAdUW1ON2RwWS1YZk0")

        loadVod("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
    }

    fun initPlayer()
    {
        val adaptiveTrackSelection=AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())
        player=ExoPlayerFactory.newSimpleInstance(this,
        DefaultRenderersFactory(this),DefaultTrackSelector(adaptiveTrackSelection),DefaultLoadControl())
        playerView.player=player
        player.addListener(object :Player.EventListener{
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when(playbackState){

                    ExoPlayer.STATE_BUFFERING->progressBar.visibility=View.VISIBLE
                    ExoPlayer.STATE_READY->progressBar.visibility=View.GONE
                }
            }
        })
    }

    fun loadVod(url:String){
        val dataSourceFactory=DefaultDataSourceFactory(this, Util.getUserAgent(this,"Exo"),DefaultBandwidthMeter())

        val mediaSource=ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url))
        player.prepare(mediaSource)
        player.playWhenReady=true
    }
}