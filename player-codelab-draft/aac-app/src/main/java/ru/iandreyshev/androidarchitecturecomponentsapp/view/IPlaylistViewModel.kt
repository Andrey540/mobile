package ru.iandreyshev.androidarchitecturecomponentsapp.view

import ru.iandreyshev.model.playlist.ITrack

interface IPlaylistViewModel {
    fun updatePlaylist(playlist: List<ITrack>)
}