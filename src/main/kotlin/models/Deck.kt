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

    /**
     * Generates a unique identifier for a Flashcard.
     *
     * The function returns a unique identifier for a Flashcard by incrementing the last assigned Flashcard ID.
     * This ensures that each Flashcard created gets a distinct identifier.
     *
     * @return A unique identifier for a Flashcard.
     */
    private var lastFlashcardId = 0
    private fun getFlashcardId() = lastFlashcardId++

    /**
     * Adds a new Flashcard to the deck.
     *
     * The function assigns a unique identifier to the Flashcard using [getFlashcardId] and adds it to the deck.
     *
     * @param flashcard The Flashcard to be added to the deck.
     * @return `true` if the Flashcard is successfully added; `false` otherwise.
     *
     * @see getFlashcardId
     */
    fun addFlashcard(flashcard: Flashcard): Boolean {
        flashcard.flashcardId = getFlashcardId()
        return flashcards.add(flashcard)
    }

    /**
     * Returns the number of flashcards in the deck.
     *
     * The function calculates and returns the total count of flashcards currently present in the deck.
     *
     * @return The number of flashcards in the deck.
     */
    fun numberOfFlashcards(): Int = flashcards.size

    /**
     * Returns the number of flashcards marked as "Hit" in the deck.
     *
     * The function calculates and returns the count of flashcards in the deck that are marked as "Hit".
     *
     * @return The number of flashcards marked as "Hit" in the deck.
     */
    fun numberOfHits(): Int = flashcards.count { flashcard: Flashcard -> flashcard.hit == "Hit" }

    /**
     * Returns the number of flashcards marked as "Miss" in the deck.
     *
     * The function calculates and returns the count of flashcards in the deck that are marked as "Miss".
     *
     * @return The number of flashcards marked as "Miss" in the deck.
     */
    fun numberOfMisses(): Int = flashcards.count { flashcard: Flashcard -> flashcard.hit == "Miss" }

    /**
     * Returns the number of flashcards marked as favourites in the deck.
     *
     * The function calculates and returns the count of flashcards in the deck that are marked as favourites.
     *
     * @return The number of flashcards marked as favourites in the deck.
     */
    fun numberOfFavourites(): Int = flashcards.count { flashcard: Flashcard -> flashcard.favourite }

    /**
     * Returns a set of flashcards marked as "Hit" in the deck.
     *
     * The function filters the flashcards in the deck and returns a set containing those marked as "Hit".
     *
     * @return A set of flashcards marked as "Hit" in the deck.
     */
    fun getHits(): Set<Flashcard> = flashcards.filter { flashcard -> flashcard.hit == "Hit" }.toSet()

    /**
     * Returns a set of flashcards marked as "Miss" in the deck.
     *
     * The function filters the flashcards in the deck and returns a set containing those marked as "Miss".
     *
     * @return A set of flashcards marked as "Miss" in the deck.
     */
    fun getMisses(): Set<Flashcard> = flashcards.filter { flashcard -> flashcard.hit == "Miss" }.toSet()

    /**
     * Returns a set of flashcards marked as favourites in the deck.
     *
     * The function filters the flashcards in the deck and returns a set containing those marked as favourites.
     *
     * @return A set of flashcards marked as favourites in the deck.
     */
    fun getFavourites(): Set<Flashcard> = flashcards.filter { flashcard -> flashcard.favourite }.toSet()

    /**
     * Finds a flashcard in the deck based on its unique identifier.
     *
     * The function searches for a flashcard with the specified identifier in the deck and returns it if found,
     * otherwise, it returns null.
     *
     * @param id The unique identifier of the flashcard to find.
     * @return The flashcard with the specified identifier, or null if not found.
     */
    fun findFlashcard(id: Int): Flashcard? = flashcards.find { flashcard -> flashcard.flashcardId == id }

    /**
     * Deletes a flashcard from the deck based on its unique identifier.
     *
     * The function removes the flashcard with the specified identifier from the deck.
     *
     * @param id The unique identifier of the flashcard to delete.
     * @return `true` if the flashcard is successfully deleted; `false` if the flashcard with the given identifier was not found.
     */
    fun deleteFlashcard(id: Int): Boolean {
        return flashcards.removeIf { flashcard -> flashcard.flashcardId == id }
    }

    /**
     * Updates a flashcard in the deck based on its unique identifier.
     *
     * The function searches for a flashcard with the specified identifier in the deck,
     * and if found, it updates the flashcard with the details provided in the `newFlashcard` parameter.
     *
     * @param id The unique identifier of the flashcard to update.
     * @param newFlashcard The new details to update the flashcard with.
     * @return `true` if the flashcard is successfully updated; `false` if the flashcard with the given identifier was not found.
     */
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

    /**
     * Generates a formatted string representation of the flashcards in the deck.
     *
     * If the deck contains flashcards, the function returns a formatted string representing each flashcard,
     * using the `Utilities.formatSetString` function. If the deck is empty, it returns a message indicating
     * that no flashcards have been added.
     *
     * @return A formatted string representation of the flashcards in the deck.
     */
    fun listFlashcards() =
        if (flashcards.isEmpty()) {
            "\tNO FLASHCARDS ADDED"
        } else {
            Utilities.formatSetString(flashcards)
        }

    /**
     * Calculates the percentage of flashcards marked as "Hit" in the deck.
     *
     * The function calculates the percentage of flashcards marked as "Hit" relative to the total number of flashcards
     * in the deck. If the deck is empty, it returns null.
     *
     * @return The percentage of "Hit" flashcards in the deck, rounded to two decimal places, or null if the deck is empty.
     */
    fun calculateHitsPercentage(): Double? {
        return if (numberOfFlashcards() > 0) {
            val hitsNumber = flashcards.filter { flashcard -> flashcard.hit == "Hit" }.size.toDouble()
            ((hitsNumber / flashcards.size.toDouble()) * 100).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
        } else {
            null
        }
    }

    /**
     * Calculates the average number of attempts per flashcard in the deck.
     *
     * The function calculates the average number of attempts per flashcard in the deck.
     * If the deck is empty, it returns null.
     *
     * @return The average number of attempts per flashcard in the deck, or null if the deck is empty.
     */
    fun calculateDeckAverageAttemptNo(): Double? {
        return if (flashcards.isNotEmpty()) {
            flashcards.sumOf { flashcard: Flashcard -> flashcard.attempts.toDouble() } / flashcards.size
        } else {
            null
        }
    }

    /**
     * Calculates the number of flashcards marked as "favourite" in the deck.
     *
     * The function counts the number of flashcards marked as "favourite" in the deck.
     * If the deck is empty, it returns null.
     *
     * @return The number of flashcards marked as "favourite" in the deck, or null if the deck is empty.
     */
    fun calculateNoOfFavouriteFlashcards(): Int? {
        return if (flashcards.isNotEmpty()) {
            flashcards.count { flashcard: Flashcard -> flashcard.favourite }
        } else {
            null
        }
    }

    /**
     * Returns a formatted string representation of the deck, including details such as title, theme, level,
     * the number of flashcards, last date accessed, hits, hits percentage, average attempt number, and favorites.
     *
     * The function constructs a string with various details about the deck. If the deck has been played before,
     * it includes information about the last date accessed, hits, hits percentage, average attempt number,
     * and the number of favorite flashcards. If the deck has not been played yet, it indicates that in the result.
     *
     * @return A formatted string representation of the deck.
     */
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
