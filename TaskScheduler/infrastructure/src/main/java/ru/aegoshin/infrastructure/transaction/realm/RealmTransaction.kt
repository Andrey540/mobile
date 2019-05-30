package ru.aegoshin.infrastructure.transaction.realm

import io.realm.Realm
import ru.aegoshin.application.transaction.ITransaction

class RealmTransaction(private val realm: Realm) : ITransaction {
    override fun begin() {
        realm.beginTransaction()
    }

    override fun commit() {
        realm.commitTransaction()
    }

    override fun rollback() {
        realm.cancelTransaction()
    }

    override fun execute(executor: () -> Unit) {
        try {
            realm.beginTransaction()
            executor()
            realm.commitTransaction()
        } catch (exception: Exception) {
            realm.cancelTransaction()
            throw exception
        }
    }
}