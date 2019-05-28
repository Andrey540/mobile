package ru.aegoshin.domain.model.category

import java.util.*

class CategoryId(private val id: UUID) {
    override fun toString(): String {
        return id.toString()
    }

    fun equalTo(categoryId: CategoryId): Boolean {
        return toString() == categoryId.toString()
    }

    companion object {
        fun stringToUuid(id: String): UUID {
            return UUID.fromString(id)
        }
    }
}