package ru.aegoshin.application.transaction

interface ITransaction {
    fun begin()
    fun commit()
    fun rollback()
    fun execute(executor: () -> Unit)
}