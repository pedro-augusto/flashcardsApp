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

    @JvmStatic
    fun validRange(numberToCheck: Int, min: Int, max: Int): Boolean {
        return numberToCheck in min..max
    }

    @JvmStatic
    fun readValidInput(attribute: String, prompt: String, month: Int? = null): Int {
        var input = ScannerInput.readNextInt(prompt)
        var min: Int? = null
        var max: Int? = null

        when (attribute) {
            "theme", "type of word" -> { min = 1; max = 5 }
            "level", "option for generating a deck" -> { min = 1; max = 4 }
        }

        do {
            if (validRange(input, min!!, max!!)) {
                return input
            } else {
                println("Invalid $attribute $input.")
                input = ScannerInput.readNextInt(prompt)
            }
        } while (true)
    }

    fun readValidTitle(): String {
        var title: String
        do {
            title = ScannerInput.readNextLine("Enter a title for the deck: ")
        } while (title.replace("\\s".toRegex(), "").length >= 2)

        return title
    }
}
