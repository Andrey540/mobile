package ru.iandreyshev.androidarchitecturecomponentsapp.presenter

import ru.iandreyshev.androidarchitecturecomponentsapp.view.IPlaylistViewModel
import ru.iandreyshev.model.playlist.IPlaylistPresenter
import ru.iandreyshev.model.playlist.ITrack

class PlaylistPresenter : IPlaylistPresenter {
    private var mPlaylist = listOf<ITrack>()
    private val mViewMap = mutableListOf<IPlaylistViewModel>()

    fun subscribe(viewModel: IPlaylistViewModel)  {
        if (mViewMap.indexOf(viewModel) == -1) {
            mViewMap.add(viewModel)
        }
        refresh(viewModel)
    }

    fun unsubscribe(viewModel: IPlaylistViewModel) {
        mViewMap.remove(viewModel)
    }

    override fun updatePlaylist(playlist: List<ITrack>) {
        mPlaylist = playlist
        mViewMap.forEach {
            it.updatePlaylist(playlist)
        }
    }

    private fun refresh(viewModel: IPlaylistViewModel) {
        if (mViewMap.indexOf(viewModel) != -1) {
            viewModel.updatePlaylist(mPlaylist)
        }
    }
}
