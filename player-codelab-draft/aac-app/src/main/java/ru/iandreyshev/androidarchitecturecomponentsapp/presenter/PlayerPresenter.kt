package ru.iandreyshev.androidarchitecturecomponentsapp.presenter

import ru.iandreyshev.androidarchitecturecomponentsapp.view.IPlayerViewModel
import ru.iandreyshev.model.player.IPlayerPresenter
import ru.iandreyshev.model.player.Player
import ru.iandreyshev.model.player.PlayingState
import ru.iandreyshev.model.player.Timeline

class PlayerPresenter(
    private val player: Player
) : IPlayerPresenter {
    private var mTitle: String = ""
    private var mPosterUrl: String = ""
    private var mTimeline: Timeline = Timeline(0, 0f)
    private var mPlayingState: PlayingState = PlayingState.Disabled
    private val mViewMap = mutableListOf<IPlayerViewModel>()

    fun onPlay() = player.onPlay()
    fun onStop() = player.onStop()
    fun onRestart() = player.onRestart()
    fun onChangeTimePosition(timePercent: Float) = player.onChangeTimelinePosition(timePercent)

    fun subscribe(viewModel: IPlayerViewModel) {
        if (mViewMap.indexOf(viewModel) == -1) {
            mViewMap.add(viewModel)
        }
        refresh(viewModel)
    }

    fun unsubscribe(viewModel: IPlayerViewModel) {
        mViewMap.remove(viewModel)
    }

    override fun updateTitle(title: String?) {
        mTitle = title ?: ""
        updateView { view ->
            view.updateTitle(mTitle)
        }
    }

    override fun updatePoster(url: String?) {
        mPosterUrl = url ?: ""
        updateView { view ->
            view.updatePoster(mPosterUrl)
        }
    }

    override fun updateTimeline(timeline: Timeline) {
        mTimeline = timeline
        updateView { view ->
            view.updateTimeline(mTimeline)
        }
    }

    override fun updatePlaying(state: PlayingState) {
        mPlayingState = state
        updateView { view ->
            view.updatePlaying(mPlayingState)
        }
    }

    private fun updateView(updateCallback: (IPlayerViewModel) -> Unit) {
        mViewMap.forEach {
            updateCallback(it)
        }
    }

    private fun refresh(viewModel: IPlayerViewModel) {
        if (mViewMap.indexOf(viewModel) != -1) {
            viewModel.updateTitle(mTitle)
            viewModel.updatePoster(mPosterUrl)
            viewModel.updateTimeline(mTimeline)
            viewModel.updatePlaying(mPlayingState)
        }
    }
}
