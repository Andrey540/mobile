package ru.iandreyshev.mymusicapplication.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_player.*
import ru.iandreyshev.model.player.PlayingState
import ru.iandreyshev.mymusicapplication.R
import ru.iandreyshev.mymusicapplication.application.MusicApplication
import ru.iandreyshev.mymusicapplication.presenter.PlayerPresenter
import ru.iandreyshev.utils.disable
import ru.iandreyshev.utils.enable
import ru.iandreyshev.utils.toHumanReadableTime

class PlayerActivity : AppCompatActivity(), PlayerPresenter.IView {
    private val mPlayerPresenter = MusicApplication.getPlayerPresenter()

    override fun updateTitle(title: String) =
        updateTitleView(title)

    override fun updatePoster(url: String) =
        updatePosterView(url)

    override fun updateTimeline(progress: Float, currentTime: String) =
        updateTimelineView(progress, currentTime)

    override fun updatePlaying(state: PlayingState) =
        updatePlayingButtons(state)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        initButtons()
        initTimeline()
    }

    override fun onResume() {
        super.onResume()
        mPlayerPresenter.onAttach(this)
    }

    override fun onPause() {
        super.onPause()
        mPlayerPresenter.onDetach(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) {
            mPlayerPresenter.onFinish(this)
        }
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
        tvTime.text = currentTime.toInt().toHumanReadableTime()
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
