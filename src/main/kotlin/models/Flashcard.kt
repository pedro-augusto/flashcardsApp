package models
import utils.Utilities

data class Flashcard(
    var flashcardId: Int = 0,
    var word: String = "",
    var meaning: String = "",
    var typeOfWord: String = "",
    var hit: String = "Not Attempted",
    var attempts: Int = 0,
    var favourite: Boolean = false
) {

    /**
     * Generates a formatted string representation of the Flashcard.
     *
     * The function creates a string that includes the Flashcard's ID, word, meaning, type of word,
     * hit status, number of attempts, and favorite status. The information is presented in a structured
     * format with color-coded sections for improved readability.
     *
     * @return A formatted string representation of the Flashcard.
     */
    override fun toString(): String {
        val favouriteString = if (favourite) {
            "| FAVOURITE"
        } else {
            ""
        }

        val attemptsString = if (attempts > 0) {
            "| $attempts attemps"
        } else {
            ""
        }

        return """                    ${Utilities.blueColour}--------------------------------------------------------------------------------------------
                        | ID: $flashcardId  | WORD: $word  | MEANING: $meaning  | TYPE: $typeOfWord 
                        | ${hit.uppercase()} $attemptsString$favouriteString
                        --------------------------------------------------------------------------------------------${Utilities.resetColour}
        """.trimIndent()
    }
}
