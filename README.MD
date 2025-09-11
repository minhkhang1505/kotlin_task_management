# Task Management Project

# Tech Stack
- Kotlin --> programming language
- Kotlin Coroutines --> asynchronous programming
- Room --> database
- DataStore --> data storage
- Hilt --> dependency injection
- MVVM --> App architecture

# Design Database
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

## Usage

This table is suitable for task management applications that require:

- Task grouping (collection support)
- Completion and favorite flags
- Flexible reminders and due dates
- Advanced repeat scheduling (daily/weekly/monthly, custom days, etc.)
- Tracking of creation and update times

## Table: `task_collection`

Each row in the `task_collection` table represents a group or collection of tasks.

| Column Name      | Type        | Description                                                                 |
|------------------|-------------|-----------------------------------------------------------------------------|
| `id`             | Long (PK)   | Auto-generated unique identifier for the task collection.                   |
| `content`        | String      | The name or description of the task collection.                             |
| `updated_at`     | Long (Epoch)| Timestamp of the last update to the collection (milliseconds since epoch).  |
| `sorted_type`    | Int         | Sorting type for tasks within the collection (default: 0).                  |

# UI

<div align="center">
  <img src="https://drive.google.com/uc?export=view&id=1XPuL8_TvAF_XJbKEuzxXQfT7QXbDa1mx" width="200" />
  <img src="https://drive.google.com/uc?export=view&id=1EScXS5_966sGpjFrlkS3iKDGXEEx2A9J" width="200" />
  <img src="https://drive.google.com/uc?export=view&id=1qUE7n78o1S7RxE5T4o31WL0F2Qp8X3jv" width="200" />
  <img src="https://drive.google.com/uc?export=view&id=1pAESJ88LHt6aTsDJkKnKQfqEhV1uoW5Q" width="200" />
</div>

<div align="center">
  <img src="https://drive.google.com/uc?export=view&id=11vDu6YmYpP8hnc-wItROc_hwBBhN-ywb" width="200" />
  <img src="https://drive.google.com/uc?export=view&id=1tor0YdBtQPPQGOCqWDULJrnOGgJBNDtH" width="200" />
  <img src="https://drive.google.com/uc?export=view&id=1YIVrrKFYvnnzi-AXoRAptEpkh9zZCdrj" width="200" />
  <img src="https://drive.google.com/uc?export=view&id=18X0DLtZYD3srORuGtdAzk17hRzrqTjNZ" width="200" />
</div>

<div align="center">
  <img src="https://drive.google.com/uc?export=view&id=1baHkq592Wo4RZEePgwcV4SwDGUaEOMyD" width="200" />
</div>

