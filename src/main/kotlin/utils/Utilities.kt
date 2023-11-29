package utils
import models.Deck
import models.Flashcard

object Utilities {

    /**
     * ANSI color codes for text formatting in the console.
     * These constants can be used to change the text color in the console output.
     */
    const val yellowColour = "\u001B[93m"
    const val blueColour = "\u001B[94m"
    const val redColour = "\u001B[31m"
    const val greenColour = "\u001B[32m"
    const val resetColour = "\u001B[0m"

    /**
     * Formats a list of decks into a string representation.
     *
     * The function takes a list of decks and uses [joinToString] to concatenate their string representations
     * with a newline separator. Each deck is represented using its [Deck.toString] method.
     *
     * @param notesToFormat The list of decks to be formatted.
     * @return A formatted string containing the string representations of the decks.
     *
     * @see joinToString
     * @see Deck.toString
     */
    @JvmStatic
    fun formatListString(notesToFormat: List<Deck>): String =
        notesToFormat
            .joinToString(separator = "\n") { deck -> "$deck" }

    /**
     * Formats a set of flashcards into a string representation.
     *
     * The function takes a set of flashcards and uses [joinToString] to concatenate their string representations
     * with a newline separator. Each flashcard is represented using its [Flashcard.toString] method, and each line
     * is indented with a tab character.
     *
     * @param itemsToFormat The set of flashcards to be formatted.
     * @return A formatted string containing the indented string representations of the flashcards.
     *
     * @see joinToString
     * @see Flashcard.toString
     */
    @JvmStatic
    fun formatSetString(itemsToFormat: Set<Flashcard>): String =
        itemsToFormat
            .joinToString(separator = "\n") { flashcard -> "\t$flashcard" }

    /**
     * Checks if a given number falls within a specified range (inclusive).
     *
     * The function takes a number to check (`numberToCheck`) and verifies if it falls within the specified
     * inclusive range defined by the minimum (`min`) and maximum (`max`) values.
     *
     * @param numberToCheck The number to be checked for validity within the specified range.
     * @param min The minimum value of the valid range (inclusive).
     * @param max The maximum value of the valid range (inclusive).
     * @return `true` if the number falls within the specified range; otherwise, `false`.
     */
    @JvmStatic
    fun validRange(numberToCheck: Int, min: Int, max: Int): Boolean {
        return numberToCheck in min..max
    }

    /**
     * Reads and validates an integer input from the user within a specified range.
     *
     * The function prompts the user with a specified message and reads an integer input. It then checks if
     * the input falls within the valid range based on the provided attribute. If the input is valid, it is
     * returned; otherwise, an error message is displayed, and the user is prompted again until a valid input is provided.
     *
     * @param attribute The attribute for which the input is being validated (e.g., "theme", "level").
     * @param prompt The message prompt to display to the user.
     * @return The validated integer input within the specified range.
     *
     * @see validRange
     * @see ScannerInput.readNextInt
     */
    @JvmStatic
    fun readValidInput(attribute: String, prompt: String): Int {
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

    /**
     * Reads and validates a non-empty string input from the user with a minimum length requirement.
     *
     * The function prompts the user with a specified message to enter a string of a specified type. It then
     * reads the input and checks if it is non-empty and meets the minimum length requirement. If the input is valid,
     * it is returned; otherwise, the user is prompted again until a valid input is provided.
     *
     * @param type The type of string being entered (e.g., "title", "description").
     * @return The validated non-empty string input with a minimum length requirement.
     *
     * @see ScannerInput.readNextLine
     */
    fun readValidString(type: String): String {
        var title: String
        do {
            title = ScannerInput.readNextLine("Enter a $type: ")
        } while (title.isEmpty() || title.trim().length < 2)

        return title
    }
}
