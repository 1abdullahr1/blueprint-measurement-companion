# Construction & Real Estate — Blueprint & Measurement Companion

An Android utility for people who work with building plans, property dimensions, rooms, and basic
construction measurements. The app follows one simple flow:

**measure → calculate → record → reference**

## What the app does

### 1. Measurement calculator
Enter length, width, and height in feet, inches, meters, or centimeters. The app shows:

- Area (needs length + width)
- Perimeter (needs length + width)
- Volume (needs length + width + height)

Results are shown in the selected unit and also summarized in feet. Example: 15 ft × 12 ft → 180 sq ft.

### 2. Property / room records
A small digital measurement notebook for properties. Each property keeps a name, address, and notes,
plus any number of rooms or spaces, for example:

| Space | Dimensions | Area |
| --- | --- | --- |
| Living Room | 15 × 12 ft | 180 sq ft |
| Bedroom 1 | 12 × 10 ft | 120 sq ft |
| Kitchen | 10 × 8 ft | 80 sq ft |

The property screen also totals the recorded floor area, so the notes stay useful as a reference later.

### 3. Blueprint / floor plan reference
Attach a blueprint, floor plan, or site sketch to a property. It is displayed alongside the room list so
the plan and its measurements stay together. Images are copied into app-private storage, and removing a
plan or property deletes the stored image file.

This is deliberately **not** a CAD tool and does not do estimating, accounting, scheduling, or 3D modeling.

## Tech stack

- Kotlin, Jetpack Compose, Material 3
- Room (SQLite) with KSP for local persistence
- Navigation Compose
- Coil for loading the attached plan image
- JUnit 4 unit tests for the measurement logic

Measurement logic lives in `app/src/main/java/com/blueprintcompanion/app/domain/Units.kt` and is pure
Kotlin, so it is unit tested without any Android dependency.

## Project structure

```
app/src/main/java/com/blueprintcompanion/app/
├── MainActivity.kt        # Navigation host and edge-to-edge setup
├── data/                  # Room entities, DAOs, database, repository
├── domain/                # Units, conversions, area/perimeter/volume math
└── ui/                    # Screens, dialogs, shared measurement controls, theme
app/src/test/java/.../domain/UnitsTest.kt   # Unit conversion and calculation tests
.github/workflows/android-ci.yml            # CI: tests, debug APK, release APK
```

## Screens

1. **Properties** – list of saved properties with a create dialog and a shortcut to the calculator.
2. **Property detail** – address/notes, floor plan panel (attach, replace, remove), room list with
   delete, and per-room editing.
3. **Measurement calculator** – quick unit-aware area, perimeter, and volume calculation.

Adding or editing a room shows a live area preview before saving, and the calculator short-circuits the
arithmetic rather than behaving like a general scientific calculator.

## Building

The authoritative build environment for this project is GitHub Actions. Dependency installation and all
builds happen in CI, not on the development machine.

CI workflow (`.github/workflows/android-ci.yml`) runs on pushes and pull requests to `main`:

1. Installs JDK 17 and the Android SDK on the runner.
2. Runs `./gradlew testDebugUnitTest`.
3. Runs `./gradlew assembleDebug`.
4. Runs `./gradlew assembleRelease`.
5. Uploads the built APKs as the `blueprint-companion-apks` artifact.

If you build locally (for example in Android Studio with the Android SDK installed), the equivalent
commands are:

```bash
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

Debug APK output: `app/build/outputs/apk/debug/app-debug.apk`.

## Toolchain

| Component | Version |
| --- | --- |
| Android Gradle Plugin | 8.7.3 |
| Gradle | 8.9 |
| Kotlin | 2.0.21 |
| Compose BOM | 2024.10.01 |
| compileSdk / targetSdk | 35 |
| minSdk | 26 |
| JDK | 17 |

## Scope notes

The MVP intentionally stays focused on property → floor plan → rooms → measurements → calculations.
Anything that pushes it toward contractor management, advanced estimating, structural engineering, or a
full CAD platform is out of scope.
