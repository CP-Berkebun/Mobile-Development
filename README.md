# Berkebun+ Mobile Development 🌱

This repository contains the mobile development for Berkebun+ Capstone Project.

Berkebun+ mobile application is used to interact with users by predicting the plant diseases by scanning plant leaf using camera or input an images from gallery and then the results of diagnoses will shows up and the users could save it.

## 🎯 Feature

<ul>
    <li>User Login</li>
    <li>User Registration</li>
    <li>Plant Diagnosis</li>
    <li>Weather Information</li>
    <li>Save Diagnosis</li>
    <li>User Profile</li>
</ul>

## 🛠️ Technologies used

This project leverages the following technologies:

<ul> 
    <li> <strong>Git & GitHub:</strong> Version control system for collaborative development and code management. </li> 
    <li> <strong>Figma:</strong> Design tool for creating wireframes and prototypes of the mobile application. </li> 
    <li> <strong>Discord:</strong> Communication platform for team collaboration and discussions. </li> 
    <li> <strong>Android Studio:</strong> Integrated Development Environment (IDE) for building and testing Android applications. </li> 
    <li> <strong>Postman:</strong> API testing tool to verify backend endpoints and request responses. </li> 
    <li> <strong>Glide:</strong> Library for efficient image loading and caching in Android apps. </li> 
    <li> <strong>Retrofit:</strong> HTTP client for handling API requests and JSON parsing. </li> 
    <li> <strong>Android KTX:</strong> Kotlin extensions to simplify Android development with concise and idiomatic code. </li> 
    <li> <strong>Jetpack Components:</strong> 
        <ul> 
            <li><strong>Room:</strong> Local database management for offline storage.</li> 
            <li><strong>ViewModel/LiveData:</strong> Implements MVVM architecture for UI and data binding.</li> 
            <li><strong>CameraX:</strong> Provides easy-to-use APIs for camera functionalities in the app.</li> 
            <li><strong>DataStore:</strong> Data persistence library for storing key-value data.</li> 
        </ul> 
    </li> 
    <li> <strong>Firebase:</strong> For user authentication </li> 
    <li> <strong>Kotlin:</strong> The programming language used for Android development, known for its concise and safe syntax. </li> 
    <li> <strong>Firestore:</strong> Saving user information </li> 
</ul>

## 🚀 Prerequisites

Before running the project locally, ensure you have the following installed and set up:

1. **Android Studio**

   - Download and install from [here](https://developer.android.com/studio).

2. **JDK (Java Development Kit)**

   - Version 17 or newer.

3. **Git**

   - Install Git from [here](https://git-scm.com/).

4. **Firebase Setup**

   - Create a project in [Firebase Console](https://console.firebase.google.com/).
   - Add your Android app and download the `google-services.json` file.

5. **Internet Connection**
   - Required for syncing Gradle and downloading dependencies.

## 💻 Running Locally

To run this project locally, follow these steps:

1. **Clone the Repository**

   ```bash
   git clone https://github.com/CP-Berkebun/Mobile-Development.git
   cd Mobile-Development
   ```

2. **Open the Project in Android Studio**

   - Open Android Studio.
   - Go to **File > Open**, and select the cloned project directory.
   - Wait for indexing and Gradle synchronization.

3. **Add Firebase Configuration**

   - Place the `google-services.json` file (downloaded from Firebase Console) in the `app/` directory of the project.

4. **Sync Gradle**

   - Click **Sync Now** in Android Studio if prompted. This ensures all dependencies are resolved correctly.

5. **Run the Application**
   - Connect an Android device via USB or set up an emulator in Android Studio.
   - Click the **Run** button (green triangle icon) to build and launch the application.

## 💡 About this repo

This mobile development was created by mobile development cohort:

<ul>
    <li>
       <a href="https://github.com/vilixvoid">Mayhesta Gilang Maulana</a>
    </li>
    <li>
        <a href="https://github.com/denidoodle12">Deni Hidayat</a>
    </li>
</ul>
