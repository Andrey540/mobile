package ru.iandreyshev.model.repository

interface IRepository {
    fun getAllSongs(): List<ISong>
    fun getSongById(id: Long): ISong?
}
