package ru.aegoshin.infrastructure.event

import ru.aegoshin.domain.model.event.IDomainEvent
import ru.aegoshin.domain.model.event.IDomainEventHandler
import ru.aegoshin.domain.model.event.IEventDispather

class EventDispatcher private constructor() : IEventDispather {
    private val mHandlers = mutableListOf<IDomainEventHandler>()

    private object Holder { val INSTANCE = EventDispatcher() }

    override fun dispatch(event: IDomainEvent) {
        mHandlers.forEach {
            if (it.isSubscribedToEvent(event)) {
                it.handle(event)
            }
        }
    }

    override fun subscribe(handler: IDomainEventHandler) {
        if (mHandlers.indexOf(handler) == -1) {
            mHandlers.add(handler)
        }
    }

    override fun unsubscribe(handler: IDomainEventHandler) {
        mHandlers.remove(handler)
    }

    companion object {
        val instance: EventDispatcher by lazy { Holder.INSTANCE }
    }
}