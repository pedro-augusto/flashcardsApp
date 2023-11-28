package models

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DeckTest {
    private var emptyDeck: Deck? = null
    private var populatedDeck: Deck? = null

    @BeforeEach
    fun setup() {
        emptyDeck = Deck(
            title = "Mixed Feelings",
            theme = "Emotions and Feelings",
            level = "Intermediate",
            lastDateAccessed = LocalDate.now().minusDays(3)
        )

        populatedDeck = Deck(
            title = "Mixed Feelings",
            theme = "Emotions and Feelings",
            level = "Intermediate",
            lastDateAccessed = LocalDate.now().minusDays(3)
        )
        populatedDeck!!.addFlashcard(Flashcard(word = "Enthousiasme", meaning = "Enthusiasm", typeOfWord = "Noun", hit = "Miss", favourite = true))
        populatedDeck!!.addFlashcard(Flashcard(word = "Déception", meaning = "Disappointment", typeOfWord = "Noun", hit = "Hit", favourite = false))
        populatedDeck!!.addFlashcard(Flashcard(word = "Excitation", meaning = "Excitement", typeOfWord = "Noun", hit = "Miss", favourite = true))
        populatedDeck!!.addFlashcard(Flashcard(word = "Honte", meaning = "Shame", typeOfWord = "Noun", hit = "Hit", favourite = false))
    }

    @AfterEach
    fun tearDown() {
        populatedDeck = null
        emptyDeck = null
    }

    @Nested
    inner class AddFlashcard {
        @Test
        fun `Add flashcard returns true when flashcard is added to empty deck`() {
            val newFlashcard = Flashcard(word = "Colère", meaning = "Anger", typeOfWord = "Noun", hit = "Miss", favourite = true)
            assertTrue(emptyDeck!!.addFlashcard(newFlashcard))
            assertTrue(emptyDeck!!.listFlashcards().contains("Colère", true))
        }

        @Test
        fun `Add flashcard returns true when flashcard is added to populated deck`() {
            val newFlashcard = Flashcard(word = "Colère", meaning = "Anger", typeOfWord = "Noun", hit = "Miss", favourite = true)
            assertTrue(populatedDeck!!.addFlashcard(newFlashcard))
            assertTrue(populatedDeck!!.listFlashcards().contains("Colère", true))
        }
    }

    @Nested
    inner class FindFlashcard {

        @Test
        fun `findFlashcard returns the correct flashcards when deck is populated`() {
            assertEquals(4, populatedDeck!!.numberOfFlashcards())
            assertEquals(
                Flashcard(
                    word = "Enthousiasme",
                    meaning = "Enthusiasm",
                    typeOfWord = "Noun",
                    hit = "Miss",
                    favourite = true
                ),
                populatedDeck!!.findFlashcard(0)
            )
            assertEquals(
                Flashcard(
                    flashcardId = 1,
                    word = "Déception",
                    meaning = "Disappointment",
                    typeOfWord = "Noun",
                    hit = "Hit",
                    favourite = false
                ),
                populatedDeck!!.findFlashcard(1)
            )
            assertEquals(
                Flashcard(
                    flashcardId = 2,
                    word = "Excitation",
                    meaning = "Excitement",
                    typeOfWord = "Noun",
                    hit = "Miss",
                    favourite = true
                ),
                populatedDeck!!.findFlashcard(2)
            )
            assertEquals(
                Flashcard(
                    flashcardId = 3,
                    word = "Honte",
                    meaning = "Shame",
                    typeOfWord = "Noun",
                    hit = "Hit",
                    favourite = false
                ),
                populatedDeck!!.findFlashcard(3)
            )
        }

        @Test
        fun `findFlashcard returns null when deck is populated and flashcardId does not exist on deck`() {
            assertEquals(4, populatedDeck!!.numberOfFlashcards())
            assertNull(populatedDeck!!.findFlashcard(-1))
            assertNull(populatedDeck!!.findFlashcard(100))
        }

        @Test
        fun `findFlashcard returns null when deck is empty`() {
            assertEquals(0, emptyDeck!!.numberOfFlashcards())
            assertNull(emptyDeck!!.findFlashcard(-1))
            assertNull(emptyDeck!!.findFlashcard(0))
            assertNull(emptyDeck!!.findFlashcard(100))
        }
    }

    @Nested
    inner class DeleteFlashcard {
        @Test
        fun `deleteFlashcard returns true when a flashcard in a populated deck is successfully deleted`() {
            assertEquals(4, populatedDeck!!.numberOfFlashcards())
            assertTrue(populatedDeck!!.deleteFlashcard(0))
            assertTrue(populatedDeck!!.deleteFlashcard(1))
            assertTrue(populatedDeck!!.deleteFlashcard(2))
            assertTrue(populatedDeck!!.deleteFlashcard(3))

            assertFalse(populatedDeck!!.listFlashcards().contains("Enthousiasme", true))
            assertFalse(populatedDeck!!.listFlashcards().contains("Déception", true))
            assertFalse(populatedDeck!!.listFlashcards().contains("Excitation", true))
            assertFalse(populatedDeck!!.listFlashcards().contains("Honte", true))

            assertEquals(0, populatedDeck!!.numberOfFlashcards())
        }

        @Test
        fun `deleteFlashcard returns false when flashcardId doesn't exist in a populated deck`() {
            assertEquals(4, populatedDeck!!.numberOfFlashcards())
            assertFalse(populatedDeck!!.deleteFlashcard(-1))
            assertFalse(populatedDeck!!.deleteFlashcard(5))
            assertFalse(populatedDeck!!.deleteFlashcard(100))
            assertEquals(4, populatedDeck!!.numberOfFlashcards())
        }

        @Test
        fun `deleteFlashcard returns false in an empty deck`() {
            assertEquals(0, emptyDeck!!.numberOfFlashcards())
            assertFalse(emptyDeck!!.deleteFlashcard(-1))
            assertFalse(emptyDeck!!.deleteFlashcard(0))
            assertFalse(emptyDeck!!.deleteFlashcard(100))
        }
    }

    @Nested
    inner class UpdateFlashcard {
        @Test
        fun `updateFlashcard returns true and updates when flashcardId is valid in a populated deck`() {
            assertTrue(populatedDeck!!.updateFlashcard(1, Flashcard(word = "Anxiété", meaning = "Anxiety", typeOfWord = "Noun", hit = "Miss", favourite = true)))
            assertFalse(populatedDeck!!.listFlashcards().contains("Déception", true))
            assertTrue(populatedDeck!!.listFlashcards().contains("Anxiété", true))
        }

        @Test
        fun `updateFlashcard returns false when flashcardId is invalid in a populated deck`() {
            assertFalse(populatedDeck!!.updateFlashcard(-1, Flashcard(word = "Anxiété", meaning = "Anxiety", typeOfWord = "Noun", hit = "Miss", favourite = true)))
            assertFalse(populatedDeck!!.updateFlashcard(5, Flashcard(word = "Anxiété", meaning = "Anxiety", typeOfWord = "Noun", hit = "Miss", favourite = true)))
            assertFalse(populatedDeck!!.updateFlashcard(100, Flashcard(word = "Anxiété", meaning = "Anxiety", typeOfWord = "Noun", hit = "Miss", favourite = true)))
        }
    }

    @Nested
    inner class Calculate {
        @Test
        fun `number of flashcards is correctly calculated for empty and populated decks`() {
            assertEquals(0, emptyDeck!!.numberOfFlashcards())
            assertEquals(4, populatedDeck!!.numberOfFlashcards())
        }

        @Test
        fun `listFlashcards returns no flashcards added when deck is empty`() {
            assertTrue(emptyDeck!!.listFlashcards().contains("no flashcards added", true))
        }

        @Test
        fun `calculatePercentage returns correct percentage when deck is populated`() {
            assertEquals(50.0, populatedDeck!!.calculateHitsPercentage())
        }

        @Test
        fun `calculatePercentage returns null when deck is empty`() {
            assertNull(emptyDeck!!.calculateHitsPercentage())
        }

        @Test
        fun `NumberOfHits returns correct number when deck is populated`() {
            assertEquals(2, populatedDeck!!.numberOfHits())
        }

        @Test
        fun `numberOfHits returns null when deck is empty`() {
            assertEquals(0, emptyDeck!!.numberOfHits())
        }

        @Test
        fun `numberOfMisses returns correct number when deck is populated`() {
            assertEquals(2, populatedDeck!!.numberOfMisses())
        }

        @Test
        fun `numberOfMisses returns null when deck is empty`() {
            assertEquals(0, emptyDeck!!.numberOfMisses())
        }

        @Test
        fun `numberOfFavourites returns correct number when deck is populated`() {
            assertEquals(2, populatedDeck!!.numberOfFavourites())
        }

        @Test
        fun `numberOfFavourites returns null when deck is empty`() {
            assertEquals(0, emptyDeck!!.numberOfFavourites())
        }

        @Test
        fun `calculateDeckAverageAttemptNo returns correct number when deck is populated`() {
            populatedDeck!!.findFlashcard(0)!!.attempts = 3
            populatedDeck!!.findFlashcard(1)!!.attempts = 1
            populatedDeck!!.findFlashcard(2)!!.attempts = 5
            populatedDeck!!.findFlashcard(3)!!.attempts = 8
            assertEquals(4.25, populatedDeck!!.calculateDeckAverageAttemptNo())
        }

        @Test
        fun `calculateDeckAverageAttemptNo returns null when deck is empty`() {
            assertNull(emptyDeck!!.calculateDeckAverageAttemptNo())
        }

        @Test
        fun `calculateNoOfFavouriteFlashcards returns correct number when deck is populated`() {
            assertEquals(2, populatedDeck!!.calculateNoOfFavouriteFlashcards())
            populatedDeck!!.findFlashcard(1)!!.favourite = true
            assertEquals(3, populatedDeck!!.calculateNoOfFavouriteFlashcards())
        }

        @Test
        fun `calculateNoOfFavouriteFlashcards returns null when deck is empty`() {
            assertNull(emptyDeck!!.calculateNoOfFavouriteFlashcards())
        }
    }

    @Nested
    inner class Get {

        @Test
        fun `getHits returns correct flashcards when deck is populated`() {
            assertEquals(2, populatedDeck!!.numberOfHits())
            assertTrue(utils.Utilities.formatSetString(populatedDeck!!.getHits()).contains("Déception", true))
            assertTrue(utils.Utilities.formatSetString(populatedDeck!!.getHits()).contains("Honte", true))
            assertFalse(utils.Utilities.formatSetString(populatedDeck!!.getHits()).contains("Excitation", true))
            assertFalse(utils.Utilities.formatSetString(populatedDeck!!.getHits()).contains("Enthousiasme", true))
        }

        @Test
        fun `getHits returns empty when deck is empty`() {
            assertEquals(0, emptyDeck!!.numberOfHits())
            assertFalse(utils.Utilities.formatSetString(emptyDeck!!.getHits()).contains("Excitation", true))
            assertFalse(utils.Utilities.formatSetString(emptyDeck!!.getHits()).contains("Enthousiasme", true))
            assertFalse(utils.Utilities.formatSetString(emptyDeck!!.getHits()).contains("Déception", true))
            assertFalse(utils.Utilities.formatSetString(emptyDeck!!.getHits()).contains("Honte", true))
            assertTrue(emptyDeck!!.getHits().isEmpty())
        }

        @Test
        fun `getMisses returns correct flashcards when deck is populated`() {
            assertEquals(2, populatedDeck!!.numberOfMisses())
            assertFalse(utils.Utilities.formatSetString(populatedDeck!!.getMisses()).contains("Déception", true))
            assertFalse(utils.Utilities.formatSetString(populatedDeck!!.getMisses()).contains("Honte", true))
            assertTrue(utils.Utilities.formatSetString(populatedDeck!!.getMisses()).contains("Excitation", true))
            assertTrue(utils.Utilities.formatSetString(populatedDeck!!.getMisses()).contains("Enthousiasme", true))
        }

        @Test
        fun `getMisses returns empty when deck is empty`() {
            assertEquals(0, emptyDeck!!.numberOfMisses())
            assertFalse(utils.Utilities.formatSetString(emptyDeck!!.getMisses()).contains("Excitation", true))
            assertFalse(utils.Utilities.formatSetString(emptyDeck!!.getMisses()).contains("Enthousiasme", true))
            assertFalse(utils.Utilities.formatSetString(emptyDeck!!.getMisses()).contains("Déception", true))
            assertFalse(utils.Utilities.formatSetString(emptyDeck!!.getMisses()).contains("Honte", true))
            assertTrue(emptyDeck!!.getMisses().isEmpty())
        }

        @Test
        fun `getFavourites returns correct flashcards when deck is populated`() {
            assertEquals(2, populatedDeck!!.numberOfFavourites())
            assertFalse(utils.Utilities.formatSetString(populatedDeck!!.getFavourites()).contains("Déception", true))
            assertFalse(utils.Utilities.formatSetString(populatedDeck!!.getFavourites()).contains("Honte", true))
            assertTrue(utils.Utilities.formatSetString(populatedDeck!!.getFavourites()).contains("Excitation", true))
            assertTrue(utils.Utilities.formatSetString(populatedDeck!!.getFavourites()).contains("Enthousiasme", true))
        }

        @Test
        fun `getFavourites returns empty when deck is empty`() {
            assertEquals(0, emptyDeck!!.numberOfFavourites())
            assertFalse(utils.Utilities.formatSetString(emptyDeck!!.getFavourites()).contains("Excitation", true))
            assertFalse(utils.Utilities.formatSetString(emptyDeck!!.getFavourites()).contains("Enthousiasme", true))
            assertFalse(utils.Utilities.formatSetString(emptyDeck!!.getFavourites()).contains("Déception", true))
            assertFalse(utils.Utilities.formatSetString(emptyDeck!!.getFavourites()).contains("Honte", true))
            assertTrue(emptyDeck!!.getFavourites().isEmpty())
        }
    }
}
