package ru.aegoshin.domain.model.category

interface IImmutableCategory {
    fun getId(): CategoryId
    fun getTitle(): String
}