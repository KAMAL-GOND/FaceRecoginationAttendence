# FaceRecognitionAttendance

## Overview
FaceRecognitionAttendance is an Android application designed to streamline the attendance marking process using on-device face recognition technology. This project leverages the power of Machine Learning (ML Kit) to perform efficient and accurate facial detection and recognition directly on the mobile device, ensuring privacy and fast processing without relying on cloud services.

## Features

### 1. On-Device AI & Machine Learning Core
The heart of this application lies in its on-device AI capabilities. By processing facial data locally, the app ensures quick response times, reduces data transfer costs, and enhances user privacy as sensitive biometric data never leaves the device.

### 2. ML Kit Integration
-   **Face Detection:** Utilizes Google's ML Kit for robust and high-performance face detection. This allows the application to accurately locate faces within an image or camera stream, even under varying conditions (e.g., different lighting, angles).
-   **Face Embeddings:** Beyond simple detection, ML Kit is used to generate unique "embeddings" (vector representations) for each detected face. These vectors capture the distinctive features of a face, transforming complex image data into a numerical format suitable for comparison.

### 3. Euclidean Distance for Face Matching
-   **Vector Comparison:** Once a face's embedding (vector) is generated, it is compared against a database of registered student face embeddings.
-   **Euclidean Distance:** The comparison is performed using Euclidean distance, a metric that calculates the "straight-line" distance between two points (vectors) in a multi-dimensional space. A smaller Euclidean distance indicates a higher similarity between two faces. This allows for reliable identification of students.

### 4. Persistent Data Storage with Room Database
-   **Local Storage:** All student data, including their names, roll numbers, class information, and most importantly, their face embeddings, are securely stored locally using the Room Persistence Library.
-   **Efficient Data Management:** Room provides an abstraction layer over SQLite, making database interactions simpler, more robust, and highly efficient.

### 5. Seamless Attendance Marking
-   Users can quickly mark attendance by capturing a student's face.
-   The app intelligently identifies the student and records their presence for the designated class.

## How it Works (ML/AI Workflow)

1.  **Student Enrollment:** When a new student is enrolled, an image of their face is captured.
2.  **Embedding Generation:** ML Kit processes this image to generate a unique high-dimensional vector (face embedding) representing the student's facial features.
3.  **Database Storage:** This face embedding, along with other student details, is stored in the local Room database.
4.  **Attendance Marking:**
    *   A student's face is captured via the camera.
    *   ML Kit detects the face and generates a real-time embedding.
    *   This real-time embedding is compared against all stored embeddings in the Room database using Euclidean distance.
    *   If a match is found within a predefined threshold, the student is identified, and their attendance is marked.

## Installation

### Option 1: Download from GitHub Releases (For End-Users)

1.  Navigate to the [Releases section](https://github.com/KAMAL-GOND/FaceRecoginationAttendence/releases/tag/version1) of this GitHub repository. 
2.  Download the latest `app-release.apk` file.
3.  On your Android device, you might need to enable installation from "Unknown Sources" in your device's security settings.
4.  Locate the downloaded APK file and tap it to install the application.
5. OR You can download directly from here **https://github.com/KAMAL-GOND/FaceRecoginationAttendence/releases/download/version1/app-release.apk**

### Option 2: Build from Source (For Developers)

To set up the development environment and build the application from its source code:

#### Prerequisites
*   **Android Studio:** Ensure you have the latest stable version of Android Studio installed. It includes the Android SDK, build tools, and an integrated development environment.
    *   Download from: [developer.android.com/studio](https://developer.android.com/studio)
*   **Git:** You'll need Git to clone the repository.
    *   Download from: [git-scm.com/downloads](https://git-scm.com/downloads)

#### Steps

1.  **Clone the Repository:**
    Open your terminal or Git Bash and run the following command to clone the project:
    ```bash
    git clone https://github.com/KAMAL-GOND/FaceRecoginationAttendence.git
    cd FaceRecoginationAttendence
    ```


2.  **Open in Android Studio:**
    *   Launch Android Studio.
    *   Select "Open an existing Android Studio project".
    *   Navigate to the `FaceRecoginationAttendence` directory you just cloned and click "OK".

3.  **Install ADB (Android Debug Bridge) via Android Studio:**
    ADB is essential for installing and debugging apps on devices or emulators. Android Studio typically installs it with the SDK.
    *   Go to `File > Project Structure > SDK Location`.
    *   Ensure the Android SDK is installed. ADB executables are usually found in `sdk/platform-tools/`. Android Studio handles this automatically during SDK setup.

4.  **Sync Gradle Project:**
    Android Studio will automatically try to sync the project with Gradle. If not, click the "Sync Project with Gradle Files" button (looks like an elephant with a down arrow) in the toolbar.

5.  **Run the Application:**
    *   Connect an Android device via USB (enable USB debugging in Developer Options) or start an Android Emulator.
    *   Click the "Run 'app'" button (green play icon) in the toolbar. Android Studio will build and install the application on your selected device/emulator.

## Usage
Once installed, launch the app. You can then:
*   Add new classes and students.
*   Capture student photos to generate and store their face embeddings.
*   Use the attendance marking screen to detect and identify students for attendance.



## License
This project is licensed under the MIT License 
