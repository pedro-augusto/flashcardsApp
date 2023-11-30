import controllers.DeckAPI
import models.Deck
import models.Flashcard
import mu.KotlinLogging
import persistence.YAMLSerializer
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import utils.Utilities.greenColour
import utils.Utilities.readValidInput
import utils.Utilities.readValidString
import utils.Utilities.redColour
import utils.Utilities.resetColour
import utils.Utilities.yellowColour
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import kotlin.system.exitProcess
private val deckAPI = DeckAPI(YAMLSerializer(File("decks.yaml")))
public val logger = KotlinLogging.logger { }

/**
 * The entry point of the program.
 * Invokes the [runMenu] function to start the main menu and execute the program logic.
 *
 * @see runMenu
 */
fun main() {
    logger.info { "Application started" }
    runMenu()
}

/**
 * Executes the main menu loop, allowing users to interact with various features of the flashcard application.
 * The menu options include:
 *
 *
 * 1. Add a new deck
 * 2. List all decks
 * 3. Update an existing deck
 * 4. Delete a deck
 * 5. Search decks
 * 6. Add a flashcard to a deck
 * 7. Update flashcards in a deck
 * 8. Delete a flashcard
 * 9. Play with flashcards
 * 10. Save the current state
 * 11. Load a saved state
 * 0. Exit the application
 *
 * The function uses a do-while loop to repeatedly display the main menu, process user input, and execute the chosen action.
 * Invalid menu choices result in an error message being printed.
 */
fun runMenu() {
    do when (val option = mainMenu()) {
        1 -> addDeck()
        2 -> println(listDecks("all"))
        3 -> updateDeck()
        4 -> deleteDeck()
        5 -> searchDecks()
        6 -> addFlashcardToDeck()
        7 -> updateFlashcardsInDeck()
        8 -> deleteFlashcard()
        9 -> play()
        10 -> save()
        11 -> load()
        0 -> exitApp()
        else -> println("Invalid menu choice: $option")
    }
    while (true)
}

/**
 * Displays the main menu of the flashcard memorization app and reads the user's menu choice.
 *
 * The menu includes options for managing decks and flashcards, as well as starting the play mode.
 * Users can perform the following actions:
 *
 * DECK MENU:
 * 1. Add a deck
 * 2. List decks
 * 3. Update a deck
 * 4. Delete a deck
 * 5. Search decks by title
 *
 * FLASHCARDS MENU:
 * 6. Add flashcards to a deck
 * 7. Update a flashcard in a deck
 * 8. Delete a flashcard from a deck
 *
 * PLAY MODE:
 * 9. Start playing with flashcards
 *
 * OTHER OPTIONS:
 * 10. Save decks
 * 11. Load decks
 * 0. Exit the application
 *
 * The function uses the `readNextInt` function to read and return the user's menu choice.
 *
 * @return The user's menu choice as an integer.
 */
fun mainMenu() = readNextInt(
    """ 
         > -----------------------------------------------------  
         > |           FLASHCARD MEMORISATION APP              |
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
         > |   10) Save decks                                  |     
         > |   11) Load decks                                  |
         > |   0) Exit                                         |
         > -----------------------------------------------------  
         > ==>> """.trimMargin(">")
)

// ------------------------------------
// PLAY
// ------------------------------------

/**
 * Initiates the flashcard play mode, allowing the user to test their knowledge of flashcards.
 * The function guides the user through the play mode, presenting flashcards and recording their responses.
 * After completing the play session, it provides feedback on the user's performance and optionally allows
 * marking flashcards as favorites or saving a generated deck.
 *
 * The play mode includes the following steps:
 * 1. User chooses to list existing decks or generate a new one.
 * 2. If listing, the user selects a deck with non-empty flashcards.
 * 3. If generating, the user chooses options for generating a new deck.
 * 4. Instructions are provided for the play mode.
 * 5. Each flashcard is presented, and the user is given time to think.
 * 6. User indicates if they guessed the flashcard correctly.
 * 7. Flashcards are marked with hit/miss and the percentage of hits is displayed.
 * 8. A function to ask the user whether they want to mark a flashcard as favourite or not is invoked.
 * 9. If a deck is generated, the user can choose to save it.
 *
 * @see readNextInt
 * @see askUserToChooseDeck
 * @see generateDeck
 * @see printFlashcardPlayMode
 * @see Deck.calculateHitsPercentage
 * @see Deck.findFlashcard
 * @see Deck.addFlashcard
 * @see deckAPI.addDeck
 * @see TimeUnit.SECONDS.sleep
 */
fun play() {
    logger.info { "Play function invoked" }
    var guessedCorrectly: Int
    var chosenDeck: Deck?
    val addGeneratedDeck: Int

    if (deckAPI.numberOfDecksWithFlashcards() > 0) {
        val questionGenerate = readNextInt("Would you like to list decks or generate a one? (1- LIST | 2- GENERATE): ")
        if (questionGenerate == 1) {
            do {
                println("Please select an option:")
                chosenDeck = askUserToChooseDeck("excludeEmpty")
            } while (chosenDeck!!.flashcards.isEmpty())
        } else {
            chosenDeck = generateDeck(askUserToChooseGenerateOption())
            if (chosenDeck.flashcards.isEmpty()) {
                return
            }
        }

        logger.info { "Chosen deck: $chosenDeck" }

        println("")
        println("Instructions: Either the WORD side of the card will be shown and you have to try to remember its meaning or the MEANING side of the word will be shown and you have to remember the respective WORD.")
        println("The system will ask you if you guessed it correctly or not. There is no need to input anything unless asked.")

        chosenDeck.flashcards.forEach { flashcard ->

            println("")

            if (Random.nextBoolean()) {
                printFlashcardPlayMode("meaning", flashcard, false)
                TimeUnit.SECONDS.sleep(5)
                printFlashcardPlayMode("meaning", flashcard, true)
            } else {
                printFlashcardPlayMode("word", flashcard, false)
                TimeUnit.SECONDS.sleep(5)
                printFlashcardPlayMode("word", flashcard, true)
            }

            guessedCorrectly = readNextInt("Did you guess it correctly? (1- YES | ANY OTHER NUMBER- NO): ")
            if (guessedCorrectly == 1) {
                flashcard.hit = "Hit"
            } else {
                flashcard.hit = "Missed"
            }

            flashcard.attempts++
        }
        chosenDeck.lastDateAccessed = LocalDate.now()

        println("")
        println("Your percentage of hits is: $greenColour ${chosenDeck.calculateHitsPercentage()}% $resetColour")
        logger.info { "Play function completed. Percentage of hits: ${chosenDeck.calculateHitsPercentage()}%" }

        promptMarkFlashcardAsFavourite(chosenDeck)

        if (questionGenerate == 2) {
            addGeneratedDeck = readNextInt("Would you like to save this generated deck? (1- YES | ANY OTHER NUMBER- 2): ")
            if (addGeneratedDeck == 1) {
                deckAPI.addDeck(chosenDeck)
                logger.info { "Generated deck saved" }
            }
        }
    } else {
        logger.info { "Play function not concluded - user tried to play with no valid decks in the system" }
        println("There are no decks with flashcards in the system yet. Create decks or populate an existing empty one with flashcards to start playing.")
    }
}

/**
 * Prompts the user to mark one or more flashcards as favorites within the given deck.
 *
 * @param chosenDeck The deck in which the user can mark flashcards as favorites.
 */
fun promptMarkFlashcardAsFavourite(chosenDeck: Deck) {
    val markAsFavourite =
        readNextInt("Would you like to mark one or more words as favourite? (1- YES | Any Other Number- NO): ")

    if (markAsFavourite == 1) {
        var continueMarking: Int
        var favouriteId: Int

        do {
            favouriteId = readNextInt("Enter the id of the flashcard you want to mark as favourite: ")
            if (chosenDeck.findFlashcard(favouriteId) != null && !chosenDeck.findFlashcard(favouriteId)!!.favourite) {
                chosenDeck.findFlashcard(favouriteId)!!.favourite = true
                println("Flashcard successfully marked as favourite.")
                logger.info { "Flashcard $favouriteId in deck ${chosenDeck.deckId} marked as favourite" }
            } else {
                println("This flashcard is either already marked as favourite or it doesn't exist in this deck.")
            }

            continueMarking =
                readNextInt("Would you like to continue marking flashcards as favourites? (1- YES | Any Other Number- NO): ")
        } while (continueMarking == 1)
    }
}

/**
 * Prints the representation of a flashcard for the flashcard play mode.
 * Depending on the user's role (guessing the meaning or the word), certain information is visible or hidden.
 * The function also allows displaying the correct result, highlighting the correct guess in green.
 *
 * @param guess The role of the user in the play mode ("meaning" or "word").
 * @param flashcard The flashcard to be displayed.
 * @param result Indicates whether the correct result should be displayed (true if correct, false otherwise).
 *
 * @see Flashcard.flashcardId
 * @see Flashcard.word
 * @see Flashcard.meaning
 * @see Flashcard.typeOfWord
 */
fun printFlashcardPlayMode(guess: String, flashcard: Flashcard, result: Boolean) {
    var word: String
    var meaning: String

    if (guess == "meaning") { // user has to guess meaning
        word = flashcard.word // word is visible
        meaning = "$redColour???$yellowColour" // meaning is not visible
        if (result) { // if result is to be displayed
            meaning = "$greenColour${flashcard.meaning}$yellowColour" // meaning is visible and green
        }
    } else { // user has to guess word
        meaning = flashcard.meaning // meaning visible
        word = "$redColour???$yellowColour" // word not visible
        if (result) { // if result is to be displayed
            word = "$greenColour${flashcard.word}$yellowColour" // word is visible and green
        }
    }

    println(
        """
        $yellowColour-----------------------------------
        | ID: ${flashcard.flashcardId}  | 
        |---------
        |   WORD: $word                    
        |   TYPE: (${flashcard.typeOfWord})
        |----------------------------------
        |   MEANING: $meaning
        -----------------------------------$resetColour
        """.trimIndent()
    )
}

// ------------------------------------
// DECK MENU
// ------------------------------------

/**
 * Adds a new deck to the flashcard application based on user input for title, theme, and level.
 * The function prompts the user to enter a title, choose a theme, and select a level for the new deck.
 * It then creates a new deck using the provided information and adds it to the deckAPI.
 *
 * @see readValidString
 * @see askUserToChooseTheme
 * @see askUserToChooseLevel
 * @see Deck
 * @see deckAPI.addDeck
 */
fun addDeck() {
    logger.info { "Add deck function invoked" }
    val deckTitle = readValidString("Title")
    val deckTheme = askUserToChooseTheme()
    val deckLevel = askUserToChooseLevel()

    val isAdded = deckAPI.addDeck(Deck(title = deckTitle, theme = deckTheme, level = deckLevel))

    if (isAdded) {
        println("Added Successfully")
        logger.info { "Deck successfully added" }
    } else {
        println("Add Operation Failed")
        logger.info { "Add Operation Failed" }
    }
}

/**
 * Lists decks based on specified criteria and user input.
 *
 * @param page Determines whether to include or exclude empty decks ("all" or "excludeEmpty").
 * @return A formatted string representing the list of decks based on the chosen criteria.
 *
 * The function presents various listing options to the user, such as all decks, decks with flashcards,
 * decks by theme, decks by level, most/least recently played decks, decks never played, decks by hits/misses,
 * and decks by average attempts or marked as favorite.
 *
 * The user is prompted to choose an option, and the function returns a formatted string representing
 * the list of decks based on the selected criteria.
 *
 * @see readNextInt
 * @see askUserToChooseTheme
 * @see askUserToChooseLevel
 * @see DeckAPI.listAllDecks
 * @see DeckAPI.listEmptyDecks
 * @see DeckAPI.listDecksWithFlashcards
 * @see DeckAPI.listDecksByTheme
 * @see DeckAPI.listDecksByLevel
 * @see DeckAPI.listDecksByMostRecentlyPlayed
 * @see DeckAPI.listDecksByLeastRecentlyPlayed
 * @see DeckAPI.listNeverPlayedDecks
 * @see DeckAPI.listDecksByNumberOfHits
 * @see DeckAPI.listDecksByNumberOfMisses
 * @see DeckAPI.listDecksByHighestAverageAttemptNo
 * @see DeckAPI.listDecksByLowestAverageAttemptNo
 * @see DeckAPI.listDecksByMostMarkedAsFavourite
 */
fun listDecks(page: String): String {
    if (deckAPI.numberOfDecks() > 0) {
        var output = ""
        var chosenOption: Int
        var promptString: String = """
       ----------------------------------------------
       |   LIST DECKS                               
       ----------------------------------------------
        """.trimIndent()

        val listingOptionsEmpty: Set<String> = setOf("All decks", "Empty decks")
        val listingOptionsNotEmpty = setOf(
            "Decks with flashcards",
            "Decks by theme",
            "Decks by level",
            "Most recently played decks",
            "Least recently played decks",
            "Never played decks",
            "List decks by number of hits",
            "List decks by number of misses",
            "List decks by highest average number of attempts",
            "List decks by lowest average number of attempts",
            "List decks by highest number of flashcards marked as favourite"
        )

        val finalListingOptions: MutableSet<String> = if (page == "excludeEmpty") {
            listingOptionsNotEmpty as MutableSet<String>
        } else { // all
            listingOptionsEmpty.plus(listingOptionsNotEmpty) as MutableSet<String>
        }

        finalListingOptions.forEachIndexed { index, option: String ->
            promptString += """
            
            | ${index + 1}. $option
            """.trimIndent()
        }

        promptString += """
        
        ----------------------------------------------
        ==>> 
        """.trimIndent()
        do {
            chosenOption = readNextInt(promptString)

            if (page == "all") {
                when (chosenOption) {
                    1 -> output = deckAPI.listAllDecks()
                    2 -> output = deckAPI.listEmptyDecks()
                    3 -> output = deckAPI.listDecksWithFlashcards()
                    4 -> output = deckAPI.listDecksByTheme(askUserToChooseTheme())
                    5 -> output = deckAPI.listDecksByLevel(askUserToChooseLevel())
                    6 -> output = deckAPI.listDecksByMostRecentlyPlayed()
                    7 -> output = deckAPI.listDecksByLeastRecentlyPlayed()
                    8 -> output = deckAPI.listNeverPlayedDecks()
                    9 -> output = deckAPI.listDecksByNumberOfHits()
                    10 -> output = deckAPI.listDecksByNumberOfMisses()
                    11 -> output = deckAPI.listDecksByHighestAverageAttemptNo()
                    12 -> output = deckAPI.listDecksByLowestAverageAttemptNo()
                    13 -> output = deckAPI.listDecksByMostMarkedAsFavourite()
                    else -> println("Invalid option entered: $chosenOption")
                }
            } else { // excludeEmpty
                when (chosenOption) {
                    1 -> output = deckAPI.listDecksWithFlashcards()
                    2 -> output = deckAPI.listDecksByThemeNotEmpty(askUserToChooseTheme())
                    3 -> output = deckAPI.listDecksByLevelNotEmpty(askUserToChooseLevel())
                    4 -> output = deckAPI.listDecksByMostRecentlyPlayed()
                    5 -> output = deckAPI.listDecksByLeastRecentlyPlayed()
                    6 -> output = deckAPI.listNeverPlayedDecks()
                    7 -> output = deckAPI.listDecksByNumberOfHits()
                    8 -> output = deckAPI.listDecksByNumberOfMisses()
                    9 -> output = deckAPI.listDecksByHighestAverageAttemptNo()
                    10 -> output = deckAPI.listDecksByLowestAverageAttemptNo()
                    11 -> output = deckAPI.listDecksByMostMarkedAsFavourite()
                    else -> println("Invalid option entered: $chosenOption")
                }
            }
        } while (chosenOption !in 1..finalListingOptions.size + 1)
        return output
    } else {
        return "Option Invalid - No decks stored"
    }
}

/**
 * Generates a new deck based on the user's chosen option and the specified criteria.
 *
 * @param option The criteria for generating the deck ("Miss", "Hit", "Random", "Favourite").
 * @return A newly generated deck with the specified flashcards.
 *
 * The function calculates the maximum number of flashcards available based on the chosen option.
 * The user is prompted to input the desired number of flashcards for the generated deck, ensuring it does
 * not exceed the calculated maximum. The function then calls the `deckAPI` to generate a set of flashcards
 * based on the specified criteria, and a new deck is created with the generated flashcards.
 *
 * @see readNextInt
 * @see deckAPI.calculateOverallNumberOfMisses
 * @see deckAPI.calculateOverallNumberOfHits
 * @see deckAPI.calculateOverallNumberOfFlashcards
 * @see deckAPI.calculateOverallNumberOfFavourites
 * @see deckAPI.generateSetOfFlashcard
 * @see Deck
 */
fun generateDeck(option: String): Deck {
    logger.info { "Generate deck function invoked" }
    var numberOfFlashcardsChosen: Int
    val finalDeck = Deck(title = "Generated Deck ($option) in ${LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}", theme = "Generated", level = "Generated")

    val maxNoFlashcards: Int = when (option) {
        "Miss" -> deckAPI.calculateOverallNumberOfMisses()
        "Hit" -> deckAPI.calculateOverallNumberOfHits()
        "Random" -> deckAPI.calculateOverallNumberOfFlashcards()
        "Favourite" -> deckAPI.calculateOverallNumberOfFavourites()
        else -> 0
    }

    if (maxNoFlashcards > 0) {
        do {
            numberOfFlashcardsChosen = readNextInt("Please type the number of flashcards you wish your generated deck to have (maximum number: $maxNoFlashcards): ")
        } while (numberOfFlashcardsChosen > maxNoFlashcards)

        val flashcards = deckAPI.generateSetOfFlashcard(option, numberOfFlashcardsChosen)
        if (flashcards == null) {
            println("There was an error and it was not possible to generate your personalised deck.")
        } else {
            finalDeck.flashcards = flashcards
        }
    } else {
        println("You do not have cards marked as '$option'.")
    }
    return finalDeck
}

/**
 * Updates the details of an existing deck based on user input for title, theme, and level.
 * The function prompts the user to choose a deck to update and then provides options to modify its title, theme, and level.
 * The updated deck details are passed to the `deckAPI` for updating.
 *
 * @see askUserToChooseDeck
 * @see readValidString
 * @see askUserToChooseTheme
 * @see askUserToChooseLevel
 * @see deckAPI.updateDeck
 * @see Deck
 */
fun updateDeck() {
    logger.info { "Update deck function invoked" }
    if (deckAPI.numberOfDecks() > 0) {
        val deckToUpdate = askUserToChooseDeck("all")
        val deckTitle = readValidString("title")
        val deckTheme = askUserToChooseTheme()
        val deckLevel = askUserToChooseLevel()

        // pass the index of the note and the new note details to NoteAPI for updating and check for success.
        if (deckAPI.updateDeck(deckToUpdate!!.deckId, Deck(deckId = 0, title = deckTitle, theme = deckTheme, level = deckLevel))) {
            println("Update Successful")
            logger.info { "Update Successful" }
        } else {
            println("Update Failed")
            logger.info { "Update Failed" }
        }
    } else {
        println("There are no decks for this id number")
        logger.info { "User tried to update an invalid deck" }
    }
}

/**
 * Deletes an existing deck based on user input.
 * The function prompts the user to choose a deck to delete, and then calls the `deckAPI` to delete the selected deck.
 * It prints a success or failure message based on the result of the deletion operation.
 *
 * @see askUserToChooseDeck
 * @see deckAPI.deleteDeck
 */
fun deleteDeck() {
    logger.info { "Delete deck function invoked" }
    if (deckAPI.numberOfDecks() > 0) {
        val deckToDelete = askUserToChooseDeck("all")
        val deleteDeck = deckAPI.deleteDeck(deckToDelete!!.deckId)
        if (deleteDeck) {
            println("Delete Successful!")
            logger.info { "Delete deck successful: $deckToDelete" }
        } else {
            println("Delete NOT Successful")
            logger.info { "Delete deck not successful" }
        }
    }
}

/**
 * Searches for decks based on user input for a title.
 * The function prompts the user to enter a title to be searched, and then calls the `deckAPI` to search for decks
 * with titles matching the entered search term. It prints the search results or a message indicating no decks were found.
 *
 * @see readNextLine
 * @see deckAPI.searchDecksByTitle
 */
fun searchDecks() {
    logger.info { "Search deck function invoked" }
    val searchTitle = readNextLine("Enter a title to be searched: ")
    val searchResults = deckAPI.searchDecksByTitle(searchTitle)
    if (searchResults.isEmpty()) {
        logger.info { "No decks with title $searchTitle found" }
        println("No decks found")
    } else {
        logger.info { "Deck(s) with title $searchTitle found" }
        println(searchResults)
    }
}

// -------------------------------------------
// FLASHCARD MENU
// -------------------------------------------

/**
 * Creates a new flashcard based on user input for word, meaning, and type of word.
 *
 * The function prompts the user to enter a word, meaning, and the type of word (Noun, Verb, Adjective, Adverb, Expression),
 * and then constructs and returns a new `Flashcard` object with the entered details.
 *
 * @return A newly created flashcard with the specified word, meaning, and type of word.
 *
 * @see readValidString
 * @see readValidInput
 * @see Flashcard
 */
fun createFlashcard(): Flashcard {
    logger.info { "Create flashcard function invoked" }
    val word = readValidString("word")
    val meaning = readValidString("meaning")
    val typeNo = readValidInput("type of word", "Enter the word type (1-Noun, 2-Verb, 3-Adjective, 4-Adverb, 5-Expression): ")

    var type = ""
    when (typeNo) {
        1 -> type = "Noun"
        2 -> type = "Verb"
        3 -> type = "Adjective"
        4 -> type = "Adverb"
        5 -> type = "Expression"
    }

    return Flashcard(word = word, meaning = meaning, typeOfWord = type)
}

/**
 * Adds flashcards to a chosen deck based on user input.
 * The function prompts the user to choose a deck to add flashcards to and then enters a loop to continuously
 * add flashcards to the chosen deck until the user decides to stop.
 *
 * @see askUserToChooseDeck
 * @see createFlashcard
 * @see Deck.addFlashcard
 */
private fun addFlashcardToDeck() {
    logger.info { "Add flashcard to deck function invoked" }
    val deck: Deck? = askUserToChooseDeck("all")
    var continueAdding: Int
    if (deck != null) {
        do {
            if (deck.addFlashcard(createFlashcard())) {
                println("Added Successfully!")
                logger.info { "Flashcard added to deck ${deck.deckId}" }
            } else {
                println("Add operation NOT Successful")
                logger.info { "Add flashcard to deck failed" }
            }
            continueAdding = readNextInt("Would you like to continue adding flashcards to this deck? (1-YES | ANY OTHER NUMBER-NO): ")
        } while (continueAdding == 1)
    }
}

/**
 * Updates a flashcard within a chosen deck based on user input.
 * The function prompts the user to choose a deck with non-empty flashcards and then select a flashcard from the deck
 * to update. The user is then prompted to provide new details for the flashcard, and the deck is updated accordingly.
 *
 * @see askUserToChooseDeck
 * @see askUserToChooseFlashcard
 * @see createFlashcard
 * @see Deck.updateFlashcard
 */
fun updateFlashcardsInDeck() {
    logger.info { "Update flashcards in deck function invoked" }
    val deck: Deck? = askUserToChooseDeck("excludeEmpty")
    if (deck != null && deck.flashcards.isNotEmpty()) {
        val flashcard: Flashcard? = askUserToChooseFlashcard(deck)
        if (flashcard != null) {
            if (deck.updateFlashcard(flashcard.flashcardId, createFlashcard())) {
                println("Flashcard updated")
                logger.info { "Flashcard ${flashcard.flashcardId} in deck ${deck.deckId} updated" }
            } else {
                println("Flashcard NOT updated")
                logger.info { "Flashcard not updated" }
            }
        }
    }
}

/**
 * Deletes a flashcard from a chosen deck based on user input.
 * The function prompts the user to choose a deck with non-empty flashcards and then select a flashcard from the deck
 * to delete. The chosen flashcard is then deleted from the deck, and the user is informed of the operation's result.
 *
 * @see askUserToChooseDeck
 * @see askUserToChooseFlashcard
 * @see Deck.deleteFlashcard
 */
fun deleteFlashcard() {
    logger.info { "Delete flashcard function invoked" }
    val deck: Deck? = askUserToChooseDeck("excludeEmpty")
    if (deck != null) {
        val flashcard: Flashcard? = askUserToChooseFlashcard(deck)
        if (flashcard != null) {
            val isDeleted = deck.deleteFlashcard(flashcard.flashcardId)
            if (isDeleted) {
                println("Deleted Successfully!")
                logger.info { "Flashcard ${flashcard.flashcardId} in deck ${deck.deckId} deleted" }
            } else {
                println("Delete operation NOT Successful")
                logger.info { "Delete flashcard operation NOT Successful" }
            }
        }
    }
}

// ------------------------------------
// HELPER FUNCTIONS
// ------------------------------------

/**
 * Prompts the user to choose a deck based on a specified criteria and returns the selected deck.
 *
 * The function displays a list of decks using the [listDecks] function and prompts the user to enter the
 * ID of the deck they want to choose. It continues to prompt until a valid deck ID is entered.
 *
 * @param page Determines whether to include or exclude empty decks ("all" or "excludeEmpty").
 * @return The selected deck or null if no valid deck is chosen.
 *
 * @see listDecks
 * @see readNextInt
 * @see DeckAPI.findDeck
 */
private fun askUserToChooseDeck(page: String): Deck? {
    logger.info { "User prompted to choose a deck" }
    var output: String
    do {
        output = listDecks(page)
        println(output)
    } while (!output.contains("| ID: "))

    if (deckAPI.numberOfDecks() > 0) {
        val deck = deckAPI.findDeck(readNextInt("\nEnter the id of the deck: "))
        if (deck != null) {
            return deck
        } else {
            println("Deck id is not valid")
        }
    }
    return null
}

/**
 * Prompts the user to choose an option for generating a deck and returns the selected option.
 *
 * The function uses [readValidInput] to prompt the user to enter an option for generating a deck
 * and maps the chosen option number to corresponding deck generation criteria.
 *
 * @return The selected deck generation option.
 *
 * @see readValidInput
 */
private fun askUserToChooseGenerateOption(): String {
    logger.info { "User prompted to choose an option to generate a deck" }
    val chosenOption = readValidInput("option for generating a deck", "Play generated deck with (1-Flashcards that were 'Miss' | 2-Flashcards that were 'Hit' | 3-Favourite Flashcards | 4-Random Flashcards): ")
    var option = ""
    when (chosenOption) {
        1 -> option = "Miss"
        2 -> option = "Hit"
        3 -> option = "Favourite"
        4 -> option = "Random"
    }
    return option
}

/**
 * Prompts the user to choose a flashcard from a specified deck and returns the selected flashcard.
 *
 * The function checks if the deck has any flashcards. If flashcards are present, it displays the list of
 * flashcards using the [Deck.listFlashcards] function and prompts the user to enter the ID of the flashcard
 * they want to choose. It returns the selected flashcard or null if no flashcards are available in the deck.
 *
 * @param deck The deck from which the user chooses a flashcard.
 * @return The selected flashcard or null if no flashcards are available.
 *
 * @see Deck.listFlashcards
 * @see readNextInt
 * @see Deck.findFlashcard
 */
private fun askUserToChooseFlashcard(deck: Deck): Flashcard? {
    logger.info { "User prompted to choose a flashcard" }
    return if (deck.numberOfFlashcards() > 0) {
        print(deck.listFlashcards())
        deck.findFlashcard(readNextInt("\nEnter the id of the flashcard: "))
    } else {
        println("No flashcards for chosen deck")
        null
    }
}

/**
 * Prompts the user to choose a theme for a deck and returns the selected theme.
 *
 * The function uses [readValidInput] to prompt the user to enter a theme for a deck and maps the chosen
 * theme number to corresponding deck themes.
 *
 * @return The selected deck theme.
 *
 * @see readValidInput
 */
fun askUserToChooseTheme(): String {
    logger.info { "User prompted to choose a theme" }
    val chosenTheme = readValidInput("theme", "Enter a theme (1-Everyday, 2-Academic, 3-Professional, 4- Cultural and Idiomatic, 5-Emotions and Feelings): ")
    var deckTheme = ""
    when (chosenTheme) {
        1 -> deckTheme = "Everyday"
        2 -> deckTheme = "Academic"
        3 -> deckTheme = "Professional"
        4 -> deckTheme = "Cultural and Idiomatic"
        5 -> deckTheme = "Emotions and Feelings"
    }
    return deckTheme
}

/**
 * Prompts the user to choose a level for a deck and returns the selected level.
 *
 * The function uses [readValidInput] to prompt the user to enter a level for a deck and maps the chosen
 * level number to corresponding deck levels.
 *
 * @return The selected deck level.
 *
 * @see readValidInput
 */
fun askUserToChooseLevel(): String {
    logger.info { "User prompted to choose a level" }
    val chosenLevel = readValidInput("level", "Enter a level (1-Beginner, 2-Intermediate, 3-Advanced, 4- Proficient): ")
    var deckLevel = ""
    when (chosenLevel) {
        1 -> deckLevel = "Beginner"
        2 -> deckLevel = "Intermediate"
        3 -> deckLevel = "Advanced"
        4 -> deckLevel = "Proficient"
    }
    return deckLevel
}

// ------------------------------------
// PERSISTENCE FUNCTIONS
// ------------------------------------

/**
 * Saves the current state of the application's data, including decks and flashcards, to a file.
 *
 * The function uses [DeckAPI.store] to save the data. If the operation is successful, it prints a success message.
 * If an exception occurs during the saving process, it catches the exception and prints an error message.
 *
 * @see DeckAPI.store
 */
fun save() {
    try {
        deckAPI.store()
        println("Data successfully saved")
        logger.info { "Data saved to the system" }
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
        logger.info { "Error in saving data to the system" }
    }
}

/**
 * Loads previously saved data, including decks and flashcards, from a file to restore the application's state.
 *
 * The function uses [DeckAPI.load] to load the data. If the operation is successful, it prints a success message.
 * If an exception occurs during the loading process, it catches the exception and prints an error message.
 *
 * @see DeckAPI.load
 */
fun load() {
    try {
        deckAPI.load()
        println("Data successfully loaded")
        logger.info { "Data loaded into the system" }
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
        logger.info { "Error in loading data to the system" }
    }
}

// ------------------------------------
// Exit App
// ------------------------------------

/**
 * Exits the application by printing a farewell message and terminating the program.
 *
 * The function prints a farewell message to the console and terminates the program using [exitProcess].
 *
 * @see exitProcess
 */
fun exitApp() {
    logger.info { "Exiting application" }
    println("Exiting...bye")
    exitProcess(0)
}
