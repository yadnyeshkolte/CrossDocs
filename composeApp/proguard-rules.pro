
# Keep Kotlin serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep Compose
-keep class androidx.compose.** { *; }
-keep class org.jetbrains.skiko.** { *; }

# Keep Ktor
-keep class io.ktor.** { *; }
-keep class kotlinx.coroutines.** { *; }

# General configuration
-dontobfuscate
-dontoptimize
-dontwarn kotlin.**
-dontwarn org.slf4j.**
-dontwarn org.jetbrains.skija.**
-dontwarn org.jetbrains.skiko.**