package org.hyperskill.app.code.domain.model

enum class ProgrammingLanguage(val languageName: String) {
    PYTHON("python"),
    PYTHON3("python3"),
    PYTHON3_1("python3.10"),
    PYTHON3_11("python3.11"),
    CPP("c++"),
    CPP11("c++11"),
    CPP20("c++20"),
    C("c"),
    C_VALGRIND("c_valgrind"),
    HASKELL("haskell"),
    HASKELL7("haskell 7.10"),
    HASKELL8("haskell 8.0"),
    HASKELL8_8("haskell 8.8"),
    JAVA("java"),
    JAVA8("java8"),
    JAVA9("java9"),
    JAVA11("java11"),
    JAVA17("java17"),
    OCTAVE("octave"),
    ASM32("asm32"),
    ASM64("asm64"),
    SHELL("shell"),
    RUST("rust"),
    R("r"),
    RUBY("ruby"),
    CLOJURE("clojure"),
    CS("c#"),
    CS_MONO("mono c#"),
    JAVASCRIPT("javascript"),
    TYPESCRIPT("typescript"),
    SCALA("scala"),
    SCALA3("scala3"),
    KOTLIN("kotlin"),
    GO("go"),
    PASCAL_ABC("pascalabc"),
    PERL("perl"),
    SQL("sql"),
    SWIFT("swift"),
    PHP("php"),
    JULIA("julia"),
    DART("dart");

    val fileExtension: String
        get() = when (this) {
            PYTHON, PYTHON3, PYTHON3_1, PYTHON3_11 -> "py"
            CPP, CPP11, CPP20, C, C_VALGRIND -> "cpp"
            HASKELL, HASKELL7, HASKELL8, HASKELL8_8 -> "hs"
            JAVA, JAVA8, JAVA9, JAVA11, JAVA17 -> "java"
            OCTAVE -> "matlab"
            ASM32, ASM64 -> "asm"
            SHELL -> "sh"
            RUST -> "rust"
            R -> "r"
            RUBY -> "rb"
            CLOJURE -> "clj"
            CS, CS_MONO -> "cs"
            JAVASCRIPT -> "js"
            TYPESCRIPT -> "ts"
            SCALA, SCALA3 -> "scala"
            KOTLIN -> "kt"
            GO -> "go"
            PASCAL_ABC -> "pascal"
            PERL -> "perl"
            SQL -> "sql"
            SWIFT -> "swift"
            PHP -> "php"
            JULIA -> "julia"
            DART -> "dart"
        }

    companion object {
        fun of(languageName: String): ProgrammingLanguage? =
            ProgrammingLanguage.values()
                .find {
                    it.languageName.equals(languageName, ignoreCase = true)
                }
    }
}

/**
 * Returns the file extension for a given programming language.
 *
 * @param lang The name of the programming language.
 * @return The file extension for the given programming language, or an empty string if the language is not found.
 * Empty string for not found language is used for compatibility with Android code.
 */
fun fileExtensionForLanguage(lang: String): String =
    ProgrammingLanguage.of(lang)?.fileExtension ?: ""