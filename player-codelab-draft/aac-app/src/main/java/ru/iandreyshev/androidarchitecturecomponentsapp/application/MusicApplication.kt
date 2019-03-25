package ru.iandreyshev.androidarchitecturecomponentsapp.application

import android.app.Application
import ru.iandreyshev.androidarchitecturecomponentsapp.presenter.PlayerPresenter
import ru.iandreyshev.model.player.Player
import ru.iandreyshev.model.playlist.Playlist
import ru.iandreyshev.model.repository.Repository
import ru.iandreyshev.androidarchitecturecomponentsapp.presenter.PlaylistPresenter

class MusicApplication : Application() {
    private val mRepository = Repository()
    private val mPlaylist = Playlist(mRepository.getAllSongs(), ::onSelectSong)
    private val mPlayer = Player(this)
    private lateinit var mPlayerPresenter: PlayerPresenter
    private lateinit var mPlayListPresenter: PlaylistPresenter

    override fun onCreate() {
        super.onCreate()

        instance = this

        mPlayerPresenter = PlayerPresenter(instance.mPlayer)
        mPlayListPresenter = PlaylistPresenter(instance.mPlaylist)
    }

    private fun onSelectSong(songId: Long) {
        val songToPlay = mRepository.getSongById(songId) ?: return
        mPlayer.setSong(songToPlay)
        mPlayer.onPlay()
    }

    companion object {
        private lateinit var instance: MusicApplication

        fun getPlaylistPresenter(): PlaylistPresenter {
            return instance.mPlayListPresenter
        }

        fun getPlayerPresenter(): PlayerPresenter {
            return instance.mPlayerPresenter
        }
    }
}