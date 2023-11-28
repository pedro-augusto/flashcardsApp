package models

import utils.Utilities
import utils.Utilities.resetColour
import utils.Utilities.yellowColour
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

data class Deck(
    var deckId: Int = 0,
    var title: String = "",
    var theme: String = "",
    var lastDateAccessed: LocalDate? = null,
    var level: String = "",
    var flashcards: MutableSet<Flashcard> = mutableSetOf()
) {
    private var lastFlashcardId = 0
    private fun getFlashcardId() = lastFlashcardId++

    fun addFlashcard(flashcard: Flashcard): Boolean {
        flashcard.flashcardId = getFlashcardId()
        return flashcards.add(flashcard)
    }

    fun numberOfFlashcards(): Int = flashcards.size

    fun numberOfHits(): Int = flashcards.count { flashcard: Flashcard -> flashcard.hit == "Hit" }

    fun numberOfMisses(): Int = flashcards.count { flashcard: Flashcard -> flashcard.hit == "Miss" }
    fun numberOfFavourites(): Int = flashcards.count { flashcard: Flashcard -> flashcard.favourite }
    fun getHits(): Set<Flashcard> = flashcards.filter { flashcard -> flashcard.hit == "Hit" }.toSet()
    fun getMisses(): Set<Flashcard> = flashcards.filter { flashcard -> flashcard.hit == "Misses" }.toSet()

    fun getFavourites(): Set<Flashcard> = flashcards.filter { flashcard -> flashcard.favourite }.toSet()

    fun findFlashcard(id: Int): Flashcard? {
        return flashcards.find { flashcard -> flashcard.flashcardId == id }
    }

    fun deleteFlashcard(id: Int): Boolean {
        return flashcards.removeIf { flashcard -> flashcard.flashcardId == id }
    }

    fun updateFlashcard(id: Int, newFlashcard: Flashcard): Boolean {
        val foundItem = findFlashcard(id)

        // if the object exists, use the details passed in the newItem parameter to
        // update the found object in the Set
        if (foundItem != null) {
            foundItem.word = newFlashcard.word
            foundItem.meaning = newFlashcard.meaning
            foundItem.typeOfWord = newFlashcard.typeOfWord
            foundItem.hit = newFlashcard.hit
            foundItem.attempts = newFlashcard.attempts
            foundItem.favourite = newFlashcard.favourite
            return true
        }

        // if the object was not found, return false, indicating that the update was not successful
        return false
    }

    fun listFlashcards() =
        if (flashcards.isEmpty()) {
            "\tNO FLASHCARDS ADDED"
        } else {
            Utilities.formatSetString(flashcards)
        }

    fun calculateHitsPercentage(): Double? {
        return if (numberOfFlashcards() > 0) {
            val hitsNumber = flashcards.filter { flashcard -> flashcard.hit == "Hit" }.size.toDouble()
            ((hitsNumber / flashcards.size.toDouble()) * 100).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
        } else {
            null
        }
    }

    fun calculateDeckAverageAttemptNo(): Double? {
        return if (flashcards.isNotEmpty()) {
            flashcards.sumOf { flashcard: Flashcard -> flashcard.attempts.toDouble() } / flashcards.size
        } else {
            null
        }
    }

    fun calculateNoOfFavouriteFlashcards(): Int? {
        return if (flashcards.isNotEmpty()) {
            flashcards.count { flashcard: Flashcard -> flashcard.favourite }
        } else {
            null
        }
    }

    override fun toString(): String {
        val lastDateAccessedString = if (lastDateAccessed != null) {
            " LAST ACCESS: ${lastDateAccessed?.format(
                DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
            )}  | HITS: ${numberOfHits()}/${flashcards.size} (${calculateHitsPercentage()}%)  | AVERAGE ATTEMPT NUMBER: ${calculateDeckAverageAttemptNo()!!.toBigDecimal().setScale(2, RoundingMode.HALF_UP)} | FAVOURITES: ${calculateNoOfFavouriteFlashcards()}/${flashcards.size}"
        } else {
            " HAS NOT BEEN PLAYED YET"
        }

        val result: String =
            """            $yellowColour----------------------------------------------------------------------------------------------------------
            | ID: $deckId  | TITLE: $title  | THEME: $theme  | LEVEL: $level  | FLASHCARDS: ${flashcards.size}  
            |$lastDateAccessedString 
            ----------------------------------------------------------------------------------------------------------$resetColour
            """.trimIndent()

        return result
    }
}
