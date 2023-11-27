# Application Flashcards - Évaluation continue finale pour les outils de développement logiciel

## Arrière-plan

Ce projet est une application flashcard pour aider les étudiants à mémoriser le vocabulaire dans une langue étrangère. Il a été développé comme projet final pour le module Outils de développement logiciel par Pedro Augusto Canteli (20103533).

## Caractéristiques

Cette application est composée de deux modèles : deck et flashcards. Un deck peut être vide (sans flashcards) ou contenir plusieurs flashcards. Les fonctionnalités sont les suivantes :

1.  Opérations CRUD pour les decks
2.  Opérations CRUD pour les flashcards
3.  Rechercher un deck par titre
4.  Jouer : cette fonction invite l'utilisateur à choisir un jeu, affiche la signification des cartes mémoire ou les mots pendant une durée spécifiée, puis affiche la réponse. Une fois que toutes les cartes mémoire ont été lues, le pourcentage de hits est affiché et les utilisateurs ont la possibilité de marquer n'importe quelle carte mémoire comme favorite. La dernière date d'accès au deck est automatiquement mise à jour. Aucun deck vide ne peut être joué.
5.  Liste des decks :

-   Tous les decks
-   Ponts vides
-   Decks avec flashcards
-   Decks by theme (2 functions: one to be used when the user has to select a deck to play listing only populated decks and another function listing including empty decks too)
-   Decks par niveau (2 fonctions : une à utiliser lorsque l'utilisateur doit sélectionner un deck pour jouer en répertoriant uniquement les decks remplis et une autre fonction listant également les decks vides)
-   Decks joués le plus récemment
-   Decks les moins récemment joués
-   Je n'ai jamais joué aux decks
-   Liste des decks par nombre de hits
-   Lister les decks par nombre d'échecs
-   Répertorier les decks par nombre moyen de tentatives le plus élevé
-   Répertorier les decks par nombre moyen de tentatives le plus bas
-   Répertorier les decks par le plus grand nombre de flashcards marquées comme favorites

Des tests unitaires ont été effectués pour garantir une bonne couverture des tests.

### Persistance

La persistance a été introduite dans le projet, permettant le stockage et la récupération de données via trois méthodes de sérialisation différentes : JSON, YAML et XML. Malgré la disponibilité de trois méthodes de sérialisation, les données sont désormais stockées et chargées par défaut à l'aide de YAML. L'interface utilisateur affiche un message indiquant la réussite ou l'échec de ces opérations.

## Auteurs du projet

Projet développé par Pedro Augusto pour le module Outils de développement logiciel avec l'aide des ressources mises à disposition par le Dr Siobhan Drohan.

## Références supplémentaires

<https://stackoverflow.com/questions/70538793/remote-write-access-to-repository-not-granted-fatal-unable-to-access><https://stackoverflow.com/questions/58282791/why-when-i-use-github-actions-ci-for-a-gradle-project-i-face-gradlew-permiss>[https://www.tutorialspoint.com/java/math/java_math_enumerations.htm#:~:text=The%20 java.,result%20is%20to%20be%20 calculated](https://www.tutorialspoint.com/java/math/java_math_enumerations.htm#:~:text=The%20java.,result%20is%20to%20be%20calculated).<https://www.baeldung.com/kotlin/round-numbers><https://stackoverflow.com/questions/66368396/kotlin-collections-and-objects-sum><https://stackoverflow.com/questions/75002278/how-to-sort-data-class-object-in-array-with-localdate-now-in-kotlin><https://developermemos.com/posts/random-boolean-kotlin>
