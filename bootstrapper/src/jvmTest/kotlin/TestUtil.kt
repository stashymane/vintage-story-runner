private object ResourceLoader {}

fun readFile(path: String): String = ResourceLoader::class.java.getResourceAsStream(path)!!.bufferedReader().readText()
