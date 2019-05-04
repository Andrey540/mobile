package ru.aegoshin.domain.model.event

import ru.aegoshin.domain.model.event.IDomainEvent

interface IEventDispather {
    fun dispatch(event: IDomainEvent)
    fun subscribe(handler: IDomainEventHandler)
    fun unsubscribe(handler: IDomainEventHandler)
}