package utils
import models.Deck
import models.Flashcard

object Utilities {

    var yellowColour = "\u001B[93m"
    val redColour = "\u001B[31m"
    val greenColour = "\u001B[32m"
    val resetColour = "\u001B[0m"

    // NOTE: JvmStatic annotation means that the methods are static i.e. we can call them over the class
    //      name; we don't have to create an object of Utilities to use them.

    @JvmStatic
    fun formatListString(notesToFormat: List<Deck>): String =
        notesToFormat
            .joinToString(separator = "\n") { deck -> "$deck" }

    @JvmStatic
    fun formatSetString(itemsToFormat: Set<Flashcard>): String =
        itemsToFormat
            .joinToString(separator = "\n") { flashcard -> "\t$flashcard" }
}
