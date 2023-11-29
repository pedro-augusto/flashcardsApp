package controllers

import models.Deck
import models.Flashcard
import persistence.Serializer
import utils.Utilities.formatListString
import java.util.ArrayList

/**
 * DeckAPI class for managing decks and their operations.
 *
 * The DeckAPI class handles the management of decks, including adding, updating, deleting, and listing decks.
 * It also provides functionalities to generate and play decks, as well as storing and loading deck data using a serializer.
 *
 * @param serializerType The serializer type used for data storage and retrieval.
 */
class DeckAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType
    private var decks = ArrayList<Deck>()

    // ----------------------------------------------
    //  For Managing the id internally in the program
    // ----------------------------------------------
    private var lastId = 0

    /**
     * Generates and returns a unique identifier for a deck.
     *
     * The function generates a unique identifier for a deck by incrementing the internal counter.
     * Each time this function is called, it returns a new, unique identifier for a deck.
     *
     * @return A unique identifier for a deck.
     */
    private fun getId() = lastId++

    // ----------------------------------------------
    //  CRUD METHODS FOR DECK ArrayList
    // ----------------------------------------------

    /**
     * Adds a deck to the list of decks managed by the DeckAPI.
     *
     * The function assigns a unique identifier to the deck and adds it to the list of decks.
     *
     * @param deck The deck to be added.
     * @return `true` if the deck is added successfully, `false` otherwise.
     */
    fun addDeck(deck: Deck): Boolean {
        deck.deckId = getId()
        return decks.add(deck)
    }

    /**
     * Deletes a deck from the list of decks managed by the DeckAPI.
     *
     * The function removes the deck with the specified identifier from the list of decks.
     *
     * @param id The unique identifier of the deck to be deleted.
     * @return `true` if the deck is deleted successfully, `false` otherwise.
     */
    fun deleteDeck(id: Int) = decks.removeIf { deck -> deck.deckId == id }

    /**
     * Updates the details of a deck in the list of decks managed by the DeckAPI.
     *
     * The function finds the deck with the specified identifier and updates its details with the provided deck information.
     *
     * @param id The unique identifier of the deck to be updated.
     * @param deck The deck containing the updated details.
     * @return `true` if the deck is updated successfully, `false` otherwise.
     */
    fun updateDeck(id: Int, deck: Deck?): Boolean {
        // find the note object by the index number
        val foundDeck = findDeck(id)

        // if the note exists, use the note details passed as parameters to update the found note in the ArrayList.
        if ((foundDeck != null) && (deck != null)) {
            foundDeck.title = deck.title
            foundDeck.theme = deck.theme
            foundDeck.level = deck.level
            return true
        }

        // if the note was not found, return false, indicating that the update was not successful
        return false
    }

    // ----------------------------------------------
    //  LISTING METHODS FOR DECK ArrayList
    // ----------------------------------------------

    /**
     * Lists all decks stored in the DeckAPI.
     *
     * @return A formatted string containing the details of all decks, or "No decks stored" if the list is empty.
     */
    fun listAllDecks() =
        if (decks.isEmpty()) {
            "No decks stored"
        } else {
            formatListString(decks)
        }

    /**
     * Lists decks with flashcards stored in the DeckAPI.
     *
     * @return A formatted string containing the details of decks with flashcards, or "No decks with flashcards stored" if none are found.
     */
    fun listDecksWithFlashcards() =
        if (numberOfDecksWithFlashcards() == 0) {
            "No decks with flashcards stored"
        } else {
            formatListString(decks.filter { deck -> deck.flashcards.isNotEmpty() })
        }

    /**
     * Lists decks with a specific theme in the DeckAPI.
     *
     * @param theme The theme to filter decks.
     * @return A formatted string containing the details of decks with the specified theme,
     *         or "No decks with this theme stored" if none are found.
     */
    fun listDecksByTheme(theme: String): String {
        val decksWithThemeChosen = decks.filter { deck: Deck -> deck.theme == theme }
        return if (decksWithThemeChosen.isNotEmpty()) {
            formatListString(decksWithThemeChosen)
        } else {
            "No decks with this theme stored"
        }
    }

    /**
     * Lists decks with a specific theme and non-empty flashcards in the DeckAPI.
     *
     * @param theme The theme to filter decks.
     * @return A formatted string containing the details of decks with the specified theme and non-empty flashcards,
     *         or "No decks with flashcards with this theme stored" if none are found.
     */
    fun listDecksByThemeNotEmpty(theme: String): String {
        val decksWithThemeChosen = decks.filter { deck: Deck -> deck.theme == theme && deck.flashcards.isNotEmpty() }
        return if (decksWithThemeChosen.isNotEmpty()) {
            formatListString(decksWithThemeChosen)
        } else {
            "No decks with with flashcards with this theme stored"
        }
    }

    /**
     * Lists decks with a specific level in the DeckAPI.
     *
     * @param level The level to filter decks.
     * @return A formatted string containing the details of decks with the specified level,
     *         or "No decks with this level stored" if none are found.
     */
    fun listDecksByLevel(level: String): String {
        val decksWithLevelChosen = decks.filter { deck: Deck -> deck.level == level }
        return if (decksWithLevelChosen.isNotEmpty()) {
            formatListString(decksWithLevelChosen)
        } else {
            "No decks with this level stored"
        }
    }

    /**
     * Lists decks with a specific level and non-empty flashcards in the DeckAPI.
     *
     * @param level The level to filter decks.
     * @return A formatted string containing the details of decks with the specified level
     *         and non-empty flashcards, or "No decks with flashcards with this level stored"
     *         if none are found.
     */
    fun listDecksByLevelNotEmpty(level: String): String {
        val decksWithLevelChosen = decks.filter { deck: Deck -> deck.level == level && deck.flashcards.isNotEmpty() }
        return if (decksWithLevelChosen.isNotEmpty()) {
            formatListString(decksWithLevelChosen)
        } else {
            "No decks with flashcards with this level stored"
        }
    }

    /**
     * Lists decks sorted by the most recent play date in the DeckAPI.
     *
     * @return A formatted string containing the details of decks, sorted by the most recent play date,
     *         or "You have not played with any deck yet." if no decks have been played.
     */
    fun listDecksByMostRecentlyPlayed(): String {
        return if (numberOfDecksPlayed() > 0) {
            val mostRecentlyPlayedDecks = decks.filter { deck: Deck -> deck.lastDateAccessed != null }.sortedByDescending { deck: Deck -> deck.lastDateAccessed }
            formatListString(mostRecentlyPlayedDecks)
        } else {
            "You have not played with any deck yet."
        }
    }

    /**
     * Lists decks sorted by the least recent play date in the DeckAPI.
     *
     * @return A formatted string containing the details of decks, sorted by the least recent play date,
     *         or "You have not played with any deck yet." if no decks have been played.
     */
    fun listDecksByLeastRecentlyPlayed(): String {
        return if (numberOfDecksPlayed() > 0) {
            val leastRecentlyPlayedDecks = decks.filter { deck: Deck -> deck.lastDateAccessed != null }.sortedBy { deck: Deck -> deck.lastDateAccessed }
            formatListString(leastRecentlyPlayedDecks)
        } else {
            "You have not played with any deck yet."
        }
    }

    /**
     * Lists decks with flashcards that have never been played in the DeckAPI.
     *
     * @return A formatted string containing the details of decks with flashcards that have never been played,
     *         or "There is either no decks with flashcards that haven't already been played or no decks with flashcards at all"
     *         if no such decks are found.
     */
    fun listNeverPlayedDecks(): String {
        val neverPlayedDecks = decks.filter { deck: Deck -> deck.flashcards.isNotEmpty() && deck.lastDateAccessed == null }
        return if (neverPlayedDecks.isNotEmpty()) {
            formatListString(neverPlayedDecks)
        } else {
            "There is either no decks with flashcards that haven't already been played or no decks with flashcards at all"
        }
    }

    /**
     * Lists decks with flashcards by the number of hits in descending order in the DeckAPI.
     *
     * @return A formatted string containing the details of decks with flashcards,
     *         sorted by the number of hits in descending order,
     *         or "You have not played with any deck yet." if no such decks are found.
     */
    fun listDecksByNumberOfHits(): String {
        val decksWithMostHits = decks.filter { deck: Deck -> deck.lastDateAccessed != null }.sortedByDescending { deck: Deck -> deck.numberOfHits() }
        return if (decksWithMostHits.isNotEmpty()) {
            formatListString(decksWithMostHits)
        } else {
            "You have not played with any deck yet."
        }
    }

    /**
     * Lists decks with flashcards by the number of misses in descending order in the DeckAPI.
     *
     * @return A formatted string containing the details of decks with flashcards,
     *         sorted by the number of hits in ascending order (therefore, misses in descending order),
     *         or "You have not played with any deck yet." if no such decks are found.
     */
    fun listDecksByNumberOfMisses(): String {
        val decksWithMostMisses = decks.filter { deck: Deck -> deck.lastDateAccessed != null }.sortedBy { deck: Deck -> deck.numberOfHits() }
        return if (decksWithMostMisses.isNotEmpty()) {
            formatListString(decksWithMostMisses)
        } else {
            "You have not played with any deck yet."
        }
    }

    /**
     * Lists decks with flashcards by the highest average attempt number in descending order in the DeckAPI.
     *
     * @return A formatted string containing the details of decks with flashcards,
     *         sorted by the highest average attempt number in descending order,
     *         or "You have not played with any deck yet." if no such decks are found.
     */
    fun listDecksByHighestAverageAttemptNo(): String {
        val decksHighestAverageAttempts = decks.filter { deck: Deck -> deck.lastDateAccessed != null }.sortedByDescending { deck: Deck -> deck.calculateDeckAverageAttemptNo() }
        return if (decksHighestAverageAttempts.isNotEmpty()) {
            formatListString(decksHighestAverageAttempts)
        } else {
            "You have not played with any deck yet."
        }
    }

    /**
     * Lists decks with flashcards by the lowest average attempt number in ascending order in the DeckAPI.
     *
     * @return A formatted string containing the details of decks with flashcards,
     *         sorted by the lowest average attempt number in ascending order,
     *         or "You have not played with any deck yet." if no such decks are found.
     */
    fun listDecksByLowestAverageAttemptNo(): String {
        val decksLowestAverageAttempts = decks.filter { deck: Deck -> deck.lastDateAccessed != null }.sortedBy { deck: Deck -> deck.calculateDeckAverageAttemptNo() }
        return if (decksLowestAverageAttempts.isNotEmpty()) {
            formatListString(decksLowestAverageAttempts)
        } else {
            "You have not played with any deck yet."
        }
    }

    /**
     * Lists decks with flashcards by the most marked as favourite flashcards in descending order in the DeckAPI.
     *
     * @return A formatted string containing the details of decks with flashcards,
     *         sorted by the most marked as favourite flashcards in descending order,
     *         or "You have either not played with any deck or not marked a flashcard as a favourite yet."
     *         if no such decks are found.
     */
    fun listDecksByMostMarkedAsFavourite(): String {
        val decksLowestAverageAttempts = decks.filter { deck: Deck -> deck.lastDateAccessed != null && deck.calculateNoOfFavouriteFlashcards()!! > 0 }.sortedByDescending { deck: Deck -> deck.calculateNoOfFavouriteFlashcards() }
        return if (decksLowestAverageAttempts.isNotEmpty()) {
            formatListString(decksLowestAverageAttempts)
        } else {
            "You have either not played with any deck or not marked a flashcard as a favourite yet."
        }
    }

    /**
     * Lists empty decks (decks without flashcards) in the DeckAPI.
     *
     * @return A formatted string containing the details of empty decks,
     *         or "No empty decks stored" if no empty decks are found.
     */
    fun listEmptyDecks() =
        if (numberOfEmptyDecks() == 0) {
            "No empty decks stored"
        } else {
            formatListString(decks.filter { deck -> deck.flashcards.isEmpty() })
        }

    // ----------------------------------------------
    //  COUNTING METHODS FOR NOTE ArrayList
    // ----------------------------------------------

    /**
     * Returns the total number of decks in the DeckAPI.
     *
     * @return The number of decks stored in the DeckAPI.
     */
    fun numberOfDecks() = decks.size

    /**
     * Returns the number of decks in the DeckAPI that have at least one flashcard.
     *
     * @return The number of decks with flashcards stored in the DeckAPI.
     */
    fun numberOfDecksWithFlashcards(): Int = decks.count { deck: Deck -> deck.flashcards.isNotEmpty() }

    /**
     * Returns the number of empty decks in the DeckAPI (decks with no flashcards).
     *
     * @return The number of decks without any flashcards stored in the DeckAPI.
     */
    fun numberOfEmptyDecks(): Int = decks.count { deck: Deck -> deck.flashcards.isEmpty() }

    /**
     * Returns the number of decks that have been played (accessed) at least once in the DeckAPI.
     *
     * @return The number of decks that have been accessed at least once in the DeckAPI.
     */
    fun numberOfDecksPlayed(): Int = decks.count { deck: Deck -> deck.lastDateAccessed != null }

    /**
     * Returns the number of decks in the DeckAPI that have the specified theme.
     *
     * @param theme The theme to filter the decks by.
     * @return The number of decks with the specified theme in the DeckAPI.
     */
    fun numberOfDecksByTheme(theme: String): Int = decks.count { deck: Deck -> deck.theme == theme }

    /**
     * Returns the number of decks in the DeckAPI with the specified theme that are not empty (contain flashcards).
     *
     * @param theme The theme to filter the decks by.
     * @return The number of non-empty decks with the specified theme in the DeckAPI.
     */
    fun numberOfDecksByThemeNotEmpty(theme: String): Int = decks.count { deck: Deck -> deck.theme == theme && deck.flashcards.isNotEmpty() }

    /**
     * Returns the number of decks in the DeckAPI with the specified level that are not empty (contain flashcards).
     *
     * @param level The level to filter the decks by.
     * @return The number of non-empty decks with the specified level in the DeckAPI.
     */
    fun numberOfDecksByLevelNotEmpty(level: String): Int = decks.count { deck: Deck -> deck.level == level && deck.flashcards.isNotEmpty() }

    /**
     * Returns the number of decks in the DeckAPI with the specified level.
     *
     * @param level The level to filter the decks by.
     * @return The number of decks with the specified level in the DeckAPI.
     */
    fun numberOfDecksByLevel(level: String): Int = decks.count { deck: Deck -> deck.level == level }

    /**
     * Returns the number of decks in the DeckAPI that have flashcards but have never been played.
     *
     * @return The number of decks with flashcards that have never been played in the DeckAPI.
     */
    fun numberOfDecksNeverPlayed(): Int = decks.count { deck: Deck -> deck.flashcards.isNotEmpty() && deck.lastDateAccessed == null }

    /**
     * Calculates and returns the overall number of "Misses" across all decks in the DeckAPI.
     *
     * @return The total number of "Misses" recorded across all decks in the DeckAPI.
     */
    fun calculateOverallNumberOfMisses(): Int = decks.sumOf { deck: Deck -> deck.numberOfMisses() }

    /**
     * Calculates and returns the overall number of "Hits" across all decks in the DeckAPI.
     *
     * @return The total number of "Hits" recorded across all decks in the DeckAPI.
     */
    fun calculateOverallNumberOfHits(): Int = decks.sumOf { deck: Deck -> deck.numberOfHits() }

    /**
     * Calculates and returns the overall number of flashcards across all decks in the DeckAPI.
     *
     * @return The total number of flashcards recorded across all decks in the DeckAPI.
     */
    fun calculateOverallNumberOfFlashcards(): Int = decks.sumOf { deck: Deck -> deck.numberOfFlashcards() }

    /**
     * Calculates and returns the overall number of flashcards marked as favorites across all decks in the DeckAPI.
     *
     * @return The total number of flashcards marked as favorites recorded across all decks in the DeckAPI.
     */
    fun calculateOverallNumberOfFavourites(): Int = decks.sumOf { deck: Deck -> deck.numberOfFavourites() }

    // ----------------------------------------------
    //  SEARCHING METHODS
    // ---------------------------------------------

    /**
     * Finds and returns the deck with the specified [deckId] in the DeckAPI.
     *
     * @param deckId The unique identifier of the deck to be found.
     * @return The deck with the specified [deckId], or `null` if no deck is found with the given identifier.
     */
    fun findDeck(deckId: Int) = decks.find { deck -> deck.deckId == deckId }

    /**
     * Searches for decks whose titles contain the specified [searchString].
     *
     * @param searchString The string to search for in deck titles.
     * @return A formatted string listing the decks with titles containing [searchString], or a message indicating no matches.
     */
    fun searchDecksByTitle(searchString: String): String {
        val foundDecks = decks.filter { deck -> deck.title.contains(searchString, ignoreCase = true) }
        return if (foundDecks.isNotEmpty()) {
            formatListString(foundDecks)
        } else {
            "No decks with the title $searchString were found."
        }
    }

    // ----------------------------------------------
    //  GENERATE
    // ----------------------------------------------

    /**
     * Generates a set of flashcards based on the specified [option] and the desired [numberOfFlashcardsChosen].
     *
     * @param option The option to determine the flashcards to include in the set ("Miss", "Hit", "Random", or "Favourite").
     * @param numberOfFlashcardsChosen The desired number of flashcards in the generated set.
     * @return A mutable set of flashcards based on the specified option and number, or null if the generation fails.
     */
    fun generateSetOfFlashcard(option: String, numberOfFlashcardsChosen: Int): MutableSet<Flashcard>? {
        val result: MutableSet<Flashcard> = mutableSetOf()
        var all: MutableSet<Flashcard> = mutableSetOf()
        var randomIndex: Int

        when (option) {
            "Miss" -> all = decks.flatMap { deck: Deck -> deck.getMisses() }.toMutableSet()
            "Hit" -> all = decks.flatMap { deck: Deck -> deck.getHits() }.toMutableSet()
            "Random" -> all = decks.flatMap { deck: Deck -> deck.flashcards }.toMutableSet()
            "Favourite" -> all = decks.flatMap { deck: Deck -> deck.getFavourites() }.toMutableSet()
        }

        if (all.isNotEmpty()) {
            do {
                randomIndex = (0 until all.size).random()
                result.add(all.elementAt(randomIndex))
            } while (result.distinct().size != numberOfFlashcardsChosen)
            return result.distinct().toMutableSet()
        } else {
            return null
        }
    }

    // ----------------------------------------------
    //  PERSISTENCE
    // ----------------------------------------------

    /**
     * Loads deck data from the serializer, replacing the current deck list with the loaded data.
     */
    fun load() {
        decks = serializer.read() as ArrayList<Deck>
    }

    /**
     * Stores the current deck data using the serializer.
     */
    fun store() {
        serializer.write(decks)
    }
}
