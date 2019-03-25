package ru.iandreyshev.model.repository

interface ISong {
    val id: Long
    val title: String
    val resource: Int
    val posterUrl: String
}
