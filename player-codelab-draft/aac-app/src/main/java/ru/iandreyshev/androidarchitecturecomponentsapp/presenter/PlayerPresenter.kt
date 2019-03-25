package ru.iandreyshev.androidarchitecturecomponentsapp.presenter

import ru.iandreyshev.model.player.IPlayerPresenter
import ru.iandreyshev.model.player.Player

class PlayerPresenter(
    private val player: Player
) {
    fun onPlay() = player.onPlay()
    fun onStop() = player.onStop()
    fun onRestart() = player.onRestart()
    fun onChangeTimePosition(timePercent: Float) = player.onChangeTimelinePosition(timePercent)
    fun subscribe(presenter: IPlayerPresenter) = player.subscribe(presenter)
}
