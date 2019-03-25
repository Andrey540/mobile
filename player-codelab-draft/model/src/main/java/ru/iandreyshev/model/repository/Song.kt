package ru.iandreyshev.model.repository

data class Song(
    override val id: Long,
    override val title: String,
    override val resource: Int,
    override val posterUrl: String
) : ISong
