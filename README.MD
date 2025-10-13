# Task Management Project

A comprehensive Android task management application built with modern Android development tools and following MVVM architecture pattern.

## 📋 Version Information

### Project Versions

| Component       | Version              |
| --------------- | -------------------- |
| **App Version** | 1.0 (versionCode: 1) |
| **Min SDK**     | 24 (Android 7.0)     |
| **Target SDK**  | 35 (Android 15)      |
| **Compile SDK** | 35                   |

### Core Dependencies

| Library                   | Version          |
| ------------------------- | ---------------- |
| **Kotlin**                | 2.1.0            |
| **Android Gradle Plugin** | 8.7.2            |
| **KSP**                   | 2.1.10-RC-1.0.29 |
| **Compose BOM**           | 2025.05.00       |
| **Java Compatibility**    | Java 11          |

### Main Libraries

| Library                | Version          | Purpose              |
| ---------------------- | ---------------- | -------------------- |
| **Jetpack Compose**    | 2025.05.00 (BOM) | Modern UI toolkit    |
| **Room**               | 2.7.1            | Local database       |
| **DataStore**          | 1.1.6            | Data storage         |
| **Hilt**               | 2.56.2           | Dependency injection |
| **Navigation Compose** | 2.9.0            | Navigation           |
| **Lifecycle Runtime**  | 2.9.0            | Lifecycle handling   |
| **Core KTX**           | 1.16.0           | Kotlin extensions    |
| **Activity Compose**   | 1.10.1           | Compose activity     |
| **Lottie Compose**     | 6.6.2            | Animations           |
| **Work Manager**       | 2.10.2           | Background tasks     |

### Firebase & Google Services

| Service                    | Version    |
| -------------------------- | ---------- |
| **Firebase BOM**           | 34.1.0     |
| **Firebase Firestore**     | (from BOM) |
| **Firebase Auth**          | (from BOM) |
| **Firebase Analytics**     | (from BOM) |
| **Google Services Plugin** | 4.4.3      |
| **Play Services Auth**     | 21.2.0     |
| **Credentials**            | 1.3.0      |
| **Google ID**              | 1.1.1      |

### Additional Libraries

| Library    | Version | Purpose       |
| ---------- | ------- | ------------- |
| **Coil**   | 2.6.0   | Image loading |
| **Gson**   | 2.11.0  | JSON parsing  |
| **Timber** | 5.0.1   | Logging       |

### Testing

| Library            | Version |
| ------------------ | ------- |
| **JUnit**          | 4.13.2  |
| **AndroidX JUnit** | 1.2.1   |
| **Espresso Core**  | 3.6.1   |

---

## 🏗️ Tech Stack & Architecture

- **Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel)
- **UI:** Jetpack Compose
- **Async:** Kotlin Coroutines & Flow
- **Local Database:** Room
- **Data Storage:** DataStore (Preferences)
- **Backend:** Firebase (Firestore, Auth, Analytics)
- **Dependency Injection:** Hilt
- **Background Tasks:** WorkManager
- **Navigation:** Navigation Compose

---

## 🗄️ Database Design

### Table: `task`

Each row in the `task` table represents a single task with a variety of attributes.

| Column Name           | Type          | Description                                                                                    |
| --------------------- | ------------- | ---------------------------------------------------------------------------------------------- |
| `id`                  | Long (PK)     | Auto-generated unique identifier for the task.                                                 |
| `title`               | String        | The content or title of the task.                                                              |
| `is_favorite`         | Boolean       | Whether the task is marked as favorite.                                                        |
| `is_completed`        | Boolean       | Whether the task is completed.                                                                 |
| `collection_id`       | Long          | Foreign key referencing the collection/group this task belongs to.                             |
| `start_date`          | Long? (Epoch) | Optional start date for the task, in milliseconds since epoch.                                 |
| `due_date`            | Long? (Epoch) | Optional due date for the task, in milliseconds since epoch.                                   |
| `reminder_time`       | Int           | Number of minutes before the event when a reminder should trigger (default: 30).               |
| `priority`            | Int           | Task priority level (customizable, default: 0).                                                |
| `repeat_every`        | Long          | Frequency value for repeating tasks (e.g., every 1 day/week/month, default: 1).                |
| `repeat_days_of_week` | Set<String>?  | Days of the week (e.g., ["Mon", "Wed"]) for repeating tasks (if applicable).                   |
| `repeat_interval`     | String?       | Unit of repetition (e.g., "day", "week", "month").                                             |
| `repeat_end_type`     | String?       | How the repetition ends: by date, by count, or never (e.g., "date", "count", "never").         |
| `repeat_end_date`     | Long? (Epoch) | End date for repetition, if applicable.                                                        |
| `repeat_end_count`    | Int           | Number of times to repeat (if repetition is count-based, default: 1).                          |
| `start_time`          | Long? (Epoch) | Optional start time for the task (milliseconds since epoch, can be used for time-based tasks). |
| `updated_at`          | Long (Epoch)  | Timestamp of the last update to the task.                                                      |
| `created_at`          | Long (Epoch)  | Timestamp when the task was created (default: current system time).                            |

### Table: `task_collection`

Each row in the `task_collection` table represents a group or collection of tasks.

| Column Name   | Type         | Description                                                                |
| ------------- | ------------ | -------------------------------------------------------------------------- |
| `id`          | Long (PK)    | Auto-generated unique identifier for the task collection.                  |
| `content`     | String       | The name or description of the task collection.                            |
| `updated_at`  | Long (Epoch) | Timestamp of the last update to the collection (milliseconds since epoch). |
| `sorted_type` | Int          | Sorting type for tasks within the collection (default: 0).                 |

---

## ✨ Features

The **Task Management** application provides the following key features:

- 🔑 **Login & Authentication**

  - Log in with Email/Username & Password
  - Google Sign-In integration
  - Create a new account

- 🔄 **Cross-Device Sync**

  - Synchronize tasks seamlessly across multiple devices
  - Powered by your Google account for secure backup and access anywhere

- 🏠 **Homepage**

  - Manage categories with **Tabs** (add/remove tabs)
  - Display task lists
  - Quickly add new tasks with the ➕ button
  - ⭐ **Favorite Tasks**: Mark and manage important tasks

- 👤 **Account Page**

  - Manage account information
  - Change theme and language
  - Delete account / Log out
  - View app version

- 🔁 **Repeat Task**

  - Create recurring tasks (daily, weekly, …)
  - Select start date, end date, or number of occurrences

- 📅 **Task Detail**

  - Add detailed descriptions for tasks
  - Select date and time
  - Customize recurrence (weekly, specific weekdays, …)
  - Add to calendar

- 🔍 **Search**
  - Search tasks by name or content
  - Display search results by category

---

## 📱 Screenshots

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

---

## 🎥 Demo Video

<!-- Add your YouTube link here -->

**YouTube Link:** [Coming Soon](#)

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 11 or later
- Android SDK with API level 35
- Google Services configuration file (`google-services.json`)

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/minhkhang1505/kotlin_task_management.git
   cd kotlin_task_management
   ```

2. **Add Firebase Configuration**

   - Download your `google-services.json` from Firebase Console
   - Place it in the `app/` directory

3. **Build the project**

   ```bash
   ./gradlew build
   ```

4. **Run on device/emulator**
   ```bash
   ./gradlew installDebug
   ```

---

## 📦 Project Structure

```
taskmanagement/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/nguyenminhkhang/taskmanagement/
│   │       │   ├── data/          # Data layer (Room, repositories)
│   │       │   ├── domain/        # Domain layer (use cases)
│   │       │   ├── ui/            # UI layer (Composables, ViewModels)
│   │       │   ├── di/            # Dependency injection modules
│   │       │   ├── notice/        # Notification handling
│   │       │   ├── App.kt         # Application class
│   │       │   └── MainActivity.kt
│   │       ├── res/               # Resources (layouts, drawables, etc.)
│   │       └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── google-services.json
├── gradle/
│   └── libs.versions.toml         # Version catalog
└── build.gradle.kts
```

---

## 🔧 Configuration

### Required Permissions

- `POST_NOTIFICATIONS` - For task reminders
- `SCHEDULE_EXACT_ALARM` - For precise alarm scheduling
- `INTERNET` - For Firebase sync

### Firebase Setup

This app requires Firebase configuration for:

- **Authentication** - User login and Google Sign-In
- **Firestore** - Cloud data synchronization
- **Analytics** - Usage tracking

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

## 👨‍💻 Author

**Nguyen Minh Khang**

- GitHub: [@minhkhang1505](https://github.com/minhkhang1505)

---

## 📞 Support

If you have any questions or need help, please open an issue in the repository.

---

**Made with ❤️ using Kotlin & Jetpack Compose**
