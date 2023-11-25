package controllers

import models.Deck
import models.Flashcard
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import persistence.YAMLSerializer
import java.io.File
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DeckAPITest {

    private var school: Deck? = null
    private var opportunities: Deck? = null
    private var trip: Deck? = null
    private var feelings: Deck? = null
    private var business: Deck? = null

    private var populatedArray: DeckAPI? = DeckAPI(YAMLSerializer(File("decks.yaml")))
    private var emptyArray: DeckAPI? = DeckAPI(YAMLSerializer(File("decks.yaml")))

    @BeforeEach
    fun setup() {
        school = Deck(
            title = "School Vocabulary",
            theme = "Academic",
            level = "Intermediate"
        )
        school!!.addFlashcard(Flashcard(word = "École", meaning = "School", typeOfWord = "Noun"))
        school!!.addFlashcard(Flashcard(word = "École", meaning = "School", typeOfWord = "Noun"))
        school!!.addFlashcard(Flashcard(word = "Professeur", meaning = "Teacher", typeOfWord = "Noun"))
        school!!.addFlashcard(Flashcard(word = "Étudiant", meaning = "Student", typeOfWord = "Noun"))
        school!!.addFlashcard(Flashcard(word = "Tableau", meaning = "Board", typeOfWord = "Noun"))

        opportunities = Deck(
            title = "Missed Opportunities",
            theme = "Professional",
            level = "Advanced",
            lastDateAccessed = LocalDate.now().minusDays(7)
        )
        opportunities!!.addFlashcard(Flashcard(word = "Opportunité", meaning = "Opportunity", typeOfWord = "Noun", hit = "Miss", attempts = 1))
        opportunities!!.addFlashcard(Flashcard(word = "Réussir", meaning = "To succeed", typeOfWord = "Verb", hit = "Miss", attempts = 2, favourite = true))
        opportunities!!.addFlashcard(Flashcard(word = "Échec", meaning = "Failure", typeOfWord = "Noun", hit = "Miss", attempts = 3, favourite = true))
        opportunities!!.addFlashcard(Flashcard(word = "Défi", meaning = "Challenge", typeOfWord = "Noun", hit = "Hit"))

        trip = Deck(
            title = "Trip and Adventure",
            theme = "Cultural and Idiomatic",
            level = "Proficient"
        )

        business = Deck(
            title = "French Business Terms",
            theme = "Professional",
            level = "Intermediate"
        )

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

        populatedArray!!.addDeck(school!!)
        populatedArray!!.addDeck(opportunities!!)
        populatedArray!!.addDeck(trip!!)
        populatedArray!!.addDeck(business!!)
        populatedArray!!.addDeck(feelings!!)
    }

    @AfterEach
    fun tearDown() {
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
                    Flashcard(word = "Physique", meaning = "Physics", typeOfWord = "Noun", hit = "Miss", attempts = 1)
                )
            )
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
                    Flashcard(word = "Physique", meaning = "Physics", typeOfWord = "Noun", hit = "Miss", attempts = 1)
                )
            )

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

        @Nested
        inner class ByTheme {
            @Test
            fun `listDecksByTheme returns correct decks when array is populated`() {
                assertEquals(2, populatedArray!!.numberOfDecksByTheme("Professional"))
                assertTrue(populatedArray!!.listDecksByTheme("Professional").contains("Missed Opportunities", true))
                assertTrue(populatedArray!!.listDecksByTheme("Professional").contains("French Business Terms", true))
                assertFalse(populatedArray!!.listDecksByTheme("Professional").contains("Mixed Feelings", true))
                assertFalse(populatedArray!!.listDecksByTheme("Professional").contains("Trip and Adventure", true))
                assertFalse(populatedArray!!.listDecksByTheme("Professional").contains("School Vocabulary", true))
            }

            @Test
            fun `listDecksByTheme returns message when there are no decks with that category in a populated array`() {
                assertEquals(0, populatedArray!!.numberOfDecksByTheme("Everyday"))
                assertTrue(populatedArray!!.listDecksByTheme("Everyday").contains("No decks with this theme stored", true))
            }

            @Test
            fun `listDecksByTheme returns message when array is empty`() {
                assertEquals(0, emptyArray!!.numberOfDecksByTheme("Everyday"))
                assertEquals(0, emptyArray!!.numberOfDecksByTheme("Academic"))
                assertEquals(0, emptyArray!!.numberOfDecksByTheme("Professional"))
                assertEquals(0, emptyArray!!.numberOfDecksByTheme("Cultural and Idiomatic"))
                assertEquals(0, emptyArray!!.numberOfDecksByTheme("Emotions and Feelings"))
                assertTrue(emptyArray!!.listDecksByTheme("Everyday").contains("No decks with this theme stored", true))
                assertTrue(emptyArray!!.listDecksByTheme("Academic").contains("No decks with this theme stored", true))
                assertTrue(emptyArray!!.listDecksByTheme("Professional").contains("No decks with this theme stored", true))
                assertTrue(emptyArray!!.listDecksByTheme("Cultural and Idiomatic").contains("No decks with this theme stored", true))
                assertTrue(emptyArray!!.listDecksByTheme("Emotions and Feelings").contains("No decks with this theme stored", true))
            }
        }

        @Nested
        inner class ByLevelNotEmpty {
            @Test
            fun `listDecksByLevelNotEmpty returns correct decks when array is populated`() {
                assertEquals(2, populatedArray!!.numberOfDecksByLevelNotEmpty("Intermediate"))
                assertTrue(populatedArray!!.listDecksByLevelNotEmpty("Intermediate").contains("Mixed Feelings", true))
                assertTrue(populatedArray!!.listDecksByLevelNotEmpty("Intermediate").contains("School Vocabulary", true))
                assertFalse(populatedArray!!.listDecksByLevelNotEmpty("Intermediate").contains("French Business Terms", true))
                assertFalse(populatedArray!!.listDecksByLevelNotEmpty("Intermediate").contains("Trip and Adventure", true))
                assertFalse(populatedArray!!.listDecksByLevelNotEmpty("Intermediate").contains("Missed Opportunities", true))
            }

            @Test
            fun `listDecksByLevelNotEmpty returns message when there are no decks with that level in a populated array`() {
                assertEquals(0, populatedArray!!.numberOfDecksByLevelNotEmpty("Beginner"))
                assertTrue(populatedArray!!.listDecksByLevelNotEmpty("Beginner").contains("No decks with flashcards with this level stored", true))
            }

            @Test
            fun `listDecksByLevelNotEmpty returns message when array is empty`() {
                assertEquals(0, emptyArray!!.numberOfDecksByLevelNotEmpty("Beginner"))
                assertEquals(0, emptyArray!!.numberOfDecksByLevelNotEmpty("Intermediate"))
                assertEquals(0, emptyArray!!.numberOfDecksByLevelNotEmpty("Advanced"))
                assertEquals(0, emptyArray!!.numberOfDecksByLevelNotEmpty("Proficient"))
                assertTrue(emptyArray!!.listDecksByLevelNotEmpty("Beginner").contains("No decks with flashcards with this level stored", true))
                assertTrue(emptyArray!!.listDecksByLevelNotEmpty("Intermediate").contains("No decks with flashcards with this level stored", true))
                assertTrue(emptyArray!!.listDecksByLevelNotEmpty("Advanced").contains("No decks with flashcards with this level stored", true))
                assertTrue(emptyArray!!.listDecksByLevelNotEmpty("Proficient").contains("No decks with flashcards with this level stored", true))
            }
        }

        @Nested
        inner class ByLevel {
            @Test
            fun `listDecksByLevel returns correct decks when array is populated`() {
                assertEquals(3, populatedArray!!.numberOfDecksByLevel("Intermediate"))
                assertTrue(populatedArray!!.listDecksByLevel("Intermediate").contains("Mixed Feelings", true))
                assertTrue(populatedArray!!.listDecksByLevel("Intermediate").contains("School Vocabulary", true))
                assertTrue(populatedArray!!.listDecksByLevel("Intermediate").contains("French Business Terms", true))
                assertFalse(populatedArray!!.listDecksByLevel("Intermediate").contains("Trip and Adventure", true))
                assertFalse(populatedArray!!.listDecksByLevel("Intermediate").contains("Missed Opportunities", true))
            }

            @Test
            fun `listDecksByLevel returns message when there are no decks with that level in a populated array`() {
                assertEquals(0, populatedArray!!.numberOfDecksByLevel("Beginner"))
                assertTrue(populatedArray!!.listDecksByLevel("Beginner").contains("No decks with this level stored", true))
            }

            @Test
            fun `listDecksByLevelNotEmpty returns message when array is empty`() {
                assertEquals(0, emptyArray!!.numberOfDecksByLevel("Beginner"))
                assertEquals(0, emptyArray!!.numberOfDecksByLevel("Intermediate"))
                assertEquals(0, emptyArray!!.numberOfDecksByLevel("Advanced"))
                assertEquals(0, emptyArray!!.numberOfDecksByLevel("Proficient"))
                assertTrue(emptyArray!!.listDecksByLevelNotEmpty("Beginner").contains("No decks with flashcards with this level stored", true))
                assertTrue(emptyArray!!.listDecksByLevelNotEmpty("Intermediate").contains("No decks with flashcards with this level stored", true))
                assertTrue(emptyArray!!.listDecksByLevelNotEmpty("Advanced").contains("No decks with flashcards with this level stored", true))
                assertTrue(emptyArray!!.listDecksByLevelNotEmpty("Proficient").contains("No decks with flashcards with this level stored", true))
            }
        }

        @Nested
        inner class NeverPlayed {
            @Test
            fun `listNeverPlayedDecks returns correct decks when array is populated`() {
                assertEquals(1, populatedArray!!.numberOfDecksNeverPlayed())

                assertTrue(populatedArray!!.listNeverPlayedDecks().contains("School Vocabulary", true)) // not empty date null
                assertFalse(populatedArray!!.listNeverPlayedDecks().contains("Trip and Adventure", true)) // empty
                assertFalse(populatedArray!!.listNeverPlayedDecks().contains("French Business Terms", true)) // empty
                assertFalse(populatedArray!!.listNeverPlayedDecks().contains("Mixed Feelings", true)) // not empty date not null
                assertFalse(populatedArray!!.listNeverPlayedDecks().contains("Missed Opportunities", true)) // not empty date not null
            }

            @Test
            fun `listNeverPlayedDecks returns message decks when array is populated and all decks have been played`() {
                populatedArray!!.findDeck(0)!!.lastDateAccessed = LocalDate.now()
                populatedArray!!.findDeck(1)!!.lastDateAccessed = LocalDate.now()
                populatedArray!!.findDeck(2)!!.lastDateAccessed = LocalDate.now()
                populatedArray!!.findDeck(3)!!.lastDateAccessed = LocalDate.now()
                populatedArray!!.findDeck(4)!!.lastDateAccessed = LocalDate.now()
                assertEquals(0, populatedArray!!.numberOfDecksNeverPlayed())
                assertTrue(populatedArray!!.listNeverPlayedDecks().contains("There is either no decks with flashcards that haven't already been played or no decks with flashcards at all", true))
            }

            @Test
            fun `listNeverPlayedDecks returns message decks when array is empty`() {
                assertEquals(0, emptyArray!!.numberOfDecksNeverPlayed())
                assertTrue(emptyArray!!.listNeverPlayedDecks().contains("There is either no decks with flashcards that haven't already been played or no decks with flashcards at all", true))
            }
        }

       /* @Nested
        inner class ByMostRecentlyPlayed {
            @Test
            fun `listDecksByMostRecentlyPlayed returns decks in correct order when array is populated`() {
                populatedArray!!.findDeck(4)!!.lastDateAccessed = LocalDate.now()
                populatedArray!!.findDeck(3)!!.lastDateAccessed = LocalDate.now().minusDays(1)
                populatedArray!!.findDeck(2)!!.lastDateAccessed = LocalDate.now().minusDays(2)
                populatedArray!!.findDeck(1)!!.lastDateAccessed = LocalDate.now().minusDays(3)
                populatedArray!!.findDeck(0)!!.lastDateAccessed = LocalDate.now().minusDays(4)

                val first = populatedArray!!.findDeck(4)!!.toString()
                val last = populatedArray!!.findDeck(0)!!.toString()
                assertTrue(populatedArray!!.listDecksByMostRecentlyPlayed().startsWith(first))
                assertTrue(populatedArray!!.listDecksByMostRecentlyPlayed().endsWith(last))
            }
        }*/
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
    inner class UpdateDeck {
        @Test
        fun `updating a deck that does not exist returns false`() {
            assertFalse(populatedArray!!.updateDeck(10, Deck(title = "UpdatedDeck", theme = "Professional", level = "Advanced")))
            assertFalse(populatedArray!!.updateDeck(-1, Deck(title = "UpdatedDeck", theme = "Professional", level = "Advanced")))
            assertFalse(emptyArray!!.updateDeck(1, Deck(title = "UpdatedDeck", theme = "Professional", level = "Advanced")))
        }

        @Test
        fun `updating a deck that exists returns true and updates`() {
            // check deck 4 exists and check the contents
            assertEquals(feelings, populatedArray!!.findDeck(4))
            assertEquals("Mixed Feelings", populatedArray!!.findDeck(4)!!.title)
            assertEquals("Emotions and Feelings", populatedArray!!.findDeck(4)!!.theme)
            assertEquals("Intermediate", populatedArray!!.findDeck(4)!!.level)
            assertEquals(LocalDate.now().minusDays(3), populatedArray!!.findDeck(4)!!.lastDateAccessed)
            // flashcards
            assertEquals(
                Flashcard(0, word = "Enthousiasme", meaning = "Enthusiasm", typeOfWord = "Noun", hit = "Miss", favourite = true),
                populatedArray!!.findDeck(4)?.findFlashcard(0)
            )
            assertEquals(
                Flashcard(1, word = "Déception", meaning = "Disappointment", typeOfWord = "Noun", hit = "Hit", favourite = false),
                populatedArray!!.findDeck(4)?.findFlashcard(1)
            )
            assertEquals(
                Flashcard(2, word = "Excitation", meaning = "Excitement", typeOfWord = "Noun", hit = "Miss", favourite = true),
                populatedArray!!.findDeck(4)?.findFlashcard(2)
            )
            assertEquals(
                Flashcard(3, word = "Honte", meaning = "Shame", typeOfWord = "Noun", hit = "Hit", favourite = false),
                populatedArray!!.findDeck(4)?.findFlashcard(3)
            )

            // update deck 4 with new information and ensure contents updated successfully
            assertTrue(populatedArray!!.updateDeck(4, Deck(title = "Updated Deck", theme = "Cultural and Idiomatic", level = "Beginner")))
            assertEquals("Updated Deck", populatedArray!!.findDeck(4)!!.title)
            assertEquals("Cultural and Idiomatic", populatedArray!!.findDeck(4)!!.theme)
            assertEquals("Beginner", populatedArray!!.findDeck(4)!!.level)
            // checking flashcards are still the same
            assertEquals(
                Flashcard(0, word = "Enthousiasme", meaning = "Enthusiasm", typeOfWord = "Noun", hit = "Miss", favourite = true),
                populatedArray!!.findDeck(4)?.findFlashcard(0)
            )
            assertEquals(
                Flashcard(1, word = "Déception", meaning = "Disappointment", typeOfWord = "Noun", hit = "Hit", favourite = false),
                populatedArray!!.findDeck(4)?.findFlashcard(1)
            )
            assertEquals(
                Flashcard(2, word = "Excitation", meaning = "Excitement", typeOfWord = "Noun", hit = "Miss", favourite = true),
                populatedArray!!.findDeck(4)?.findFlashcard(2)
            )
            assertEquals(
                Flashcard(3, word = "Honte", meaning = "Shame", typeOfWord = "Noun", hit = "Hit", favourite = false),
                populatedArray!!.findDeck(4)?.findFlashcard(3)
            )
        }
    }
}
