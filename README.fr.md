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
-   Decks par thème (2 fonctions : une à utiliser lorsque l'utilisateur doit sélectionner un deck à jouer répertoriant uniquement les decks remplis et une autre fonction listant également les decks vides)
-   Decks par niveau (2 fonctions : une à utiliser lorsque l'utilisateur doit sélectionner un deck pour jouer en répertoriant uniquement les decks remplis et une autre fonction listant également les decks vides)
-   Decks joués le plus récemment
-   Decks les moins récemment joués
-   Je n'ai jamais joué aux decks
-   Liste des decks par nombre de hits
-   Lister les decks par nombre d'échecs
-   Répertorier les decks par nombre moyen de tentatives le plus élevé
-   Répertorier les decks par nombre moyen de tentatives le plus bas
-   Répertorier les decks par le plus grand nombre de flashcards marquées comme favorites

6.  Générer un deck : génère un nouveau deck basé sur le nombre de cartes mémoire souhaité par l'utilisateur. L'utilisateur est invité à choisir l'une des options pour générer un deck : un deck avec des flashcards qui étaient "Miss", un deck avec des flashcards qui étaient "Hit", un deck avec des flashcards préférées ou un deck avec des Flashcards aléatoires. Après avoir joué avec un deck généré, l'utilisateur est invité à indiquer s'il souhaite enregistrer le deck dans le système.

### Essais

Des tests unitaires ont été effectués pour garantir une bonne couverture des tests.

### Persistance

La persistance a été introduite dans le projet, permettant le stockage et la récupération de données via trois méthodes de sérialisation différentes : JSON, YAML et XML. Malgré la disponibilité de trois méthodes de sérialisation, les données sont désormais stockées et chargées par défaut à l'aide de YAML. L'interface utilisateur affiche un message indiquant la réussite ou l'échec de ces opérations.

### GitHub Actions

Ce référentiel comporte 2 actions supplémentaires en plus de celles travaillées dans les laboratoires pendant les cours : une qui traduit automatiquement le fichier readme.md en français et une autre qui crée automatiquement un diagramme SVG du référentiel. Les archives générées automatiquement liées aux actions décrites sont respectivement nommées README.fr.md et flashcard-app-diagram.svg

### Dépendances Gradle

Deux dépendances supplémentaires en plus de celles utilisées dans les laboratoires ont été implémentées : une qui vérifie les mises à jour des dépendances et une qui vérifie les odeurs de code.

Étant donné que les dépendances ont été implémentées à la fin du développement du projet, en raison de contraintes de temps, il n'a pas été possible de résoudre toutes les odeurs de code. Cependant, j'ai décidé de conserver cette dépendance dans le projet et de désactiver son exécution lors de la construction du projet. Sinon, les odeurs de code restantes ne permettraient pas au projet d'être construit.

Néanmoins, il est possible d'exécuter la tâche sur IntelliJ. Il génère un document HTML mettant en avant toutes les odeurs de code présentes dans le projet.

Dans les projets futurs, mon objectif est de l'implémenter dès le début et de résoudre les odeurs de code au fur et à mesure de leur écriture.

## Auteurs du projet

Projet développé par Pedro Augusto pour le module Outils de développement logiciel avec l'aide des ressources mises à disposition par le Dr Siobhan Drohan.

## Références supplémentaires

<https://www.programiz.com/kotlin-programming/examples/remove-whitespaces#:~:text=Example%3A%20Program%20to%20Remove%20All%20Whitespaces&text=In%20the%20above%20program%2C%20we,in%20the%20string>.<https://stackoverflow.com/questions/28177370/how-to-format-localdate-to-string><https://stackoverflow.com/questions/45685026/how-can-i-get-a-random-number-in-kotlin><https://stackoverflow.com/questions/66195427/how-to-convert-setsetstring-to-setstring><https://www.baeldung.com/java-difference-map-and-flatmap><https://stackoverflow.com/questions/70538793/remote-write-access-to-repository-not-granted-fatal-unable-to-access><https://stackoverflow.com/questions/58282791/why-when-i-use-github-actions-ci-for-a-gradle-project-i-face-gradlew-permiss>[https://www.tutorialspoint.com/java/math/java_math_enumerations.htm#:~:text=The%20 java.,result%20is%20to%20be%20 calculated](https://www.tutorialspoint.com/java/math/java_math_enumerations.htm#:~:text=The%20java.,result%20is%20to%20be%20calculated).<https://www.baeldung.com/kotlin/round-numbers><https://stackoverflow.com/questions/66368396/kotlin-collections-and-objects-sum><https://stackoverflow.com/questions/75002278/how-to-sort-data-class-object-in-array-with-localdate-now-in-kotlin><https://developermemos.com/posts/random-boolean-kotlin>
