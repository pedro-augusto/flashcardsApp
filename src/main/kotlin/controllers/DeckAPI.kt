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
        if (decks.isEmpty()) "No decks stored"
        else formatListString(decks)

    fun listDecksWithFlashcards() =
        if (numberOfDecksWithFlashcards() == 0) "No decks with flashcards stored"
        else formatListString(decks.filter { deck -> deck.flashcards.isNotEmpty() })

    fun listEmptyDecks() =
        if (numberOfEmptyDecks() == 0) "No empty decks stored"
        else formatListString(decks.filter { deck -> deck.flashcards.isEmpty() })

    // ----------------------------------------------
    //  COUNTING METHODS FOR NOTE ArrayList
    // ----------------------------------------------
    fun numberOfDecks() = decks.size
    fun numberOfDecksWithFlashcards(): Int = decks.count { deck: Deck -> deck.flashcards.isNotEmpty() }
    fun numberOfEmptyDecks(): Int = decks.count { deck: Deck -> deck.flashcards.isEmpty() }

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
