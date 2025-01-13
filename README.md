# CrossDocs 

[![Kotlin](https://img.shields.io/badge/kotlin-1.9.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.5.1-green.svg?logo=jetpackcompose)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![License](https://img.shields.io/badge/License-Apache%202.0-orange.svg)](LICENSE)
[![Release](https://img.shields.io/badge/release-v1.0.0-blue.svg)](https://github.com/yadnyeshkolte/CrossDocs/releases/tag/v1.0.0)

> A powerful and lightweight cross-platform Markdown editor with real-time preview and AI assistance, built with Compose Multiplatform.


The vision behind CrossDocs is to make documentation a joy rather than a chore. By bringing together the power of real-time preview, AI assistance, and cross-platform compatibility, it's a tool that adapts to your workflow, not the other way around. Whether you're creating technical documentation, taking notes, or writing a blog post, CrossDocs gives you a distraction-free environment to focus on what matters most: your content.

What makes CrossDocs special is an understanding of how modern development flows work. This application is made with Compose Multiplatform. It has native performance on any platform while also providing a smooth user experience across all of them. The integration of Google's Gemini AI is no feature checkbox-it's a considered implementation that assists you in writing better, faster, and with more accuracy.

## ✨ Features

- ⚡ **Real-time Markdown Preview** - See your changes instantly
- 🎨 **Dark/Light Theme** - Easy on your eyes, day and night
- 🤖 **AI Assistant** - Powered by Google's Gemini AI
- 📱 **Cross-Platform** - Works on all major platforms
- 🪶 **Lightweight & Efficient** - High performance, low resource usage
- 📖 **Markdown Guide** - Detailed Markdown reference for developers

#### 📝 MarkDown Editor

- Instantly see your Markdown content rendered as you type.
- User-Friendly Interface with Clean and intuitive design for a seamless writing experience.

#### 🎨 Theme Switching

- Click the refresh icon in the top bar to toggle between light and dark themes
- Theme preference is automatically saved

#### 🤖 AI Assistant Usage

1. Write your Markdown content in the left panel
2. Use the chat section below for AI assistance:
   - Ask for writing suggestions
   - Request formatting help
   - Get content ideas

#### 📖 In-App Markdown Guide
- Access the Markdown Guide directly from the menu bar

#### 📤 Export Document
- Coming Soon
     
---

##### Android
![Smartphone Screen](https://github.com/yadnyeshkolte/CrossDocs/blob/main/resources/android%20app%20screens.png?raw=true)

##### Desktop
![Desktop Screen](https://github.com/yadnyeshkolte/CrossDocs/blob/main/resources/desktop%20screen.png?raw=true)

#### Android Recording

![Android View](resources/android-view.mp4)

#### Desktop Recording

![Desktop Video](resources/Desktop.webm)

---

## 📥 Download & Installation

### Available Packages

🖥️ **Desktop Applications**:
- **Windows**: [`CrossDocs-1.0.0.msi`](https://github.com/yadnyeshkolte/CrossDocs/releases/download/v1.0.0/CrossDocs-1.0.0.msi) (58.21 MB)
- **macOS**: [`CrossDocs-1.0.0.dmg`](https://github.com/yadnyeshkolte/CrossDocs/releases/download/v1.0.0/CrossDocs-1.0.0.dmg) (59.86 MB)
- **Linux**:
  - Debian/Ubuntu: [`CrossDocs_1.0.0_amd64.deb`](https://github.com/yadnyeshkolte/CrossDocs/releases/download/v1.0.0/CrossDocs_1.0.0_amd64.deb) (52.51 MB)
  - Universal Linux: [`CrossDocs-1.0.0-x86_64.AppImage`](https://github.com/yadnyeshkolte/CrossDocs/releases/download/v1.0.0/CrossDocs-1.0.0-x86_64.AppImage) (62.72 MB)

📱 **Mobile**:
- **Android**: [`CrossDocs.apk`](https://github.com/yadnyeshkolte/CrossDocs/releases/download/v1.0.0/CrossDocs.apk) (9.40 MB)

### ⚠️ System Requirements

**Desktop Applications (Windows, macOS, Linux)**:
- JDK 23.0.1 or later is required
- 100 MB of free disk space

**Android**:
- Android 5.0 (API level 21) or higher
- No additional setup required

### Installation Instructions

1. **Android**:
   - Download the APK file
   - Allow installation from unknown sources in your device settings
   - Open the downloaded APK to install

2. **Windows**:
   - Ensure JDK 23.0.1 is installed
   - Download the `.msi` file
   - Double-click to run the installer
   - Follow the installation wizard

3. **macOS**:
   - Ensure JDK 23.0.1 is installed
   - Download the `.dmg` file
   - Open the DMG file
   - Drag CrossDocs to Applications folder

4. **Linux**:
   - Ensure JDK 23.0.1 is installed
   
   For Debian/Ubuntu:
   ```bash
   sudo dpkg -i CrossDocs_1.0.0_amd64.deb
   ```
   
   For AppImage:
   ```bash
   chmod +x CrossDocs-1.0.0-x86_64.AppImage
   ./CrossDocs-1.0.0-x86_64.AppImage
   ```

---

## 📚 Libraries and Dependencies

| Category | Library | Version | Purpose |
|----------|---------|---------|---------|
| **Core** | Kotlin | 1.9.0 | Programming Language |
| | Compose Multiplatform | 1.5.1 | UI Framework |
| | Kotlin Coroutines | 1.7.3 | Asynchronous Programming |
| **Markdown Processing** | JetBrains Markdown | 0.3.5 | Markdown Parsing |
| **Networking** | Ktor Client | 2.3.7 | HTTP Client |
| | Ktor Client CIO | 2.3.7 | Ktor Engine |
| | Ktor Client Content Negotiation | 2.3.7 | Content Negotiation |
| **Serialization** | Kotlinx Serialization | 1.6.0 | JSON Serialization |
| **Logging** | SLF4J Simple | 2.0.9 | Logging Framework |
| **Android Specific** | AndroidX Activity Compose | Latest | Android Integration |
| | AndroidX Lifecycle ViewModel | Latest | ViewModel Support |
| | AndroidX Lifecycle Runtime | Latest | Lifecycle Management |
| **Desktop Specific** | Kotlinx Coroutines Swing | Latest | Desktop Threading |

---

## 📃 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
