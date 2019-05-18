package ru.aegoshin.taskscheduler.application.transaction.realm

import io.realm.DynamicRealm
import ru.aegoshin.infrastructure.transaction.ITransaction

class RealmTransaction(private val realm: DynamicRealm) : ITransaction {
    override fun begin() {
        realm.beginTransaction()
    }

    override fun commit() {
        realm.commitTransaction()
    }

    override fun rollback() {
        realm.cancelTransaction()
    }
}