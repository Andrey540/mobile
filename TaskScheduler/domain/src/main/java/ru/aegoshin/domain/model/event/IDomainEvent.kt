package ru.aegoshin.domain.model.event

interface IDomainEvent {
    fun getType(): String
}