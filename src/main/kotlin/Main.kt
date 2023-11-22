import controllers.DeckAPI
import models.Deck
import models.Flashcard
import persistence.YAMLSerializer
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess
import kotlin.random.Random

private val deckAPI = DeckAPI(YAMLSerializer(File("decks.yaml")))


fun main() = runMenu()

fun runMenu() {
    do {
        when (val option = mainMenu()) {
            1 -> addDeck()
            2 -> listDecks()
            3 -> updateDeck()
            4 -> deleteDeck()
            5-> searchDecks()
            6 -> addFlashcardToDeck()
            7 -> updateFlashcardsInDeck()
            8 -> deleteFlashcard()
            9 -> play()
            20-> save()
            21-> load()
            0 -> exitApp()
            else -> println("Invalid menu choice: $option")
        }
    } while (true)
}

fun mainMenu() = readNextInt(
        """ 
         > -----------------------------------------------------  
         > |                  NOTE KEEPER APP                  |
         > -----------------------------------------------------  
         > | DECK MENU                                         |
         > |   1) Add a deck                                   |
         > |   2) List decks                                   |
         > |   3) Update a deck                                |
         > |   4) Delete a deck                                |
         > |   5) Search Decks by Title                        |
         > -----------------------------------------------------  
         > | FLASHCARDS MENU                                   | 
         > |   6) Add flashcards to a deck                     |
         > |   7) Update flashcard in a deck                   |
         > |   8) Delete flashcard from a deck                 |
         > ----------------------------------------------------- 
         > |                9) START PLAYING                   |
         > ----------------------------------------------------- 
         > | REPORT MENU FOR DECKS                             | 
         > |   11) .....                                       |
         > |   12) .....                                       |
         > |   13) .....                                       |
         > |   14) .....                                       |
         > -----------------------------------------------------  
         > | REPORT MENU FOR FLASHCARDS                        |                                
         > |   17) .....                                       |
         > |   18) .....                                       |
         > |   19) .....                                       |
         > ----------------------------------------------------- 
         > |   20) Save decks                                   
         > |   21) Load decks                                  |
         > |   0) Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
    )
//------------------------------------
//PLAY MENU
//------------------------------------

fun play(){
    var guessedCorrectly: Int

    val chosenDeck = askUserToChooseDeck()
    if(chosenDeck!= null){
        chosenDeck!!.flashcards.forEach{ flashcard ->
            if(Random.nextBoolean()){
                println("${flashcard.flashcardId}) ${flashcard.word} (${flashcard.typeOfWord})")
                TimeUnit.SECONDS.sleep(10)
                println(flashcard.meaning)
            } else {
                println("${flashcard.flashcardId}) ${flashcard.meaning} (${flashcard.typeOfWord})")
                TimeUnit.SECONDS.sleep(10)
                println(flashcard.word)
            }

            guessedCorrectly = readNextInt("Did you guess it correctly? (1- YES, Any Other Number- NO): ")
            if(guessedCorrectly==1) flashcard.hit = "Hit"
            else flashcard.hit = "Missed"

            flashcard.attempts++
        }

        chosenDeck.lastDateAccessed = LocalDate.now()


        var markAsFavourite = readNextInt("Would you like to mark one or more words as favourite? (1- YES, Any Other Number- NO): ")
        var continueMarking: Int
        var favouriteId: Int
        if(markAsFavourite==1){
            do{
                favouriteId = readNextInt("Enter the id of the flashcard you want to mark as favourite: ")
                if(!chosenDeck.findFlashcard(favouriteId)!!.favourite){
                    chosenDeck.findFlashcard(favouriteId)!!.favourite = true
                    print("Flashcard successfully marked as favourite.")
                } else {
                    println("This flashcard is already marked as favourite.")
                }

                continueMarking = readNextInt("Would you like to continue marking flashcards as favourites? (1- YES, Any Other Number- NO): ")
            } while(continueMarking==1)
        }
    }
}

//------------------------------------
//DECK MENU
//------------------------------------
fun addDeck() {
    val deckTitle = readNextLine("Enter a title for the deck: ")
    val deckTheme = chooseTheme()
    val deckLevel = chooseLevel()

    val isAdded = deckAPI.addDeck(Deck(title = deckTitle, theme = deckTheme, level = deckLevel))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun chooseTheme(): String{
    val chosenTheme = readNextInt("Enter a theme (1-Everyday, 2-Academic, 3-Professional, 4- Cultural and Idiomatic, 5-Emotions and Feelings): ")
    var deckTheme: String = ""
    when(chosenTheme){
        1-> deckTheme = "Everyday"
        2-> deckTheme = "Academic"
        3-> deckTheme = "Professional"
        4-> deckTheme = "Cultural and Idiomatic"
        5-> deckTheme = "Emotions and Feelings"
    }
    return deckTheme
}

fun chooseLevel(): String {
    val chosenLevel = readNextInt("Enter a level (1-Beginner, 2-Intermediate, 3-Advanced, 4- Proficient): ")
    var deckLevel: String = ""
    when(chosenLevel){
        1-> deckLevel = "Beginner"
        2-> deckLevel = "Intermediate"
        3-> deckLevel = "Advanced"
        4-> deckLevel = "Proficient"
    }
    return deckLevel
}

fun listDecks() {
    if (deckAPI.numberOfDecks() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------------
                  > |   1) View ALL decks                |
                  > |   2) View DECKS WITH FLASHCARDS    |
                  > |   3) View EMPTY decks              |
                  > --------------------------------------
         > ==>> """.trimMargin(">")
        )

        when (option) {
            1 -> listAllDecks()
            2 -> listDecksWithFlashcards()
            3 -> listEmptyDecks()
            else -> println("Invalid option entered: $option")
        }
    } else {
        println("Option Invalid - No decks stored")
    }
}

fun listAllDecks() = println(deckAPI.listAllDecks())
fun listDecksWithFlashcards() = println(deckAPI.listDecksWithFlashcards())
fun listEmptyDecks() = println(deckAPI.listEmptyDecks())

fun updateDeck() {
    listDecks()
    if (deckAPI.numberOfDecks() > 0) {
        // only ask the user to choose the note if notes exist
        val id = readNextInt("Enter the id of the deck to update: ")
        if (deckAPI.findDeck(id) != null) {
            val deckTitle = readNextLine("Enter a title for the deck: ")
            val deckTheme = chooseTheme()
            val deckLevel = chooseLevel()

            // pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (deckAPI.updateDeck(id, Deck(deckId=0, title=deckTitle, theme=deckTheme, level = deckLevel))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no decks for this id number")
        }
    }
}

fun deleteDeck() {
    listDecks()
    if (deckAPI.numberOfDecks() > 0) {
        // only ask the user to choose the note to delete if notes exist
        val id = readNextInt("Enter the id of the deck to delete: ")
        // pass the index of the note to NoteAPI for deleting and check for success.
        val deckToDelete = deckAPI.deleteDeck(id)
        if (deckToDelete) {
            println("Delete Successful!")
        } else {
            println("Delete NOT Successful")
        }
    }
}

//-------------------------------------------
//FLASHCARD MENU
//-------------------------------------------

fun createFlashcard(): Flashcard{
    val word = readNextLine("Enter the word/expression: ")
    val meaning = readNextLine("Enter its meaning: ")
    val typeNo = readNextInt("Enter the word type (1-Noun, 2-Verb, 3-Adjective, 4-Adverb, 5-Expression): ")

    var type: String = ""
    when(typeNo){
        1-> type = "Noun"
        2-> type = "Verb"
        3-> type = "Adjective"
        4-> type = "Adverb"
        5-> type = "Expression"
    }

    return Flashcard(word = word, meaning = meaning, typeOfWord = type)
}

private fun addFlashcardToDeck() {
    val deck: Deck? = askUserToChooseDeck()
    var continueAdding: Int
    if (deck != null) {
        do{
            if (deck.addFlashcard(createFlashcard()))
                println("Add Successful!")
            else println("Add NOT Successful")
            continueAdding = readNextInt("Would you like to continue adding flashcards to this deck? (1- Yes | Any Other Number- No): ")
        } while (continueAdding == 1)
    }
}

fun updateFlashcardsInDeck() {
    val deck: Deck? = askUserToChooseDeck()
    if (deck != null) {
        val flashcard: Flashcard? = askUserToChooseFlashcard(deck)
        if (flashcard != null) {
            if (deck.updateFlashcard(flashcard.flashcardId, createFlashcard())) {
                println("Flashcard updated")
            } else {
                println("Flashcard NOT updated")
            }
        }
    }
}

fun deleteFlashcard() {
    val deck: Deck? = askUserToChooseDeck()
    if (deck != null) {
        val flashcard: Flashcard? = askUserToChooseFlashcard(deck)
        if (flashcard != null) {
            val isDeleted = deck.deleteFlashcard(flashcard.flashcardId)
            if (isDeleted) {
                println("Delete Successful!")
            } else {
                println("Delete NOT Successful")
            }
        }
    }
}

//------------------------------------
//NOTE REPORTS MENU
//------------------------------------
fun searchDecks() {
    val searchTitle = readNextLine("Enter the title of the deck to search by: ")
    val searchResults = deckAPI.searchDecksByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        println("No decks found")
    } else {
        println(searchResults)
    }
}

//------------------------------------
//ITEM REPORTS MENU
//------------------------------------



//------------------------------------
//HELPER FUNCTIONS
//------------------------------------

private fun askUserToChooseDeck(): Deck? {
    listDecks()
    if (deckAPI.numberOfDecks() > 0) {
        val deck = deckAPI.findDeck(readNextInt("\nEnter the id of the deck: "))
        if (deck != null) {
                return deck
        }
        else {
            println("Deck id is not valid")
        }
    }
    return null //selected note is not active
}

private fun askUserToChooseFlashcard(deck: Deck): Flashcard? {
    if (deck.numberOfFlashcards() > 0) {
        print(deck.listFlashcards())
        return deck.findFlashcard(readNextInt("\nEnter the id of the flashcard: "))
    }
    else{
        println ("No flashcards for chosen deck")
        return null
    }
}

//------------------------------------
//PERSISTENCE FUNCTIONS
//------------------------------------

/**
 * Saves the notes to a file in the Note Keeper App.
 */
fun save(){
    try{
        deckAPI.store()
    } catch (e: Exception){
        System.err.println("Error writing to file: $e")
    }
}

/**
 * Loads the notes from a file into the Note Keeper App.
 */
fun load(){
    try {
        deckAPI.load()
    } catch (e: Exception){
        System.err.println("Error reading from file: $e")
    }
}

//------------------------------------
// Exit App
//------------------------------------
fun exitApp() {
    println("Exiting...bye")
    exitProcess(0)
}
