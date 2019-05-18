package ru.aegoshin.infrastructure.transaction

interface ITransaction {
    fun begin()
    fun commit()
    fun rollback()
}