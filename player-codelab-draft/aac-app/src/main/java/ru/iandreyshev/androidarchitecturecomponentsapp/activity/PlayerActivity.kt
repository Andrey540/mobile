package ru.iandreyshev.androidarchitecturecomponentsapp.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_player.*
import ru.iandreyshev.androidarchitecturecomponentsapp.R
import ru.iandreyshev.androidarchitecturecomponentsapp.application.MusicApplication
import ru.iandreyshev.androidarchitecturecomponentsapp.injection.ViewModelInjector
import ru.iandreyshev.androidarchitecturecomponentsapp.view.IPlayerViewModel
import ru.iandreyshev.androidarchitecturecomponentsapp.view.PlayerViewModel
import ru.iandreyshev.androidarchitecturecomponentsapp.view.TimelineViewModel
import ru.iandreyshev.model.player.PlayingState
import ru.iandreyshev.utils.disable
import ru.iandreyshev.utils.enable

class PlayerActivity : AppCompatActivity() {
    private val mPlayerPresenter = MusicApplication.getPlayerPresenter()
    private lateinit var mViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        initButtons()
        initTimeline()
        initViewModel()
    }

    private fun initViewModel() {
        mViewModel = ViewModelInjector.getPlayerViewModel(this)

        mViewModel.trackTitle.observe(this, Observer { newTitle: String? ->
            updateTitleView(newTitle.orEmpty())
        })
        mViewModel.trackPosterUrl.observe(this, Observer { newPoster: String? ->
            updatePosterView(newPoster.orEmpty())
        })
        mViewModel.trackTimeline.observe(this, Observer { newTimeline: TimelineViewModel? ->
            if (newTimeline !== null) {
                updateTimelineView(newTimeline.percent, newTimeline.time)
            }
        })
        mViewModel.playingState.observe(this, Observer { newState: PlayingState? ->
            if (newState !== null) {
                updatePlayingButtons(newState)
            }
        })
    }

    private fun initButtons() {
        btnStop.setBackgroundResource(R.drawable.icon_stop)
        btnStop.setOnClickListener {
            mPlayerPresenter.onStop()
        }

        btnPlay.setBackgroundResource(R.drawable.icon_play)
        btnPlay.setOnClickListener {
            mPlayerPresenter.onPlay()
        }

        btnRestart.setBackgroundResource(R.drawable.icon_restart)
        btnRestart.setOnClickListener {
            mPlayerPresenter.onRestart()
        }
    }

    private fun initTimeline() {
        sbTimeLine.progress = TIMELINE_MIN
        sbTimeLine.max = TIMELINE_MAX
        sbTimeLine.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mPlayerPresenter.onChangeTimePosition((sbTimeLine.progress).toFloat() / TIMELINE_MAX)
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) = Unit
            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
        })
    }

    private fun updateTitleView(title: String) {
        tvTitle.text = title
    }

    private fun updatePosterView(url: String) {
        Picasso.get()
            .load(url)
            .into(imgPoster)
    }

    private fun updateTimelineView(progress: Float, currentTime: String) {
        tvTime.text = currentTime
        sbTimeLine.progress = (TIMELINE_MAX * progress).toInt()
    }

    private fun updatePlayingButtons(state: PlayingState) {
        when (state) {
            PlayingState.Disabled -> {
                btnStop.disable()
                btnPlay.disable()
                btnRestart.disable()
                sbTimeLine.disable()

                btnPlay.setBackgroundResource(R.drawable.icon_play)
            }
            PlayingState.Idle -> {
                btnStop.disable()
                btnPlay.enable()
                btnRestart.disable()
                sbTimeLine.disable()

                btnPlay.setBackgroundResource(R.drawable.icon_play)
            }
            PlayingState.Playing -> {
                btnStop.enable()
                btnPlay.enable()
                btnRestart.enable()
                sbTimeLine.enable()

                btnPlay.setBackgroundResource(R.drawable.icon_pause)
            }
            PlayingState.Paused -> {
                btnStop.enable()
                btnPlay.enable()
                btnRestart.enable()
                sbTimeLine.disable()

                btnPlay.setBackgroundResource(R.drawable.icon_play)
            }
        }
    }

    companion object {
        private const val TIMELINE_MIN = 0
        private const val TIMELINE_MAX = 100
    }

}
