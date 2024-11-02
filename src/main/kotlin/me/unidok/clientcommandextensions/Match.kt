package me.unidok.clientcommandextensions

enum class Match {
    STARTS,
    STARTS_IGNORE_CASE,
    CONTAINS,
    CONTAINS_IGNORE_CASE;

    fun matches(first: String, second: String): Boolean = when (this) {
        STARTS -> first.startsWith(second)
        STARTS_IGNORE_CASE -> first.startsWith(second, true)
        CONTAINS -> first.contains(second)
        CONTAINS_IGNORE_CASE -> first.contains(second, true)
    }
}