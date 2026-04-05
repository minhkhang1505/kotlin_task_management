# Home MVVM Refactor Context

## Purpose
This document stores the agreed refactor context for `HomeViewModel` so future Agent sessions can query and continue implementation consistently.

## Current Status
Completed:
- Removed duplicated local state `_currentSelectedCollectionId` from `HomeViewModel`.
- Switched to single source of truth via `HomeUiState.currentCollectionId`.
- Build validation passed:
  - `:app:compileDebugKotlin`
  - `:app:compileDebugAndroidTestKotlin`

## Why This Refactor
Goal is not only easier tests, but correct MVVM responsibility boundaries:
- ViewModel should orchestrate UI events, UI state, and UI effects.
- Domain rules should live in use cases.
- Infrastructure concerns should be abstracted from ViewModel.

## Agreed MVVM Direction For This Project

### Keep In ViewModel
- Event routing (`HomeEvent` -> action).
- UI state reduction (`HomeUiState`).
- UI effects emission (snackbar/navigation/one-shot events).

### Move Out Of ViewModel
- Business/workflow rules (bootstrap, optimistic transforms, reminder time composition).
- Infrastructure direct calls (notification scheduler, analytics tracker implementation details).

## Package-Oriented Refactor Blueprint

### 1) `ui/home`
- Keep `HomeViewModel`, `HomeUiState`, `HomeEvent`.
- Introduce `HomeEffect` (if needed) for one-shot UI side effects.
- Remove heavy business logic from `HomeViewModel` methods.

### 2) `domain/usecase/home` (new package)
Add focused use cases:
- `HomeBootstrapUseCase`
  - Check empty collection list.
  - Seed default collections.
  - Trigger sync.
- `ToggleTaskCompleteOptimisticUseCase`
  - Input: `listTabGroup`, task, current collection id.
  - Output: updated `listTabGroup`.
- `BuildReminderTimeUseCase`
  - Input: `dateMillis`, `hour`, `minute`.
  - Output: reminder millis.

### 3) `domain/port` (or `domain/service`)
Define abstractions:
- `TaskReminderPort` for scheduling/canceling reminders.
- `AnalyticsPort` for tracking events.

### 4) `data/notification`
- Implement `TaskReminderPort` using current scheduler implementation.

### 5) `data/analytics`
- Implement `AnalyticsPort` using current analytics tracker implementation.

### 6) `di`
- Bind ports to implementations.
- Inject ports into use cases (not directly into ViewModel where avoidable).

## Safe Execution Order
1. Done: single source of truth for selected collection id.
2. Extract pure transforms:
   - optimistic toggle transform.
   - reminder time composition.
3. Extract bootstrap workflow into `HomeBootstrapUseCase`.
4. Introduce ports for analytics and notification scheduling.
5. Move side-effect wiring into use case/effect layer.
6. Keep `HomeViewModel` thin: event orchestration + state/effect updates.

## Guardrails
- Preserve behavior parity during each step.
- Keep each refactor commit small and buildable.
- Run compile checks after each step:
  - `./gradlew :app:compileDebugKotlin`
  - `./gradlew :app:compileDebugAndroidTestKotlin`

## Updated Files From Step 1
- `app/src/main/java/com/nguyenminhkhang/taskmanagement/ui/home/HomeViewModel.kt`

## Notes For Future Agent Queries
When querying this context, prioritize:
1. MVVM responsibility correctness over convenience changes.
2. Minimal behavior changes per step.
3. Incremental migration to avoid broad regressions.
