package controllers

import models.Deck
import persistence.Serializer
import utils.Utilities.formatListString
import java.util.ArrayList

class DeckAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType
    private var decks = ArrayList<Deck>()

    // ----------------------------------------------
    //  For Managing the id internally in the program
    // ----------------------------------------------
    private var lastId = 0
    private fun getId() = lastId++

    // ----------------------------------------------
    //  CRUD METHODS FOR DECK ArrayList
    // ----------------------------------------------
    fun addDeck(deck: Deck): Boolean {
        deck.deckId = getId()
        return decks.add(deck)
    }

    fun deleteDeck(id: Int) = decks.removeIf { deck -> deck.deckId == id }

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
    fun listAllDecks() =
        if (decks.isEmpty()) {
            "No decks stored"
        } else {
            formatListString(decks)
        }

    fun listDecksWithFlashcards() =
        if (numberOfDecksWithFlashcards() == 0) {
            "No decks with flashcards stored"
        } else {
            formatListString(decks.filter { deck -> deck.flashcards.isNotEmpty() })
        }

    fun listDecksByTheme(theme: String): String {
        val decksWithThemeChosen = decks.filter { deck: Deck -> deck.theme == theme }
        return if (decksWithThemeChosen.isNotEmpty()) {
            formatListString(decksWithThemeChosen)
        } else {
            "No decks with this theme stored"
        }
    }

    fun listDecksByThemeNotEmpty(theme: String): String {
        val decksWithThemeChosen = decks.filter { deck: Deck -> deck.theme == theme && deck.flashcards.isNotEmpty() }
        return if (decksWithThemeChosen.isNotEmpty()) {
            formatListString(decksWithThemeChosen)
        } else {
            "No decks with this theme stored"
        }
    }

    fun listDecksByLevel(level: String): String {
        val decksWithLevelChosen = decks.filter { deck: Deck -> deck.level == level }
        return if (decksWithLevelChosen.isNotEmpty()) {
            formatListString(decksWithLevelChosen)
        } else {
            "No decks with this level stored"
        }
    }

    fun listDecksByLevelNotEmpty(level: String): String {
        val decksWithLevelChosen = decks.filter { deck: Deck -> deck.level == level && deck.flashcards.isNotEmpty() }
        return if (decksWithLevelChosen.isNotEmpty()) {
            formatListString(decksWithLevelChosen)
        } else {
            "No decks with this level stored"
        }
    }

    fun listDecksByMostRecentlyPlayed(): String {
        return if (numberOfDecksPlayed() > 0) {
            val mostRecentlyPlayedDecks = decks.filter { deck: Deck -> deck.lastDateAccessed != null }.sortedByDescending { deck: Deck -> deck.lastDateAccessed }
            formatListString(mostRecentlyPlayedDecks)
        } else {
            "You have not played with any deck yet."
        }
    }

    fun listDecksByLeastRecentlyPlayed(): String {
        return if (numberOfDecksPlayed() > 0) {
            val leastRecentlyPlayedDecks = decks.filter { deck: Deck -> deck.lastDateAccessed != null }.sortedBy { deck: Deck -> deck.lastDateAccessed }
            formatListString(leastRecentlyPlayedDecks)
        } else {
            "You have not played with any deck yet."
        }
    }

    fun listNeverPlayedDecks(): String {
        val neverPlayedDecks = decks.filter { deck: Deck -> deck.flashcards.isNotEmpty() && deck.lastDateAccessed == null }
        return if (neverPlayedDecks.isNotEmpty()) {
            formatListString(neverPlayedDecks)
        } else {
            "There are no decks with flashcards that haven't already been played"
        }
    }

    fun listDecksByNumberOfHits(): String {
        val decksWithMostHits = decks.filter { deck: Deck -> deck.lastDateAccessed != null }.sortedByDescending { deck: Deck -> deck.numberOfHits() }
        return if (decksWithMostHits.isNotEmpty()) {
            formatListString(decksWithMostHits)
        } else {
            "You have not played with any deck yet."
        }
    }

    fun listDecksByNumberOfMisses(): String {
        val decksWithMostMisses = decks.filter { deck: Deck -> deck.lastDateAccessed != null }.sortedBy { deck: Deck -> deck.numberOfHits() }
        return if (decksWithMostMisses.isNotEmpty()) {
            formatListString(decksWithMostMisses)
        } else {
            "You have not played with any deck yet."
        }
    }

    fun listDecksByHighestAverageAttemptNo(): String {
        val decksHighestAverageAttempts = decks.filter { deck: Deck -> deck.lastDateAccessed != null }.sortedByDescending { deck: Deck -> deck.calculateDeckAverageAttemptNo() }
        return if (decksHighestAverageAttempts.isNotEmpty()) {
            formatListString(decksHighestAverageAttempts)
        } else {
            "You have not played with any deck yet."
        }
    }

    fun listDecksByLowestAverageAttemptNo(): String {
        val decksLowestAverageAttempts = decks.filter { deck: Deck -> deck.lastDateAccessed != null }.sortedBy { deck: Deck -> deck.calculateDeckAverageAttemptNo() }
        return if (decksLowestAverageAttempts.isNotEmpty()) {
            formatListString(decksLowestAverageAttempts)
        } else {
            "You have not played with any deck yet."
        }
    }

    fun listDecksByMostMarkedAsFavourite(): String {
        val decksLowestAverageAttempts = decks.filter { deck: Deck -> deck.lastDateAccessed != null && deck.calculateNoOfFavouriteFlashcards() > 0 }.sortedByDescending { deck: Deck -> deck.calculateNoOfFavouriteFlashcards() }
        return if (decksLowestAverageAttempts.isNotEmpty()) {
            formatListString(decksLowestAverageAttempts)
        } else {
            "You have either not played with any deck or not marked a flashcard as a favourite yet."
        }
    }

    fun listEmptyDecks() =
        if (numberOfEmptyDecks() == 0) {
            "No empty decks stored"
        } else {
            formatListString(decks.filter { deck -> deck.flashcards.isEmpty() })
        }

    // ----------------------------------------------
    //  COUNTING METHODS FOR NOTE ArrayList
    // ----------------------------------------------
    fun numberOfDecks() = decks.size
    fun numberOfDecksWithFlashcards(): Int = decks.count { deck: Deck -> deck.flashcards.isNotEmpty() }
    fun numberOfEmptyDecks(): Int = decks.count { deck: Deck -> deck.flashcards.isEmpty() }
    fun numberOfDecksPlayed(): Int = decks.count { deck: Deck -> deck.lastDateAccessed != null }

    // ----------------------------------------------
    //  SEARCHING METHODS
    // ---------------------------------------------
    fun findDeck(deckId: Int) = decks.find { deck -> deck.deckId == deckId }

    fun searchDecksByTitle(searchString: String) =
        formatListString(
            decks.filter { deck -> deck.title.contains(searchString, ignoreCase = true) }
        )

    /*    fun searchItemByContents(searchString: String): String {
            return if (numberOfNotes() == 0) "No notes stored"
            else {
                var listOfNotes = ""
                for (note in notes) {
                    for (item in note.items) {
                        if (item.itemContents.contains(searchString, ignoreCase = true)) {
                            listOfNotes += "${note.noteId}: ${note.noteTitle} \n\t${item}\n"
                        }
                    }
                }
                if (listOfNotes == "") "No items found for: $searchString"
                else listOfNotes
            }
        }*/

    // ----------------------------------------------
    //  LISTING METHODS FOR ITEMS
    // ----------------------------------------------
    /*
        fun listTodoItems(): String =
             if (numberOfNotes() == 0) "No notes stored"
             else {
                 var listOfTodoItems = ""
                 for (note in notes) {
                     for (item in note.items) {
                         if (!item.isItemComplete) {
                             listOfTodoItems += note.noteTitle + ": " + item.itemContents + "\n"
                         }
                     }
                 }
                 listOfTodoItems
             }
    */

    // ----------------------------------------------
    //  COUNTING METHODS FOR ITEMS
    // ----------------------------------------------
    /*    fun numberOfToDoItems(): Int {
            var numberOfToDoItems = 0
            for (note in notes) {
                for (item in note.items) {
                    if (!item.isItemComplete) {
                        numberOfToDoItems++
                    }
                }
            }
            return numberOfToDoItems
        }*/

    // ----------------------------------------------
    //  PERSISTENCE
    // ----------------------------------------------

    /**
     * Loads decks from the serializer.
     */
    fun load() {
        decks = serializer.read() as ArrayList<Deck>
    }

    /**
     * Stores decks using the serializer.
     */
    fun store() {
        serializer.write(decks)
    }
}
