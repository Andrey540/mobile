package ru.aegoshin.domain.model.event

interface IDomainEventHandler {
    fun handle(event: IDomainEvent)
    fun isSubscribedToEvent(event: IDomainEvent): Boolean
}