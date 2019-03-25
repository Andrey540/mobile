package ru.iandreyshev.moxyapp.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_player.*
import ru.iandreyshev.model.player.PlayingState
import ru.iandreyshev.moxyapp.R
import ru.iandreyshev.utils.disable
import ru.iandreyshev.utils.enable

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        initButtons()
        initTimeline()
    }

    private fun initButtons() {
        btnStop.setBackgroundResource(R.drawable.icon_stop)
        btnStop.setOnClickListener {
            // TODO: Добавить перенаправление события в презентер
        }

        btnPlay.setBackgroundResource(R.drawable.icon_play)
        btnPlay.setOnClickListener {
            // TODO: Добавить перенаправление события в презентер
        }

        btnRestart.setBackgroundResource(R.drawable.icon_restart)
        btnRestart.setOnClickListener {
            // TODO: Добавить перенаправление события в презентер
        }
    }

    private fun initTimeline() {
        sbTimeLine.progress = TIMELINE_MIN
        sbTimeLine.max = TIMELINE_MAX
        sbTimeLine.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // TODO: Добавить перенаправление события в презентер
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) = Unit
            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
        })
    }

    private fun updateTitleView(title: String) {
        tvTitle.text = title
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
