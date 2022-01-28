private val nonStableVersionMarker = listOf("alpha", "beta", "rc", "m")

fun isStableVersion(version: String): Boolean =
    !nonStableVersionMarker.any { version.contains(it, ignoreCase = true) }