package ru.iandreyshev.mymusicapplication.presenter

import ru.iandreyshev.model.playlist.IPlaylistPresenter
import ru.iandreyshev.model.playlist.ITrack

class PlaylistPresenter : IPlaylistPresenter {
    private var mPlaylist = listOf<ITrack>()
    private val mViewMap = mutableMapOf<IView, Boolean>()

    interface IView {
        fun updatePlaylist(playlist: List<ITrack>)
    }

    fun onAttach(view: IView) {
        mViewMap[view] = true

        view.updatePlaylist(mPlaylist)
    }

    fun onDetach(view: IView) {
        if (mViewMap.contains(view)) {
            mViewMap[view] = false
        }
    }

    fun onFinish(view: IView) {
        mViewMap.remove(view)
    }

    override fun updatePlaylist(playlist: List<ITrack>) {
        mPlaylist = playlist
        mViewMap.entries.forEach { entry ->
            if (entry.value) {
                entry.key.updatePlaylist(playlist)
            }
        }
    }
}
