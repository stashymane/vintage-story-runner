package data

actual fun readEnv(name: String): String? = System.getenv(name)
