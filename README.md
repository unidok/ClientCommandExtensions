# [Releases](https://github.com/unidok/ClientCommandExtensions/releases)

# Example
```kt
// TestCommand.kt
object TestCommand : ClientCommand("test") {
    override fun initialize() {
        literal("message") {
            argument("msg", StringArgumentType.greedyString()) { // with argument
                execute {
                    val argument = getArgument<String>("msg")
                    source.sendFeedback(Text.literal("/test message $argument"))
                }
                smartSuggest(Match.CONTAINS_IGNORE_CASE) { // smart suggest (by argument input)
                    suggest("default message", "any message", "m e s s a g e")
                }
            } 
            execute {
                source.sendFeedback(Text.literal("/test message"))
            }
        }
        execute {
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
