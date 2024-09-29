package com.unidok.clientcommandextensions

enum class Match {
    STARTS,
    STARTS_IGNORE_CASE,
    CONTAINS,
    CONTAINS_IGNORE_CASE;

    fun matches(what: String, with: String): Boolean = when (this) {
        STARTS -> what.startsWith(with)
        STARTS_IGNORE_CASE -> what.startsWith(with, true)
        CONTAINS -> what.contains(with)
        CONTAINS_IGNORE_CASE -> what.contains(with, true)
    }
}