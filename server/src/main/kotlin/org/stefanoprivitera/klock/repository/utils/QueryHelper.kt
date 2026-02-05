package org.stefanoprivitera.klock.repository.utils

import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.jdbc.Query
import org.jetbrains.exposed.v1.jdbc.andWhere

fun <T> Query.andWhereIfNotNull(value: T?, condition: (T)-> Op<Boolean>): Query =
    value?.let { this.andWhere { condition(it) } } ?: this
