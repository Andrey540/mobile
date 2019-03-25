package ru.iandreyshev.model.player

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import ru.iandreyshev.model.repository.ISong
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Player(
    private val context: Context
) {

    private var mAndroidPlayer: MediaPlayer? = null

    private var mPresenter: IPlayerPresenter? = null
    private var mPlayingState: PlayingState = PlayingState.Disabled
    private var mTitle: String? = ""
    private var mPosterUrl: String? = ""
    private var mTimeInMillis: Int = 0
    private var mPercent: Float = 0f

    private var mSeekBarPositionUpdateTask: Runnable = Runnable {}
    private var mExecutor = Executors.newSingleThreadScheduledExecutor()

    init {
        mSeekBarPositionUpdateTask = Runnable {
            synchronized(context) {
                Handler(context.mainLooper).post { notifyProgressUpdated() }
            }
        }
        mExecutor.scheduleAtFixedRate(
            mSeekBarPositionUpdateTask,
            PROGRESS_UPDATE_INITIAL_PAUSE_MS,
            PROGRESS_UPDATE_PAUSE_MS,
            TimeUnit.MILLISECONDS
        )
    }

    /*
    * Функция для смены играющей музыки из плей-листа
    * */
    fun setSong(song: ISong) {
        mAndroidPlayer?.release()
        mAndroidPlayer = MediaPlayer.create(context, song.resource)
        mAndroidPlayer?.setOnCompletionListener { onStop() }

        mTitle = song.title
        mPosterUrl = song.posterUrl
        mTimeInMillis = 0
        mPercent = 0f
        mPlayingState = PlayingState.Idle

        notifyObserver()
    }

    fun onPlay() {
        /*
        * Если песня ещё не выбрана, то плеер не будет проинициалзирован, значит нет смысла
        * реагировать на это событие
        * */
        mAndroidPlayer ?: return

        when (mPlayingState) {
            PlayingState.Playing -> {
                mAndroidPlayer?.pause()
                mPlayingState = PlayingState.Paused
            }
            PlayingState.Idle,
            PlayingState.Paused -> {
                mAndroidPlayer?.start()
                mPlayingState = PlayingState.Playing
            }
            else -> {
            }
        }

        notifyObserver()
    }

    fun onStop() {
        mAndroidPlayer ?: return

        if (mPlayingState == PlayingState.Playing || mPlayingState == PlayingState.Paused) {
            mAndroidPlayer?.seekTo(0)
            mAndroidPlayer?.pause()

            mTimeInMillis = 0
            mPercent = 0f
            mPlayingState = PlayingState.Idle

            notifyObserver()
        }
    }

    fun onRestart() {
        mAndroidPlayer ?: return

        if (mPlayingState == PlayingState.Playing || mPlayingState == PlayingState.Paused) {
            mAndroidPlayer?.seekTo(0)
            mAndroidPlayer?.start()

            mTimeInMillis = 0
            mPercent = 0f
            mPlayingState = PlayingState.Playing

            notifyObserver()
        }
    }

    /*
    * Параметр position определяет процент на который нужно выставить текущее воспроизведение
    * position может быть от 0 до 1 включительно
    * */
    fun onChangeTimelinePosition(position: Float) {
        val duration = mAndroidPlayer?.duration ?: return
        val positionInMillis = duration * position.coerceIn(0f, 1f)
        mAndroidPlayer?.seekTo(positionInMillis.toInt())

        notifyProgressUpdated()
    }

    fun subscribe(presenter: IPlayerPresenter) {
        mPresenter = presenter
        notifyObserver()
    }

    fun unsubscribe() {
        mPresenter = null
    }

    private fun notifyObserver() {
        mPresenter?.updatePlaying(mPlayingState)
        mPresenter?.updateTitle(mTitle)
        mPresenter?.updatePoster(mPosterUrl)
        mPresenter?.updateTimeline(Timeline(mTimeInMillis, mPercent))
    }

    private fun notifyProgressUpdated() {
        mAndroidPlayer ?: return
        if (mPlayingState != PlayingState.Playing) return
        val duration = mAndroidPlayer?.duration ?: return
        val currentTime = mAndroidPlayer?.currentPosition ?: return

        if (duration == 0) return

        mPercent = currentTime.toFloat() / duration
        mTimeInMillis = currentTime

        val timeLine = Timeline(mTimeInMillis, mPercent)
        mPresenter?.updateTimeline(timeLine)
    }

    companion object {
        private const val PROGRESS_UPDATE_INITIAL_PAUSE_MS = 0L
        private const val PROGRESS_UPDATE_PAUSE_MS = 1000L
    }

}