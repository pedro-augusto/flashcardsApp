package models

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

class DeckTest {

    private var feelings: Deck? = null

    @BeforeEach
    fun setup() {
        feelings = Deck(
            title = "Mixed Feelings",
            theme = "Emotions and Feelings",
            level = "Intermediate",
            lastDateAccessed = LocalDate.now().minusDays(3)
        )
        feelings!!.addFlashcard(Flashcard(word = "Enthousiasme", meaning = "Enthusiasm", typeOfWord = "Noun", hit = "Miss", favourite = true))
        feelings!!.addFlashcard(Flashcard(word = "Déception", meaning = "Disappointment", typeOfWord = "Noun", hit = "Hit", favourite = false))
        feelings!!.addFlashcard(Flashcard(word = "Excitation", meaning = "Excitement", typeOfWord = "Noun", hit = "Miss", favourite = true))
        feelings!!.addFlashcard(Flashcard(word = "Honte", meaning = "Shame", typeOfWord = "Noun", hit = "Hit", favourite = false))
    }

    @AfterEach
    fun tearDown() {
        feelings = null
    }

    @Nested
    inner class AddingFlashcards {
        @Test
        fun `Add flashcard returns true when flashcard is successfully added`() {
            val newFlashcard = Flashcard(word = "Colère", meaning = "Anger", typeOfWord = "Noun", hit = "Miss", favourite = true)
        }
    }
}
