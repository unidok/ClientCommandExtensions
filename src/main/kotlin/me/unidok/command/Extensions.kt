package me.unidok.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.Message
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder

inline fun <S> buildLiteral(
    name: String,
    builder: LiteralArgumentBuilder<S>.() -> Unit
): LiteralArgumentBuilder<S> =
    LiteralArgumentBuilder.literal<S>(name).apply(builder)

inline fun <S> ArgumentBuilder<S, *>.literal(
    name: String,
    builder: LiteralArgumentBuilder<S>.() -> Unit
) {
    then(buildLiteral(name, builder))
}

@JvmName("buildArgumentWithSuggestions")
inline fun <T, A, S> buildArgument(
    name: String,
    argumentType: A,
    builder: RequiredArgumentBuilder<S, T>.() -> Unit
): RequiredArgumentBuilder<S, T>
        where A : ArgumentType<T>,
              A : SuggestionProvider<S> =
    RequiredArgumentBuilder
        .argument<S, T>(name, argumentType)
        .suggests(argumentType)
        .apply(builder)

inline fun <T, S> buildArgument(
    name: String,
    argumentType: ArgumentType<T>,
    builder: RequiredArgumentBuilder<S, T>.() -> Unit
): RequiredArgumentBuilder<S, T> =
    RequiredArgumentBuilder
        .argument<S, T>(name, argumentType)
        .apply(builder)

@JvmName("argumentWithSuggestions")
inline fun <T, A, S> ArgumentBuilder<S, *>.argument(
    name: String,
    argumentType: A,
    builder: RequiredArgumentBuilder<S, T>.() -> Unit
) where A : ArgumentType<T>, A : SuggestionProvider<S> {
    then(buildArgument(name, argumentType, builder))
}


inline fun <T, S> ArgumentBuilder<S, *>.argument(
    name: String,
    argumentType: ArgumentType<T>,
    builder: RequiredArgumentBuilder<S, T>.() -> Unit
) {
    then(buildArgument(name, argumentType, builder))
}


inline fun <S> ArgumentBuilder<S, *>.runs(
    crossinline builder: CommandContext<S>.() -> Unit
) {
    executes { context ->
        builder(context)
        Command.SINGLE_SUCCESS
    }
}




inline fun RequiredArgumentBuilder<*, *>.smartSuggests(
    crossinline predicate: (suggestion: String, input: String) -> Boolean,
    crossinline builder: SuggestionsBuilder.() -> Unit
) {
    suggests { _, builder ->
        val newBuilder = object : SuggestionsBuilder(builder.input, builder.start) {
            override fun suggest(text: String) = this.suggest(text, null)

            override fun suggest(text: String, tooltip: Message?) =
                if (predicate(text, remaining)) super.suggest(text, tooltip) else this
        }
        builder(newBuilder)
        newBuilder.buildFuture()
    }
}

inline fun RequiredArgumentBuilder<*, *>.suggests(
    crossinline builder: SuggestionsBuilder.() -> Unit
) {
    suggests { _, builder ->
        builder(builder)
        builder.buildFuture()
    }
}

fun SuggestionsBuilder.suggest(vararg suggestions: String) {
    for (suggestion in suggestions) suggest(suggestion)
}

fun SuggestionsBuilder.suggest(suggestions: Iterable<String>) {
    for (suggestion in suggestions) suggest(suggestion)
}



inline fun <reified T> CommandContext<*>.getArgument(name: String): T {
    return getArgument<T>(name, T::class.java)
}