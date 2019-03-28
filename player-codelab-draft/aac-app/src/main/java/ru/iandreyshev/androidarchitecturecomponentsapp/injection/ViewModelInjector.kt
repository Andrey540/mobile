package ru.iandreyshev.androidarchitecturecomponentsapp.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ru.iandreyshev.androidarchitecturecomponentsapp.activity.PlayerActivity
import ru.iandreyshev.androidarchitecturecomponentsapp.activity.PlaylistActivity
import ru.iandreyshev.androidarchitecturecomponentsapp.application.MusicApplication
import ru.iandreyshev.androidarchitecturecomponentsapp.view.PlayerViewModel
import ru.iandreyshev.androidarchitecturecomponentsapp.view.PlaylistViewModel


object ViewModelInjector {
    private val mPlayerPresenter = MusicApplication.getPlayerPresenter()
    private val mPlayerListPresenter = MusicApplication.getPlaylistPresenter()

    fun getPlayerViewModel(activity: PlayerActivity): PlayerViewModel {
        val provider = ViewModelProvider(activity, Factory)
        val viewModel = provider[PlayerViewModel::class.java]
        mPlayerPresenter.subscribe(viewModel)
        return viewModel
    }

    fun getPlaylistViewModel(activity: PlaylistActivity): PlaylistViewModel {
        val provider = ViewModelProvider(activity, Factory)
        val viewModel = provider[PlaylistViewModel::class.java]
        mPlayerPresenter.subscribe(viewModel)
        mPlayerListPresenter.subscribe(viewModel)
        return viewModel
    }

    private object Factory: ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass == PlayerViewModel::class.java) {
                return PlayerViewModel(mPlayerPresenter) as T
            }
            if (modelClass == PlaylistViewModel::class.java) {
                return PlaylistViewModel(mPlayerPresenter, mPlayerListPresenter) as T
            }

            throw IllegalArgumentException(
                "ViewModel ${modelClass.canonicalName} not supported")
        }

    }

}