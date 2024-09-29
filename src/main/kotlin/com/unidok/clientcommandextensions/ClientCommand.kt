package com.unidok.clientcommandextensions

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

abstract class ClientCommand(name: String) : LiteralArgumentBuilder<FabricClientCommandSource>(name) {
    abstract fun initialize()

    fun register() {
        ClientCommandRegistrationCallback.EVENT.register(this::dispatcher)
    }

    private fun dispatcher(dispatcher: CommandDispatcher<FabricClientCommandSource>, registryAccess: CommandRegistryAccess) {
        initialize()
        dispatcher.register(this)
    }
}