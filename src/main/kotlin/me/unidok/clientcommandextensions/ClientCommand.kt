package me.unidok.clientcommandextensions

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

abstract class ClientCommand(
    val name: String,
    vararg val aliases: String
) {
    abstract fun build(command: LiteralArgumentBuilder<FabricClientCommandSource>)

    fun register() {
        register(name)
        for (alias in aliases) register(alias)
    }

    private fun register(name: String) {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            val builder = LiteralArgumentBuilder.literal<FabricClientCommandSource>(name)
            build(builder)
            dispatcher.register(builder)
        }
    }
}