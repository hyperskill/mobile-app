# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is the Hyperskill Mobile App, a cross-platform learning application built with Kotlin Multiplatform Mobile (KMM). The app supports Android (native) and iOS (SwiftUI) platforms with shared business logic in the `shared` module.

Repository: https://github.com/hyperskill/mobile-app.git

## Architecture

### Clean Architecture + Redux Pattern
- **Redux Architecture**: State management using Redux-like pattern with Feature, Reducer, ActionDispatcher components
- **Clean Architecture**: Domain, data, and presentation layers
- **Component-based Dependency Injection**: Custom DI implementation using AppGraph and component builders
- **Multiplatform**: Shared Kotlin code with platform-specific implementations

### Key Directories

- `shared/` - Shared code module
  - `src/commonMain/kotlin/` - Shared business logic, domain models, interactors
  - `src/androidMain/kotlin/` - Android-specific implementations
  - `src/iosMain/kotlin/` - iOS-specific implementations
  - `src/commonTest/` - Shared test code
  - `src/androidUnitTest/` - Android-specific unit tests
- `androidHyperskillApp/` - Android application module (Jetpack Compose)
- `iosHyperskillApp/` - iOS application module (SwiftUI)
- `buildSrc/` - Build source code and custom Gradle configuration
- `buildsystem/` - Build system configuration (includes debug keystore)
- `config/` - Configuration files (detekt)

## Common Development Commands

### Build and Run
```bash
# Build all modules
./gradlew build

# Build Android debug
./gradlew :androidHyperskillApp:assembleDebug

# Install Android debug (requires device/emulator)
./gradlew :androidHyperskillApp:installDebug

# For iOS: Open iosHyperskillApp/iosApp.xcworkspace in Xcode or run from Android Studio with KMP plugin
```

### Testing
```bash
# Run all unit tests
./gradlew :androidHyperskillApp:testDebugUnitTest

# Run specific test class
./gradlew :androidHyperskillApp:testDebugUnitTest --tests "org.hyperskill.app.android.SimpleUnitTest"

# Run specific test method
./gradlew :androidHyperskillApp:testDebugUnitTest --tests "org.hyperskill.app.android.SimpleUnitTest.testStringContains"

# Shared module tests
./gradlew :shared:iosX64Test
```

### Code Quality
```bash
# Run Detekt static analysis (check before commit)
./gradlew detekt

# Auto-fix Detekt issues
./gradlew detektFormat

# Lint check
./gradlew lint

# KtLint formatting
./gradlew ktlintFormat

# KtLint check
./gradlew ktlintCheck

# All checks
./gradlew check
```

### Build Configuration

#### Signing Keys
- **Debug**: Uses `buildsystem/certs/debug.keystore`
- **Release**: Requires environment variables:
  - `HYPERSKILL_KEYSTORE_PATH`
  - `HYPERSKILL_RELEASE_STORE_PASSWORD`
  - `HYPERSKILL_RELEASE_KEY_ALIAS`
  - `HYPERSKILL_RELEASE_KEY_PASSWORD`

## Tech Stack

### Shared Module
- **Networking**: Ktor
- **Serialization**: Kotlinx Serialization  
- **Local Storage**: Multiplatform Settings
- **Coroutines**: Kotlinx Coroutines
- **Resources**: Moko Resources
- **Build Config**: BuildKonfig
- **Crash Reporting**: Sentry

### Android
- **UI**: Jetpack Compose
- **Navigation**: Cicerone
- **Dependency**: Manual injection via custom framework

### iOS  
- **UI**: SwiftUI
- **Image Loading**: Nuke
- **SVG Support**: SVGKit

## Redux Architecture Pattern

The app follows a Redux-like architecture with these key components:

### Feature Components Structure
1. **Feature**: Defines State, Messages (events), and Actions (side effects)
2. **Reducer**: Handles state transitions based on incoming messages
3. **ActionDispatcher**: Handles side effects and produces new messages using coroutines
4. **ViewModel**: Adapts the feature for UI layer consumption
5. **ViewState**: UI-friendly representation of state
6. **ViewStateMapper**: Maps internal state to view state

### Creating a New Feature
```kotlin
object MyFeature {
    sealed interface State {
        data object Idle : State
        data object Loading : State
        data class Content(val data: MyData) : State
    }
    
    fun initialState() = State.Idle
    
    sealed interface Message {
        data object Initialize : Message
        sealed interface InternalMessage : Message {
            data class FetchDataSuccess(val data: MyData) : InternalMessage
        }
    }
    
    sealed interface Action {
        sealed interface ViewAction : Action {
            data object ShowError : ViewAction
        }
        sealed interface InternalAction : Action {
            data object FetchData : InternalAction
        }
    }
}
```

### Dependency Injection Pattern
- **Data Component**: Provides repositories and interactors
- **Feature Component**: Wires together Feature, ActionDispatcher, and ViewModel
- **AppGraph Integration**: Centralized dependency management

## Code Style Guidelines

- **Indentation**: 4 spaces
- **Line length**: Maximum 120 characters  
- **Naming**: camelCase for variables/methods, PascalCase for classes/interfaces
- **Testing Framework**: JUnit 4 (Android), Kotlin test (Multiplatform)
- **Test Location**: `shared/commonTest/kotlin/org/hyperskill/` for shared tests

### Test Example
```kotlin
class SimpleUnitTest {
    @Test
    fun testStringContains() {
        val testString = "Hello, Hyperskill!"
        assertTrue("String should contain 'Hyperskill'", testString.contains("Hyperskill"))
    }
}
```

## Development Notes

- **Custom ktlint rules**: Uses `eadm/ktlint-rules` for code formatting
- **Detekt**: Run before commits with `./gradlew detekt` and `./gradlew detektFormat`
- **Feature architecture**: Follow consistent Redux pattern with domain/data/presentation layers
- **Build configuration**: BuildKonfig manages cross-platform constants
- **iOS development**: Requires macOS, CocoaPods integration
- **Project modules**: Feature/utility modules (analytic, core, notifications, redux, etc.)

## Debugging & Common Issues

### Debugging
- **Android**: Use Android Studio's built-in debugger
- **iOS**: Use Xcode's debugger  
- **Logging**: Module-tagged logs for easier filtering

### Common Issues
- **Kotlin version mismatches**: Check `gradle/libs.versions.toml`
- **iOS build issues**: Ensure CocoaPods installed, run `pod install` in iOS directory
- **Module paths**: Project uses `:androidHyperskillApp` and `:shared` as main modules