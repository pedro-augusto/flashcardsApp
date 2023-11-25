package models

data class Flashcard(
    var flashcardId: Int = 0,
    var word: String = "",
    var meaning: String = "",
    var typeOfWord: String = "",
    var hit: String = "Not Attempted",
    var attempts: Int = 0,
    var favourite: Boolean = false
)
