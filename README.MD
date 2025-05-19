# Task Management Project

# Tech Stack
- Kotlin --> programming language
- Kotlin Coroutines --> asynchronous programming
- Room --> database
- DataStore --> data storage
- Hilt --> dependency injection
- MVVM --> App architecture

# Design Database
- Task
  - id: Long
  - content: String
  - isCompleted: Boolean
  - isFavorite: Long
  - collectionId: Int
  - updatedAt: Long
- TaskCollection
  - id: Long
  - content: String
  - updateAt: Long