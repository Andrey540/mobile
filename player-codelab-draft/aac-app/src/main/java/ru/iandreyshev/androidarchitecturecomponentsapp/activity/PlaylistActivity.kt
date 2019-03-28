package ru.iandreyshev.androidarchitecturecomponentsapp.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_player.view.*
import kotlinx.android.synthetic.main.activity_playlist.*
import ru.iandreyshev.androidarchitecturecomponentsapp.R
import ru.iandreyshev.androidarchitecturecomponentsapp.application.MusicApplication
import ru.iandreyshev.androidarchitecturecomponentsapp.injection.ViewModelInjector
import ru.iandreyshev.androidarchitecturecomponentsapp.view.PlaylistViewModel
import ru.iandreyshev.model.player.PlayingState
import ru.iandreyshev.model.playlist.ITrack
import ru.iandreyshev.utils.disable
import ru.iandreyshev.utils.enable

class PlaylistActivity : AppCompatActivity() {
    private val mPlayerPresenter = MusicApplication.getPlayerPresenter()
    private lateinit var mViewModel: PlaylistViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        initIntroView()
        initViewModel()
    }

    private fun initViewModel() {
        mViewModel = ViewModelInjector.getPlaylistViewModel(this)

        mViewModel.trackTitle.observe(this, Observer { newTitle: String? ->
            updateTitleView(newTitle.orEmpty())
        })
        mViewModel.playingState.observe(this, Observer { newState: PlayingState? ->
            if (newState !== null) {
                updatePlayingButtons(newState)
            }
        })
        mViewModel.playList.observe(this, Observer { playList: List<ITrack>? ->
            if (playList !== null) {
                updatePlayListView(playList)
            }
        })
    }

    private fun initIntroView() {
        btnPlay.setBackgroundResource(R.drawable.icon_play)
        btnPlay.setOnClickListener {
            mPlayerPresenter.onPlay()
        }

        introClickableBackground.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }
    }

    private fun updateTitleView(title: String) {
        tvIntroTitle.text = title
    }

    private fun updatePlayListView(playlist: List<ITrack>) {
        tracksList.removeAllViews()

        playlist.forEach { track ->
            val trackView = layoutInflater.inflate(R.layout.item_track, tracksList, false)
            tracksList.addView(trackView)
            trackView.tvTitle.text = track.title
            trackView.setOnClickListener {
                track.play()
            }
        }
    }

    private fun updatePlayingButtons(state: PlayingState) {
        when (state) {
            PlayingState.Disabled -> {
                btnPlay.disable()
                btnPlay.setBackgroundResource(R.drawable.icon_play)
            }
            PlayingState.Idle -> {
                btnPlay.enable()
                btnPlay.setBackgroundResource(R.drawable.icon_play)
            }
            PlayingState.Playing -> {
                btnPlay.enable()
                btnPlay.setBackgroundResource(R.drawable.icon_pause)
            }
            PlayingState.Paused -> {
                btnPlay.enable()
                btnPlay.setBackgroundResource(R.drawable.icon_play)
            }
        }
    }
}
