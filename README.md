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

6. Generate Deck: Generates a new deck based with the user's desired number of flashcards. The user is prompted to choose one of the options for generating a deck: deck with flashcards that were 'Miss', deck with flashcards that were 'Hit', deck with favourite flashcards or deck with random Flashcards. After playing with a generated deck, user is prompted if they would like to save the deck into the system.


### Tests

Unit tests were performed to ensure good test coverage.

### Persistence 

Persistence was introduced to the project, allowing data storage and retrieval through three different serialization methods: JSON, YAML, and XML. Despite the availability of three serialization methods, data is now stored and loaded by default using YAML. The user interface displays a message indicating the success or failure of these operations.

### GitHub Actions

This repository has 2 extra actions in addition to the ones worked on in labs during class: one that automatically translates the readme.md to French and another one that automatically creates an SVG diagram of the repository. The automatically generated archives related to the actions described are respectively named README.fr.md and flashcard-app-diagram.svg

### Gradle Dependencies

Two extra dependencies in addition to the ones used in labs were implemented: one that checks for dependency updates and detekt, that checks for code smells.

Because the dependencies were implemented at the end of the project development, due to time constraints it was not possible to solve all the code smells. However, I decided to keep this dependency in the project and disable it from being executed upon building the project. Otherwise, the remaining code smells would not allow the project to be built.

Still, it is possible to execute the task on IntelliJ. It generates an HTML document highlighting all the code smells present in the project.

In future projects, I aim to implement it from the start and solve code smells as they are written. 

## Project Authors 

Project developed by Pedro Augusto for the Software Development Tools module with aid from the resources made available by Dr. Siobhan Drohan.

## Additional References

https://www.programiz.com/kotlin-programming/examples/remove-whitespaces#:~:text=Example%3A%20Program%20to%20Remove%20All%20Whitespaces&text=In%20the%20above%20program%2C%20we,in%20the%20string.
https://stackoverflow.com/questions/28177370/how-to-format-localdate-to-string
https://stackoverflow.com/questions/45685026/how-can-i-get-a-random-number-in-kotlin
https://stackoverflow.com/questions/66195427/how-to-convert-setsetstring-to-setstring
https://www.baeldung.com/java-difference-map-and-flatmap
https://stackoverflow.com/questions/70538793/remote-write-access-to-repository-not-granted-fatal-unable-to-access
https://stackoverflow.com/questions/58282791/why-when-i-use-github-actions-ci-for-a-gradle-project-i-face-gradlew-permiss
https://www.tutorialspoint.com/java/math/java_math_enumerations.htm#:~:text=The%20java.,result%20is%20to%20be%20calculated.
https://www.baeldung.com/kotlin/round-numbers
https://stackoverflow.com/questions/66368396/kotlin-collections-and-objects-sum
https://stackoverflow.com/questions/75002278/how-to-sort-data-class-object-in-array-with-localdate-now-in-kotlin
https://developermemos.com/posts/random-boolean-kotlin
