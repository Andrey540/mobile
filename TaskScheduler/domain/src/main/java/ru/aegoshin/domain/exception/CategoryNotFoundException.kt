package ru.aegoshin.domain.exception

import ru.aegoshin.domain.model.category.CategoryId

class CategoryNotFoundException(categoryId: CategoryId) : DomainException("Can not find category with id $categoryId")