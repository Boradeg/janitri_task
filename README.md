# 🩺 Janitri Task App

This is a modular Android application developed using Kotlin, Jetpack Compose, and MVVM architecture. The app includes two major parts:

---

## 🔹 Part I: Vitals Logger

### ✅ Features

- 📋 **Main Screen**: Displays a list of vitals logs using `LazyColumn`.
- ➕ **Add Vitals Dialog**: A floating action button opens a Compose Dialog for data entry.
- 🩸 **Vitals Entry Form** includes:
  - Blood Pressure (Sys/Dia)
  - Heart Rate
  - Weight
  - Baby Kicks Count
- 💾 **Room Database**: Saves vitals locally with immediate UI updates.
- 🔄 **Live Updates**: List auto-refreshes using `StateFlow` (or `LiveData`) when new vitals are added.
- ✅ **Validation**: All input fields are validated before saving.
- 🧹 **Error Handling**: Proper error feedback shown for missing or invalid inputs.

---

## 🔹 Part II: Background Timer Service

### ✅ Features

- 🕒 **Start Timer Button** on the main screen.
- 🛠️ **Foreground Service** emits the current time every second.
- 📡 **BroadcastReceiver** receives and displays the time live on the UI.
- 💡 **Always-On Timer**:
  - Continues running in background
  - Survives app kill or removal from recent apps
- 🧼 **Clean Communication**:
  - No static/global variables
  - Uses proper broadcast mechanism for updates
- 📱 **UI Updates**: Live time shown in Compose UI.
- ✅ Handles:
  - Re-opening the app after it's killed
  - Preventing memory leaks or multiple timers

---

## 🧠 Architecture

- **MVVM** – Modular and testable codebase.
- **Jetpack Compose** – Declarative UI using modern best practices.
- **Room** – Local storage of vitals with entity and DAO.
- **StateFlow** – Reactive state handling.
- **Hilt** – Dependency injection.
- **Service + BroadcastReceiver** – Clean background communication.

---

---

## 📸 Screenshots

<p float="left">
  <img src="https://github.com/user-attachments/assets/cb965282-7ad2-445e-b24e-1d3d27c2d2d3" width="30%" />
  <img src="https://github.com/user-attachments/assets/159f3c44-960c-4309-86f6-20a9476e5b67" width="30%" />
    <img src="https://github.com/user-attachments/assets/d70bf8b2-8339-453d-b296-751460b5cff8" width="30%" />
  ![shared image]()
![shared image (2)]()
![shared image (1)]()

</p>
---

## 🛠 Tech Stack

| Component       | Tech Used            |
|----------------|----------------------|
| Language        | Kotlin               |
| UI              | Jetpack Compose      |
| Architecture    | MVVM                 |
| DI              | Hilt                 |
| DB              | Room                 |
| Background Task | Foreground Service   |
| State Mgmt      | StateFlow / LiveData |
| Threading       | Coroutines           |
| UI Elements     | LazyColumn, Dialog   |

---

## 🚀 Getting Started

### ✅ Prerequisites
- Android Studio Hedgehog or later
- Android SDK 33+
- Gradle 8.0+

### 📦 Clone the project

```bash
git clone https://github.com/Boradeg/janitri_task.git
cd janitri_task
3. Run the app on an emulator or device.

