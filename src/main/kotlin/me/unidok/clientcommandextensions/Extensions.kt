package me.unidok.clientcommandextensions

import com.mojang.brigadier.Command
import com.mojang.brigadier.Message
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

inline fun buildLiteral(
    name: String,
    builder: LiteralArgumentBuilder<FabricClientCommandSource>.() -> Unit
): LiteralArgumentBuilder<FabricClientCommandSource> =
    LiteralArgumentBuilder.literal<FabricClientCommandSource>(name).apply(builder)

inline fun ArgumentBuilder<FabricClientCommandSource, *>.literal(
    name: String,
    builder: LiteralArgumentBuilder<FabricClientCommandSource>.() -> Unit
) {
    then(buildLiteral(name, builder))
}



inline fun <T> buildArgument(
    name: String,
    argumentType: ArgumentType<T>,
    builder: RequiredArgumentBuilder<FabricClientCommandSource, T>.() -> Unit
): RequiredArgumentBuilder<FabricClientCommandSource, T> =
    RequiredArgumentBuilder.argument<FabricClientCommandSource, T>(name, argumentType).apply(builder)

inline fun <T> ArgumentBuilder<FabricClientCommandSource, *>.argument(
    name: String,
    argumentType: ArgumentType<T>,
    builder: RequiredArgumentBuilder<FabricClientCommandSource, T>.() -> Unit
) {
    then(buildArgument(name, argumentType, builder))
}



inline fun ArgumentBuilder<FabricClientCommandSource, *>.runs(
    crossinline builder: CommandContext<FabricClientCommandSource>.() -> Unit
) {
    executes { context ->
        builder(context)
        Command.SINGLE_SUCCESS
    }
}




inline fun RequiredArgumentBuilder<FabricClientCommandSource, String>.smartSuggests(
    match: Match,
    crossinline builder: SuggestionsBuilder.() -> Unit
) {
    suggests { context, builder ->
        val newBuilder = object : SuggestionsBuilder(builder.input, builder.start) {
            override fun suggest(text: String) =
                if (match.matches(text, remaining)) super.suggest(text) else this

            override fun suggest(text: String, tooltip: Message) =
                if (match.matches(text, remaining)) super.suggest(text, tooltip) else this
        }
        builder(newBuilder)
        newBuilder.buildFuture()
    }
}

inline fun RequiredArgumentBuilder<FabricClientCommandSource, *>.suggests(
    crossinline builder: SuggestionsBuilder.() -> Unit
) {
    suggests { context, builder ->
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