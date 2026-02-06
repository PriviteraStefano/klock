package org.stefanoprivitera.klock.routes.util

import io.ktor.http.*
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Extension function to safely parse query parameters
 */
object QueryParamParser {
    fun Parameters.getStringOrNull(key: String): String? = this[key]
    fun Parameters.getIntOrNull(key: String): Int? = this[key]?.toIntOrNull()
    fun Parameters.getLongOrNull(key: String): Long? = this[key]?.toLongOrNull()
    fun Parameters.getDoubleOrNull(key: String): Double? = this[key]?.toDoubleOrNull()
    fun Parameters.getBooleanOrNull(key: String): Boolean? = this[key]?.toBooleanStrictOrNull()
    fun Parameters.getLocalDateOrNull(key: String): LocalDate? =
        this[key]?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
    @OptIn(ExperimentalUuidApi::class)
    fun Parameters.getUuidOrNull(key: String): Uuid? =
        this[key]?.let { runCatching { Uuid.parse(it) }.getOrNull() }
    fun Parameters.getStringListOrNull(key: String, separator: String = ","): List<String>? =
        this[key]?.split(separator)?.map { it.trim() }?.filter { it.isNotEmpty() }
}

///**
// * Extension function for ApplicationCall to get query parameters easily
// */
//inline fun <reified T> ApplicationCall.queryParam(key: String): T? {
//    return when (T::class) {
//        String::class -> QueryParamParser.run { request.queryParameters.getStringOrNull(key) as? T }
//        Int::class -> QueryParamParser.run { request.queryParameters.getIntOrNull(key) as? T }
//        Long::class -> QueryParamParser.run { request.queryParameters.getLongOrNull(key) as? T }
//        Double::class -> QueryParamParser.run { request.queryParameters.getDoubleOrNull(key) as? T }
//        Boolean::class -> QueryParamParser.run { request.queryParameters.getBooleanOrNull(key) as? T }
//        LocalDate::class -> QueryParamParser.run { request.queryParameters.getLocalDateOrNull(key) as? T }
//        else -> null
//    }
//}
