# Task Management Project

# Tech Stack
- Kotlin --> programming language
- Kotlin Coroutines --> asynchronous programming
- Room --> database
- DataStore --> data storage
- Hilt --> dependency injection
- MVVM --> App architecture

# Design Database

# Task Table (`TaskEntity`)

This document describes the structure and usage of the `task` table, as defined by the `TaskEntity` data class in your Kotlin-based task management application. The table is used to store tasks with various properties to support features like reminders, repeat scheduling, prioritization, and task collection grouping.

## Table: `task`

Each row in the `task` table represents a single task with a variety of attributes.

| Column Name           | Type           | Description                                                                                   |
|-----------------------|----------------|-----------------------------------------------------------------------------------------------|
| `id`                  | Long (PK)      | Auto-generated unique identifier for the task.                                                |
| `title`               | String         | The content or title of the task.                                                             |
| `is_favorite`         | Boolean        | Whether the task is marked as favorite.                                                       |
| `is_completed`        | Boolean        | Whether the task is completed.                                                                |
| `collection_id`       | Long           | Foreign key referencing the collection/group this task belongs to.                            |
| `start_date`          | Long? (Epoch)  | Optional start date for the task, in milliseconds since epoch.                                |
| `due_date`            | Long? (Epoch)  | Optional due date for the task, in milliseconds since epoch.                                  |
| `reminder_time`       | Int            | Number of minutes before the event when a reminder should trigger (default: 30).              |
| `priority`            | Int            | Task priority level (customizable, default: 0).                                               |
| `repeat_every`        | Long           | Frequency value for repeating tasks (e.g., every 1 day/week/month, default: 1).               |
| `repeat_days_of_week` | Set<String>?   | Days of the week (e.g., ["Mon", "Wed"]) for repeating tasks (if applicable).                  |
| `repeat_interval`     | String?        | Unit of repetition (e.g., "day", "week", "month").                                           |
| `repeat_end_type`     | String?        | How the repetition ends: by date, by count, or never (e.g., "date", "count", "never").        |
| `repeat_end_date`     | Long? (Epoch)  | End date for repetition, if applicable.                                                       |
| `repeat_end_count`    | Int            | Number of times to repeat (if repetition is count-based, default: 1).                         |
| `start_time`          | Long? (Epoch)  | Optional start time for the task (milliseconds since epoch, can be used for time-based tasks). |
| `updated_at`          | Long (Epoch)   | Timestamp of the last update to the task.                                                     |
| `created_at`          | Long (Epoch)   | Timestamp when the task was created (default: current system time).                           |

## Notes

- All timestamps are represented as milliseconds since the Unix epoch.
- Repetition fields (`repeat_*`) allow flexible scheduling of recurring tasks.
- Use `collection_id` to group tasks under a specific collection or project.
- `reminder_time` is relative to either `start_date` or `due_date`, depending on your application's logic.

## Usage

This table is suitable for task management applications that require:

- Task grouping (collection support)
- Completion and favorite flags
- Flexible reminders and due dates
- Advanced repeat scheduling (daily/weekly/monthly, custom days, etc.)
- Tracking of creation and update times

---

For more details, see the `TaskEntity` class definition in the codebase.

- TaskCollection
  - id: Long
  - content: String
  - updateAt: Long
