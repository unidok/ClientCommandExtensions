# [Releases](https://github.com/unidok/ClientCommandExtensions/releases)

# Example
```kt
// TestCommand.kt
object TestCommand : ClientCommand("test", "t") { // command 'test' and alias 't'
    override fun build(command: LiteralArgumentBuilder<FabricClientCommandSource>) {
        command.literal("message") {
            argument("msg", StringArgumentType.greedyString()) { // with argument
                runs {
                    val argument = getArgument<String>("msg")
                    source.sendFeedback(Text.literal("/test message $argument"))
                }
                smartSuggests(Match.CONTAINS_IGNORE_CASE) { // smart suggest (by argument input)
                    suggest("suggestion")
                }
            } 
            runs {
                source.sendFeedback(Text.literal("/test message"))
            }
        }
        command.runs {
            source.sendFeedback(Text.literal("/test"))
        }
    }
}


// Mod.kt
class Mod : ClientModInitializer {
    override fun onInitializeClient() {
        TestCommand.register() // register this command
    }
}
```
