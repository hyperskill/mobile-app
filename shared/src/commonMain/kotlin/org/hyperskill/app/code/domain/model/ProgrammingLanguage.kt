package org.hyperskill.app.code.domain.model

enum class ProgrammingLanguage(val languageName: String) {
    PYTHON("python"),
    PYTHON3("python3"),
    PYTHON31("python3.10"),
    CPP11("c++11"),
    CPP("c++"),
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
    SCALA("scala"),
    SCALA3("scala3"),
    KOTLIN("kotlin"),
    GO("go"),
    PASCAL("pascalabc"),
    PERL("perl"),
    SQL("sql"),
    SWIFT("swift"),
    PHP("php"),
    JULIA("julia"),
    DART("dart");

    val extension: String
        get() = when (this) {
            PYTHON, PYTHON3, PYTHON31 -> "py"
            CPP11,
            CPP,
            C, C_VALGRIND -> "cpp"
            HASKELL, HASKELL7, HASKELL8, HASKELL8_8 -> "hs"
            JAVA, JAVA8, JAVA9, JAVA11, JAVA17 -> "java"
            OCTAVE -> "matlab"
            ASM32,
            ASM64 -> "asm"
            SHELL -> "sh"
            RUST -> "rust"
            R -> "r"
            RUBY -> "rb"
            CLOJURE -> "clj"
            CS, CS_MONO -> "cs"
            JAVASCRIPT -> "js"
            SCALA, SCALA3 -> "scala"
            KOTLIN -> "kt"
            GO -> "go"
            PASCAL -> "pascal"
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

fun extensionForLanguage(lang: String): String =
    ProgrammingLanguage.of(lang)?.extension ?: ""