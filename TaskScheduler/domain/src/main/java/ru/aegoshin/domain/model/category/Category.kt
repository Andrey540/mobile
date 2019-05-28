package ru.aegoshin.domain.model.category

class Category(
    private var id: CategoryId,
    private var title: String
) : IImmutableCategory {
    init {
        validateTitle(title)
    }

    override fun getId(): CategoryId {
        return id
    }

    override fun getTitle(): String {
        return title
    }

    fun setTitle(newTitle: String) {
        validateTitle(newTitle)
        title = newTitle
    }

    private fun validateTitle(title: String) {
        if (title.isEmpty()) {
            throw IllegalArgumentException("Category title cannot be empty")
        }
    }
}