# 🩺 Janitri Timer Task App

A simple Android app using Jetpack Compose and MVVM architecture. It features a **foreground service** that emits the current time every second and displays it live on the UI. Users can also input vitals like blood pressure, weight, and baby kicks, which are stored locally using Room DB.

---

## ✨ Features

- 🕒 **Foreground Timer Service** – Emits current time every second using a background `Service`.
- 💉 **Dependency Injection (Hilt)** – Used for injecting Repository into ViewModel.
- 🧠 **MVVM Architecture** – Clean separation between UI, business logic, and data layers.
- 🧾 **Room Database** – Stores user-submitted vitals persistently.
- 📱 **Jetpack Compose UI** – Entire UI is built with modern Compose.
- ⚙️ **Reactive State Management** – Uses `State`, `StateFlow`, and `SharedFlow`.
- 🧪 **Validation & Error Handling** – Validates input before storing, shows success/error UI events.
- 📃 **LazyColumn with Stable Keys** – Efficiently renders the vitals list.

---


---

## 📸 Screenshots

> (Add screenshots here: timer running, input form, vitals history)

---

## 🛠 Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM
- **Dependency Injection**: Hilt
- **Database**: Room
- **Concurrency**: Kotlin Coroutines
- **Notifications**: Foreground service notification
- **BroadcastReceiver**: For timer communication

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Flamingo or newer
- Android SDK 33+
- Gradle 8+

### Run Locally

```bash
git clone https://github.com/gokulsidhman-cloud/janitri_task.git
cd janitri_task

