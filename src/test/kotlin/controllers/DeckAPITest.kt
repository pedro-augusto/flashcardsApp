package controllers

import models.Deck
import models.Flashcard
import org.junit.jupiter.api.AfterEach
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import persistence.YAMLSerializer
import java.io.File
import java.time.LocalDate
import kotlin.test.Test

class DeckAPITest {

    private var school: Deck? =  null
    private var opportunities: Deck? = null
    private var trip: Deck? = null
    private var feelings: Deck? = null
    private var business: Deck? = null

    private var populatedArray: DeckAPI? = DeckAPI(YAMLSerializer(File("decks.yaml")))
    private var emptyArray: DeckAPI? = DeckAPI(YAMLSerializer(File("decks.yaml")))

    @BeforeEach
    fun setup(){

        school = Deck(
            title = "School Vocabulary",
            theme = "Academic",
            level = "Intermediate")
        school!!.addFlashcard(Flashcard( word = "École", meaning = "School", typeOfWord = "Noun"))
        school!!.addFlashcard(Flashcard( word = "École", meaning = "School", typeOfWord = "Noun"))
        school!!.addFlashcard(Flashcard( word = "Professeur", meaning = "Teacher", typeOfWord = "Noun"))
        school!!.addFlashcard(Flashcard( word = "Étudiant", meaning = "Student", typeOfWord = "Noun"))
        school!!.addFlashcard(Flashcard( word = "Tableau", meaning = "Board", typeOfWord = "Noun"))

        opportunities = Deck(
            title = "Missed Opportunities",
            theme = "Professional",
            level = "Advanced",
            lastDateAccessed = LocalDate.now().minusDays(7))
        opportunities!!.addFlashcard(Flashcard( word = "Opportunité", meaning = "Opportunity", typeOfWord = "Noun", hit = "Miss", attempts = 1))
        opportunities!!.addFlashcard(Flashcard( word = "Réussir", meaning = "To succeed", typeOfWord = "Verb", hit = "Miss", attempts = 2, favourite = true))
        opportunities!!.addFlashcard(Flashcard( word = "Échec", meaning = "Failure", typeOfWord = "Noun", hit = "Miss", attempts = 3, favourite = true))
        opportunities!!.addFlashcard(Flashcard( word = "Défi", meaning = "Challenge", typeOfWord = "Noun", hit = "Hit"))

        trip = Deck(
            title = "Trip and Adventure",
            theme = "Cultural and Idiomatic",
            level = "Proficient")

        business = Deck(
            title = "French Business Terms",
            theme = "Professional",
            level = "Intermediate"
        )

        feelings = Deck(
            title = "Mixed Feelings",
            theme = "Emotions and Feelings",
            level = "Intermediate",
            lastDateAccessed = LocalDate.now().minusDays(3))
        feelings!!.addFlashcard(Flashcard( word = "Enthousiasme", meaning = "Enthusiasm", typeOfWord = "Noun", hit = "Miss", favourite = true))
        feelings!!.addFlashcard(Flashcard( word = "Déception", meaning = "Disappointment", typeOfWord = "Noun", hit = "Hit", favourite = false))
        feelings!!.addFlashcard(Flashcard( word = "Excitation", meaning = "Excitement", typeOfWord = "Noun", hit = "Miss", favourite = true))
        feelings!!.addFlashcard(Flashcard( word = "Honte", meaning = "Shame", typeOfWord = "Noun", hit = "Hit", favourite = false))

        populatedArray!!.addDeck(school!!)
        populatedArray!!.addDeck(opportunities!!)
        populatedArray!!.addDeck(trip!!)
        populatedArray!!.addDeck(business!!)
        populatedArray!!.addDeck(feelings!!)
    }

    @AfterEach
    fun tearDown(){
        school = null
        opportunities = null
        trip = null
        business = null
        feelings = null
    }

    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfNotesCalculatedCorrectly() {
            assertEquals(5, populatedArray!!.numberOfDecks())
            assertEquals(0, emptyArray!!.numberOfDecks())
        }

        @Test
        fun numberOfDecksWithFlashcardsCalculatedCorrectly() {
            assertEquals(3, populatedArray!!.numberOfDecksWithFlashcards())
            assertEquals(0, emptyArray!!.numberOfDecksWithFlashcards())
        }

        @Test
        fun numberOfEmptyDecksCalculatedCorrectly() {
            assertEquals(2, populatedArray!!.numberOfEmptyDecks())
            assertEquals(0, emptyArray!!.numberOfEmptyDecks())
        }
    }


    @Nested
    inner class AddDecks {

        @Test
        fun `adding a Deck to a populated list adds to ArrayList`() {
            val newDeck = Deck(
                title = "Matières Scolaires",
                theme = "Academic",
                level = "Beginner",
                flashcards = mutableSetOf(
                    Flashcard(word = "Mathématiques", meaning = "Mathematics", typeOfWord = "Noun", hit = "Miss", attempts = 2),
                    Flashcard(word = "Chimie", meaning = "Chemistry", typeOfWord = "Noun", hit = "Miss", attempts = 1, favourite = true),
                    Flashcard(word = "Biologie", meaning = "Biology", typeOfWord = "Noun", hit = "Miss", attempts = 3),
                    Flashcard(word = "Physique", meaning = "Physics", typeOfWord = "Noun", hit = "Miss", attempts = 1)))
            assertEquals(5, populatedArray!!.numberOfDecks())
            assertTrue(populatedArray!!.addDeck(newDeck))
            assertEquals(6, populatedArray!!.numberOfDecks())
            assertEquals(newDeck, populatedArray!!.findDeck(populatedArray!!.numberOfDecks() - 1))
        }

        @Test
        fun `adding a Deck to an empty list adds to ArrayList`() {
            val newDeck = Deck(
                title = "Matières Scolaires",
                theme = "Academic",
                level = "Beginner",
                flashcards = mutableSetOf(
                    Flashcard(word = "Mathématiques", meaning = "Mathematics", typeOfWord = "Noun", hit = "Miss", attempts = 2),
                    Flashcard(word = "Chimie", meaning = "Chemistry", typeOfWord = "Noun", hit = "Miss", attempts = 1, favourite = true),
                    Flashcard(word = "Biologie", meaning = "Biology", typeOfWord = "Noun", hit = "Miss", attempts = 3),
                    Flashcard(word = "Physique", meaning = "Physics", typeOfWord = "Noun", hit = "Miss", attempts = 1)))

            assertEquals(0, emptyArray!!.numberOfDecks())
            assertTrue(emptyArray!!.addDeck(newDeck))
            assertEquals(1, emptyArray!!.numberOfDecks())
            assertEquals(newDeck, emptyArray!!.findDeck(emptyArray!!.numberOfDecks() - 1))
        }
    }



    @Nested
    inner class ListDecks {

        @Test
        fun `listAllDecks returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyArray!!.numberOfDecks())
            assertTrue(emptyArray!!.listAllDecks().contains("no decks", true))
        }

        @Test
        fun `listAllDecks returns Decks when ArrayList has notes stored`() {
            assertEquals(5, populatedArray!!.numberOfDecks())
            val notesString = populatedArray!!.listAllDecks()
            assertTrue(notesString.contains("School Vocabulary", true))
            assertTrue(notesString.contains("Missed Opportunities", true))
            assertTrue(notesString.contains("Trip and Adventure", true))
            assertTrue(notesString.contains("French Business Terms", true))
            assertTrue(notesString.contains("Mixed Feelings", true))
        }

        @Nested
        inner class WithFlashcards {
            @Test
            fun `listDecksWithFlashcards returns No decks with flashcards stored when ArrayList empty`() {
                assertEquals(0, emptyArray!!.numberOfDecksWithFlashcards())
                assertTrue(emptyArray!!.listDecksWithFlashcards().contains("No decks with flashcards stored", true))
            }

            @Test
            fun `listDecksWithFlashcards returns decks with flashcards when ArrayList has decks with flashcards`() {
                assertEquals(3, populatedArray!!.numberOfDecksWithFlashcards())
                val notesString = populatedArray!!.listDecksWithFlashcards()
                assertTrue(notesString.contains("School Vocabulary", true))
                assertTrue(notesString.contains("Missed Opportunities", true))
                assertTrue(notesString.contains("Mixed Feelings", true))
            }
        }

        @Nested
        inner class EmptyDecks {
            @Test
            fun `listEmptyDecks returns No Empty Decks Stored when ArrayList empty`() {
                assertEquals(0, emptyArray!!.numberOfEmptyDecks())
                assertTrue(emptyArray!!.listEmptyDecks().contains("No empty decks stored", true))
            }

            @Test
            fun `listEmptyDecks returns empty decks when ArrayList has empty decks stored`() {
                assertEquals(2, populatedArray!!.numberOfEmptyDecks())
                val notesString = populatedArray!!.listEmptyDecks()
                assertFalse(notesString.contains("School Vocabulary", true))
                assertFalse(notesString.contains("Missed Opportunities", true))
                assertFalse(notesString.contains("Mixed Feelings", true))
                assertTrue(notesString.contains("French Business Terms", true))
                assertTrue(notesString.contains("Trip and Adventure", true))
            }
        }
    }



    @Nested
    inner class DeleteDeck {
        @Test
        fun `deleting a Deck that does not exist returns null`() {
            assertFalse(emptyArray!!.deleteDeck(0))
            assertFalse(emptyArray!!.deleteDeck(-1))
            assertFalse(emptyArray!!.deleteDeck(10))
        }

        @Test
        fun `deleting a deck that exists deletes deck`() {
            assertEquals(5, populatedArray!!.numberOfDecks())
            assertEquals(2, populatedArray!!.numberOfEmptyDecks())
            assertEquals(3, populatedArray!!.numberOfDecksWithFlashcards())
            assertTrue(populatedArray!!.deleteDeck(3))
            assertEquals(4, populatedArray!!.numberOfDecks())
            assertEquals(1, populatedArray!!.numberOfEmptyDecks())
            assertEquals(3, populatedArray!!.numberOfDecksWithFlashcards())
            assertFalse(populatedArray!!.listAllDecks().contains("French Business Terms"))
            assertFalse(populatedArray!!.listEmptyDecks().contains("French Business Terms"))
        }
    }


    @Nested
    inner class UpdateDeck{
        @Test
        fun `updating a deck that does not exist returns false`(){
            assertFalse(populatedArray!!.updateDeck(10,Deck(title = "UpdatedDeck", theme = "Professional", level = "Advanced")))
            assertFalse(populatedArray!!.updateDeck(-1,Deck(title = "UpdatedDeck", theme = "Professional", level = "Advanced")))
            assertFalse(emptyArray!!.updateDeck(1,Deck(title = "UpdatedDeck", theme = "Professional", level = "Advanced")))
        }

        @Test
        fun `updating a deck that exists returns true and updates`(){
            //check deck 4 exists and check the contents
            assertEquals(feelings,populatedArray!!.findDeck(4))
            assertEquals("Mixed Feelings", populatedArray!!.findDeck(4)!!.title)
            assertEquals("Emotions and Feelings", populatedArray!!.findDeck(4)!!.theme)
            assertEquals("Intermediate", populatedArray!!.findDeck(4)!!.level)
            assertEquals(LocalDate.now().minusDays(3), populatedArray!!.findDeck(4)!!.lastDateAccessed)
                // flashcards
            assertEquals(Flashcard(0, word = "Enthousiasme", meaning = "Enthusiasm", typeOfWord = "Noun", hit = "Miss", favourite = true),
                populatedArray!!.findDeck(4)?.findFlashcard(0))
            assertEquals(Flashcard(1, word = "Déception", meaning = "Disappointment", typeOfWord = "Noun", hit = "Hit", favourite = false),
                populatedArray!!.findDeck(4)?.findFlashcard(1))
            assertEquals(Flashcard(2, word = "Excitation", meaning = "Excitement", typeOfWord = "Noun", hit = "Miss", favourite = true),
                populatedArray!!.findDeck(4)?.findFlashcard(2))
            assertEquals(Flashcard(3, word = "Honte", meaning = "Shame", typeOfWord = "Noun", hit = "Hit", favourite = false),
                populatedArray!!.findDeck(4)?.findFlashcard(3))


            //update deck 4 with new information and ensure contents updated successfully
            assertTrue(populatedArray!!.updateDeck(4,Deck(title= "Updated Deck", theme = "Cultural and Idiomatic", level = "Beginner")))
            assertEquals("Updated Deck", populatedArray!!.findDeck(4)!!.title)
            assertEquals("Cultural and Idiomatic", populatedArray!!.findDeck(4)!!.theme)
            assertEquals("Beginner", populatedArray!!.findDeck(4)!!.level)
            //checking flashcards are still the same
            assertEquals(Flashcard(0, word = "Enthousiasme", meaning = "Enthusiasm", typeOfWord = "Noun", hit = "Miss", favourite = true),
                populatedArray!!.findDeck(4)?.findFlashcard(0))
            assertEquals(Flashcard(1, word = "Déception", meaning = "Disappointment", typeOfWord = "Noun", hit = "Hit", favourite = false),
                populatedArray!!.findDeck(4)?.findFlashcard(1))
            assertEquals(Flashcard(2, word = "Excitation", meaning = "Excitement", typeOfWord = "Noun", hit = "Miss", favourite = true),
                populatedArray!!.findDeck(4)?.findFlashcard(2))
            assertEquals(Flashcard(3, word = "Honte", meaning = "Shame", typeOfWord = "Noun", hit = "Hit", favourite = false),
                populatedArray!!.findDeck(4)?.findFlashcard(3))
        }

    }

}