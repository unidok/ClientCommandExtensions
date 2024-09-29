package com.unidok.clientcommandextensions

import com.mojang.brigadier.Command
import com.mojang.brigadier.Message
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder.argument
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import java.util.concurrent.CompletableFuture

fun ArgumentBuilder<FabricClientCommandSource, *>.literalBuilder(
    name: String,
    body: LiteralArgumentBuilder<FabricClientCommandSource>.() -> Unit
): LiteralArgumentBuilder<FabricClientCommandSource> {
    val builder = literal<FabricClientCommandSource>(name)
    body(builder)
    return builder
}

fun ArgumentBuilder<FabricClientCommandSource, *>.literal(
    name: String,
    body: LiteralArgumentBuilder<FabricClientCommandSource>.() -> Unit
) {
    then(literalBuilder(name, body))
}



fun <T> ArgumentBuilder<FabricClientCommandSource, *>.argumentBuilder(
    name: String,
    argumentType: ArgumentType<T>,
    body: RequiredArgumentBuilder<FabricClientCommandSource, T>.() -> Unit
): RequiredArgumentBuilder<FabricClientCommandSource, T> {
    val builder = argument<FabricClientCommandSource, T>(name, argumentType)
    body(builder)
    return builder
}

fun <T> ArgumentBuilder<FabricClientCommandSource, *>.argument(
    name: String,
    argumentType: ArgumentType<T>,
    body: RequiredArgumentBuilder<FabricClientCommandSource, T>.() -> Unit
) {
    then(argumentBuilder(name, argumentType, body))
}



fun ArgumentBuilder<FabricClientCommandSource, *>.execute(
    body: CommandContext<FabricClientCommandSource>.() -> Unit
): ArgumentBuilder<FabricClientCommandSource, *> {
    return executes { context ->
        body(context)
        return@executes Command.SINGLE_SUCCESS // 1
    }
}



fun RequiredArgumentBuilder<FabricClientCommandSource, String>.smartSuggest(match: Match, body: SuggestionsBuilder.() -> Unit) {
    suggests(object : SuggestionProvider<FabricClientCommandSource> {
        override fun getSuggestions(
            context: CommandContext<FabricClientCommandSource>,
            builder: SuggestionsBuilder
        ): CompletableFuture<Suggestions> {
            val newBuilder = object : SuggestionsBuilder(builder.input, builder.start) {
                override fun suggest(text: String) =
                    if (match.matches(text, remaining)) super.suggest(text) else this

                override fun suggest(text: String, tooltip: Message) =
                    if (match.matches(text, remaining)) super.suggest(text, tooltip) else this
            }
            body(newBuilder)
            return newBuilder.buildFuture()
        }
    })
}

fun RequiredArgumentBuilder<FabricClientCommandSource, *>.suggest(body: (CommandContext<FabricClientCommandSource>, SuggestionsBuilder) -> Unit) {
    suggests(object : SuggestionProvider<FabricClientCommandSource> {
        override fun getSuggestions(
            context: CommandContext<FabricClientCommandSource>,
            builder: SuggestionsBuilder
        ): CompletableFuture<Suggestions> {
            body(context, builder)
            return builder.buildFuture()
        }
    })
}

fun SuggestionsBuilder.suggest(vararg suggestions: String) {
    for (suggestion in suggestions) suggest(suggestion)
}

fun SuggestionsBuilder.suggest(suggestions: Iterable<String>) {
    for (suggestion in suggestions) suggest(suggestion)
}



inline fun <reified T> CommandContext<*>.getArgument(name: String): T = getArgument<T>(name, T::class.java)
