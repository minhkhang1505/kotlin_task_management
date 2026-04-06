# KMP Migration & Refactoring Plan

## 1. Readiness Score: 3/10

**Explanation:**
While the project claims to use MVVM and Clean Architecture, it violates fundamental rules of Dependency Inversion. The `domain` layer heavily depends on the `data` layer (e.g., Room entities like `TaskEntity` inside `TaskRepository`) and Android/Firebase framework classes (e.g., `FirebaseUser` inside `AuthRepository`). ViewModels are extremely bloated (e.g., `HomeViewModel` is ~500 lines) and directly manipulate data structures, perform date calculations with `java.util.Calendar`, and run Android-specific schedulers. Significant refactoring to achieve "pure" domain and data abstractions is required before KMP tools can even compile the common code.

---

## 2. Blocking Issues

* **Leaking Framework Dependencies into Domain:** `AuthRepository.kt` exposes `com.google.firebase.auth.FirebaseUser`. The `domain` layer must be 100% platform-agnostic Kotlin.
* **Leaking Database Entities into Domain & UI:** `TaskRepository.kt` returns Room database entities (`TaskEntity`, `TaskCollection`), which forces domain and UI layers to depend on Android Room annotations.
* **DataStore & UI Models in Domain:** `SettingsRepository.kt` depends on `SettingsPreferenceData` (Data layer) and `LanguageOption` (UI layer Enum).
* **Platform-Specific Time Logic:** Usage of `java.util.Calendar` directly inside ViewModels (e.g., `HomeViewModel.combineDateAndTime`).
* **Direct Notification Scheduling in ViewModels:** `TaskScheduler` is passed into `HomeViewModel` and invoked directly, firmly coupling UI behavior to Android's notification and alarm manager systems.

---

## 3. Architecture Problems

* **Fat ViewModels:** `HomeViewModel` has mixed responsibilities. It controls UI state, manipulates entity data, invokes analytics, scheduling, and orchestrates task saving logic. 
* **Mixed Responsibilities:** Logic that belongs in UseCases (e.g., calendar math, optimistic UI updates, scheduling logic) is baked directly into the ViewModels.
* **Non-testable Code:** Static hidden dependencies (like `Calendar.getInstance()`) make time-based operations untestable. The heavy orchestration in ViewModels requires massive mocking setups for unit testing.
* **Hidden Side Effects:** `TaskScheduler.schedule()` and analytics tracking run as hidden side effects interleaved with complex state manipulations preventing predictable state pipelines.

---

## 4. KMP Migration Mapping

| Layer | Move to `commonMain` | Stay in `androidMain` | Needs abstraction |
| ----- | ------------------ | ------------------- | ----------------- |
| **Domain Models** | Yes | No | **Needs pure Kotlin models completely independent of Room/Firebase.** |
| **Repositories (Interfaces)** | Yes | No | **Clean domain interfaces that return pure Kotlin models.** |
| **UseCases** | Yes | No | **Yes, logic must be decoupled from `java.util.Calendar`.** |
| **Room Database** | No (mostly) | Yes | **Needs migration to Room KMP or SQLDelight for shared persistence.** |
| **Firebase Auth/Firestore** | No (unless mapped) | Yes | **Needs abstraction via interfaces OR migrate to `gitlive Firebase KMP SDK`.** |
| **ViewModels**| Maybe (MVI/KMP) | Yes (for now) | **Keep in Android for UI, extract core logic to `commonMain` UseCases.** |
| **UI (Compose)** | No | Yes | **Android UI stays native; iOS uses SwiftUI natively.** |
| **Notification Scheduler**| No | Yes | **Needs `expect/actual` or standard interface implementation.** |

---

## 5. Refactoring & Migration Roadmap (Phases)

* **Phase 1: Pre-refactor (Safe changes)**
  * Purify Domain Layer (Replace Data entities with pure Kotlin data classes).
  * Introduce Mapper classes between Data and Domain.
  * Extract formatting and time logic from ViewModels into pure domain structures.
* **Phase 2: Prepare Shared Module**
  * Create the `shared` KMP module structure (`commonMain`, `androidMain`, `iosMain`).
  * Define `expect/actual` declarations for platform APIs (e.g., Logger, UUID generator).
  * Setup dependency injection (e.g., Koin) for the KMP module to replace Dagger Hilt in common code.
* **Phase 3: Extract Domain & Data**
  * Move pure Domain classes (Models, Repository Interfaces, UseCases) to `commonMain`.
  * Migrate `java.util.*` to `kotlinx-datetime`.
  * Evaluate/Migrate data sources (either abstract behind Android interfaces or implement Room KMP/Firebase KMP SDK).
* **Phase 4: iOS Readiness**
  * Export KMP framework for iOS.
  * Build Swift wrappers/Adapters for asynchronous flows (Flow/StateFlow to Combine/AsyncSequence).
  * Implement native SwiftUI views consuming the shared UseCases/Presenters.

---

## 6. Task Breakdown Format (GitHub-ready)

### Phase 1: Pre-Refactor

**Title:** Purify Domain Models (Task and Collection)
**Description:** 
* Create pure Kotlin data classes for `Task` and `TaskCollection` in the domain layer. 
* Add mapping extension functions in the data layer to convert Room `TaskEntity` > `Task`.
* Why: KMP `commonMain` cannot depend on Android Room annotations or framework DB classes.
**Scope:** `domain/model`, `data/local/database/entity`, `domain/repository/TaskRepository.kt`
**Acceptance Criteria:** `TaskRepository` interface returns and accepts pure domain models. Zero Room imports in `domain`.
**Priority:** High

**Title:** Purify Auth Domain Models
**Description:** 
* Remove `com.google.firebase.auth.FirebaseUser` from `AuthRepository.kt` and replace it with a custom `User` domain model.
* Map FirebaseUser -> User in the repository implementation.
* Why: KMP `commonMain` cannot depend on Android Firebase SDK classes.
**Scope:** `domain/repository/AuthRepository.kt`, `data/remote/AuthRepositoryImpl.kt`
**Acceptance Criteria:** Zero Firebase imports in the `domain` module.
**Priority:** High

**Title:** Decouple Settings from Data and UI
**Description:** 
* Create abstract domain settings models to replace `SettingsPreferenceData` and `LanguageOption` inside `SettingsRepository.kt`.
* Why: Clean architecture mandates domain shouldn't know about UI enums or Data-store models.
**Scope:** `domain/repository/SettingsRepository.kt`
**Acceptance Criteria:** `SettingsRepository` only refs pure domain models.
**Priority:** High

**Title:** Extract Time-Based Logic from ViewModels
**Description:**
* Remove direct usage of `Calendar.getInstance()` inside `HomeViewModel`.
* Introduce an interface abstraction `TimeProvider` or immediately migrate to `kotlinx-datetime`.
* Why: `java.util.Calendar` does not exist in Kotlin Multiplatform `commonMain`. 
**Scope:** `ui/home/HomeViewModel.kt`, Time utility classes.
**Acceptance Criteria:** No `java.util.Calendar` or `java.util.Date` calls exist inside the ViewModel layer.
**Priority:** Medium


### Phase 2: Prepare Shared Module

**Title:** Scaffold KMP Gradle Project Structure
**Description:**
* Create a new `shared` module with `commonMain`, `androidMain`, and `iosMain` source sets. Add KMP Gradle plugin.
* Why: To provide a build sandbox for migrating the purified business logic into KMP tools.
**Scope:** Root `build.gradle.kts`, `settings.gradle.kts`, new `shared` folder.
**Acceptance Criteria:** Project syncs successfully and android app can link the shared module as a dependency.
**Priority:** High

**Title:** Migrate Dependency Injection to Koin (Optional but Recommended)
**Description:**
* Evaluate and transition from Hilt to Koin (or map bindings manually at the boundary). Let Koin handle UseCase/Repository DI in commonMain.
* Why: Dagger Hilt is Android-only and cannot be used inside `commonMain`.
**Scope:** `di` module, UseCases, Repositories.
**Acceptance Criteria:** Core domain and repository injection is handled in a multiplatform-friendly way.
**Priority:** Medium


### Phase 3: Extract Domain & Data

**Title:** Move Domain Layer to commonMain
**Description:**
* Physically move `model`, `repository` (interfaces), and `usecase` folders out of the Android `app` module into `shared/commonMain`.
* Why: Enables code reuse across iOS and Android natively.
**Scope:** `app/src/main/java/.../domain/*` -> `shared/src/commonMain/kotlin/.../domain/*`
**Acceptance Criteria:** `commonMain` compiles without Android dependencies and houses all domain logic.
**Priority:** High

**Title:** Abstract Local Storage Layer (Room)
**Description:**
* Implement the data layer for `TaskRepository` using `expect/actual`, or refactor the Android application to pass native Android implementations via interfaces into the shared module. Alternatively, migrate to Room KMP version 2.6+.
* Why: The common iOS/Android entry points need a centralized way to access local hardware persistence.
**Scope:** `app/src/main/.../data/local/*` vs `shared/src/commonMain/...`
**Acceptance Criteria:** The shared UseCases can successfully invoke Room queries on Android without direct JVM dependencies.
**Priority:** High

### Phase 4: iOS Readiness

**Title:** Expose KMP Shared Framework for iOS
**Description:**
* Configure the Xcode project properties and `packForXcode` Gradle tasks. 
* Add `SKIE` or standard wrappers to export StateFlow and coroutines cleanly to iOS Swift ecosystem.
* Why: SwiftUI requires Combine or Async/Await equivalents to bind easily.
**Scope:** `shared/build.gradle.kts`, SwiftUI Xcode Project structure.
**Acceptance Criteria:** iOS dummy app compiles, links `shared.framework`, and can invoke a sync UseCase returning data.
**Priority:** High

---

## 7. GitHub Integration Suggestion

**Epic:** 
* `KMP Migration & Architecture Standardization`

**Milestones:**
* `Milestone 1: Architectural Purity (Decoupling)` -> *Map to Phase 1*
* `Milestone 2: Multiplatform Setup (Infrastructure)` -> *Map to Phase 2*
* `Milestone 3: Logic Extraction (commonMain)` -> *Map to Phase 3*
* `Milestone 4: Native iOS Integration (SwiftUI)` -> *Map to Phase 4*

**Issue Grouping:**
Group related UseCase migrations into single PRs (e.g., `Issue: Extact Settings UseCases`, `Issue: Purify Task Entities`).

---

## 8. Testability Evaluation

* **UseCase Testability:** Currently poor. ViewModels are orchestrating everything. Moving rigid logic from ViewModels into pure domain UseCases will allow massive performance in Unit Tests via simple Junit setups without needing `Robolectric` or `InstantTaskExecutorRule`.
* **Repository Abstraction:** Once entities are purified (Phase 1), repositories can be easily faked using in-memory HashMaps for UseCase tests. 
* **Business Logic Purity:** 
  **Tasks:** 
  1. Introduce a `TimeProvider` interface to mock time during testing.
  2. Implement an `AnalyticsTracker` mock interface to avoid testing side-effects in logic tests.
  3. Lift optimistic-UI update logic out from ViewModel and into a testable "Intent-State-Reducer" pattern or discrete UseCase.
  
---

## 9. Risk Analysis

* **Data Sync (Room ↔ Firebase):** Keeping two distinct local sources of truth synchronized is hard. Porting this complexity to KMP might require a robust conflict resolution sync engine since network conditions on iOS and Android execute differently. 
* **Coroutines / Flow in iOS:** Swift's concurrency model handles memory and threading differently than Kotlin Coroutines. Using `StateFlow` from KMP on SwiftUI can lead to lifecycle memory leaks unless using tools like `SKIE` or `KMP-NativeCoroutines`.
* **Platform Differences in Date/Time:** Migrating `java.util.Calendar` to `kotlinx-datetime` will run into TZData issues. Local system timezone evaluations behave differently on Apple devices compared to JVM devices.
* **Dependency Injection limitations:** You lose Dagger Hilt for the shared module. Replacing it with Koin or Manual DI creates overhead.

---

## 10. Final Verdict
**NEEDS REFACTOR** 

The application has the skeleton of an MVVM/Clean Architecture application, but its internal organ system relies entirely on Android native framework implementations. The codebase must first be meticulously refactored to enforce strict domain purity before any Kotlin Multiplatform bridging tools are utilized.
