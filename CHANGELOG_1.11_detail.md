# Changelog — QR & Barcode Scanner

## Overview

This update modernizes the project's build tooling and dependencies to keep it
building and running correctly on current Android versions and Gradle/AGP/Kotlin
toolchains. **No UI or business logic was intentionally changed** — the goal was
strictly to keep the app compiling, compatible, and running as-is.

---

## 1. Core Migration: ViewBinding

- Removed the deprecated `kotlin-android-extensions` Gradle plugin (no longer
  supported by Google) and migrated the entire codebase to **ViewBinding**.
- **57 files migrated**: 16 Activities, 32 Fragments, 3 RecyclerView Adapters,
  5 custom compound Views, and 1 DialogFragment.
- Behavior and layouts are unchanged — only the way Kotlin code accesses views
  was updated (`binding.viewId` instead of Kotlin synthetic imports).

## 2. Build Configuration & Gradle

- `compileSdkVersion` / `targetSdkVersion`: **34 → 36** (Android 16), required
  ahead of Google Play's mandatory target API deadline.
- Migrated Room's annotation processing from **kapt to KSP** for faster builds.
- Added the mandatory `namespace` declaration in `build.gradle` (required by
  current AGP versions; previously read from the manifest `package` attribute).
- Removed the obsolete `buildToolsVersion` override (AGP now selects it
  automatically).
- Enabled `buildFeatures.buildConfig true`, required for the existing
  `buildConfigField` declarations.
- Fixed several broken/outdated dependency coordinates:
  - `zxing-android-embedded` bumped to a version actually published on Maven
    Central (the previous pinned version only existed on a defunct repository).
  - `SimpleCropView` and `singledateandtimepicker` re-pointed to valid JitPack
    tags (the original coordinates relied on the now-shut-down JCenter).
- Upgraded numerous dependencies to current stable versions, including Kotlin
  (2.1.0), AndroidX Core/AppCompat/Material/ConstraintLayout, ez-vcard,
  commons-codec, kotlin-onetimepassword, RxKotlin, and Sentry.
- Fixed a hard compile error introduced by the Kotlin 2.x stdlib, which
  promoted `String.toUpperCase(Locale)` / `toLowerCase(Locale)` from a warning
  to a compile error; replaced with `uppercase(Locale)` / `lowercase(Locale)`.

## 3. Dependency Slimming (no functional change, smaller/safer APK)

- Excluded `freemarker`, `jsoup`, and `jackson-core` — optional transitive
  dependencies pulled in by `ez-vcard` for HTML/JSON vCard export, a feature
  this app never uses (it only reads/writes plain-text vCards).
- Switched from the aggregate `sentry-android` artifact to
  `sentry-android-core`, dropping two unused modules:
  - `sentry-android-ndk` (native/JNI crash reporting — the app has no native
    code).
  - `sentry-android-replay` (session replay / screen recording — never
    enabled or used).

## 4. Security & Privacy Review

- Reviewed the manifest, declared permissions, exported components, and
  `FileProvider` configuration — no significant issues found; all exported
  components have legitimate intent-filter justifications.
- **Removed Sentry entirely** from the app (crash-reporting SDK and its
  network endpoint), in preparation for a possible future F-Droid submission,
  since F-Droid's inclusion policy prohibits proprietary tracking/analytics
  dependencies.
  - `Logger.kt` no longer reports exceptions to a third-party service; it now
    logs locally via Logcat only.
  - Removed the Sentry Gradle plugin, its configuration block, the
    `io.sentry:sentry-android-core` dependency, and the `io.sentry.dsn`
    manifest meta-data entry.

## 5. Runtime Bug Fixes Surfaced by the Upgrade

- **Quick Settings Tile crash on Android 14+**: Google deprecated
  `TileService.startActivityAndCollapse(Intent)` into a hard runtime exception
  for apps targeting API 34+. Fixed by switching to
  `TileService.startActivityAndCollapse(PendingIntent)`, with a compatibility
  fallback for devices below Android 14.

## 6. Release Signing

- Generated a dedicated release keystore.
- Added a `signingConfigs.release` block that reads credentials from a local
  `keystore.properties` file (excluded from version control) instead of
  hardcoding secrets in `build.gradle`.

## 7. Deliberately Deferred

- **Room 2.4.2 and the legacy `android.arch.paging:1.0.1` library were left
  unchanged.** Upgrading them would require a broader rewrite (DAO,
  Repository, and Adapter layers moving to `androidx.paging`), carries real
  behavioral risk, and isn't necessary while the app already builds and runs
  correctly on current tooling. Left as a candidate for a future, dedicated
  refactor.

---

*No app icon, screen layout, navigation flow, or feature behavior was
intentionally modified as part of this update.*
