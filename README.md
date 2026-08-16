# BUPOLY Mobile Portal

Android mobile portal for **Binyaminu Usman Polytechnic (BUPOLY)**, integrating the existing SRMS while providing a native-style mobile navigation layer.

## One-click cloud build — no Android Studio required

This project is prepared for **GitHub Actions**. GitHub builds the Android app in the cloud and makes the APK/AAB available as downloadable workflow artifacts.

### First time setup

1. Create a new GitHub repository.
2. Upload **all files in this folder** to the repository.
3. Open the repository on GitHub and go to **Actions**.
4. Select **Build BUPOLY Android App**.
5. Click **Run workflow**.
6. Choose:
   - **apk** — easiest option for installing on an Android phone.
   - **aab** — Android App Bundle for Google Play preparation.
   - **both** — builds both files.
7. Open the completed workflow run and download the artifact under **Artifacts**.

For a normal push to the `main` branch, the workflow automatically builds the debug APK.

## Important note about the AAB

The generated release AAB is **not Play Store-signed**. For Google Play publishing, the app must later be signed with the Polytechnic's release keystore and configured with the official application identity.

## Project structure

```text
BUPOLY_Mobile_Portal/
├── .github/workflows/build-android.yml
├── app/
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## Current application architecture

- Home Dashboard
- Student Services
- Student SRMS
- Results
- Registration
- Fees & Payments entry point
- Student ID entry point
- News & Announcements entry point
- Admissions
- Academic Departments
- Campus Directory
- Notifications entry point
- Help & Contact
- Official Website

The authenticated student-record functions continue to use the existing SRMS backend: `https://bupoly.safsrms.com/`.
