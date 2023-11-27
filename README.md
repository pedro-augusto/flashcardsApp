# Flashcards Application - Final Continuous Assessment for Software Development Tools

## Background

This project is a flashcard application to help students memorise vocabulary in a foreign language. It was developed as a final project for the Software Development Tools module by Pedro Augusto Canteli (20103533).

## Features

This application is comprised of two models: deck and flashcards. A deck can be empty (with no flashcards) or have multiple flashcards. Features are the following:

1.  CRUD operations for decks
2.  CRUD operations for flashcards
3.  Search deck by title
4.  Play: This function prompts the user to choose a deck, displays flashcard meanings or words for a specified duration, and then displays the answer. After all flashcards are played, the percentage of hits is shown, and users have the option to mark any flashcard as a favorite. The deck's last date accessed is automatically updated. No empty decks can be played.
5.  List decks:

- All decks
- Empty decks
- Decks with flashcards
- Decks by theme (2 functions: one to be used when the user has to select a deck to play listing only populated decks and another function listing including empty decks too)
- Decks by level (2 functions: one to be used when the user has to select a deck to play listing only populated decks and another function listing including empty decks too)
- Most recently played decks
- Least recently played decks
- Never played decks
- List decks by number of hits
- List decks by number of misses
- List decks by highest average number of attempts
- List decks by lowest average number of attempts
- List decks by highest number of flashcards marked as favourite

Unit tests were performed to ensure good test coverage.

### Persistence 

Persistence was introduced to the project, allowing data storage and retrieval through three different serialization methods: JSON, YAML, and XML. Despite the availability of three serialization methods, data is now stored and loaded by default using YAML. The user interface displays a message indicating the success or failure of these operations.

### GitHub Actions

This repository has 2 extra actions in addition to the ones worked on in labs during class: one that automatically translates the readme.md to French and another one that automatically creates an SVG diagram of the repository. The automatically generated archives related to the actions described are respectively named README.fr.md and flashcard-app-diagram.svg

## Project Authors 

Project developed by Pedro Augusto for the Software Development Tools module with aid from the resources made available by Dr. Siobhan Drohan.

## Additional References

https://stackoverflow.com/questions/70538793/remote-write-access-to-repository-not-granted-fatal-unable-to-access
https://stackoverflow.com/questions/58282791/why-when-i-use-github-actions-ci-for-a-gradle-project-i-face-gradlew-permiss
https://www.tutorialspoint.com/java/math/java_math_enumerations.htm#:~:text=The%20java.,result%20is%20to%20be%20calculated.
https://www.baeldung.com/kotlin/round-numbers
https://stackoverflow.com/questions/66368396/kotlin-collections-and-objects-sum
https://stackoverflow.com/questions/75002278/how-to-sort-data-class-object-in-array-with-localdate-now-in-kotlin
https://developermemos.com/posts/random-boolean-kotlin
