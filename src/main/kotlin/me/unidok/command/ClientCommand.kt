package me.unidok.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

abstract class ClientCommand(
    val name: String,
    vararg val aliases: String
) {
    abstract fun build(command: LiteralArgumentBuilder<FabricClientCommandSource>)

    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            register(dispatcher, name)
            for (alias in aliases) register(dispatcher, alias)
        }
    }

    private fun register(
        dispatcher: CommandDispatcher<FabricClientCommandSource>,
        name: String
    ) {
        val builder = LiteralArgumentBuilder.literal<FabricClientCommandSource>(name)
        build(builder)
        dispatcher.register(builder)
    }
}