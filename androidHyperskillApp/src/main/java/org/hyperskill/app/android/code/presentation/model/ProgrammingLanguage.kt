package org.hyperskill.app.android.code.presentation.model

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import org.hyperskill.app.android.R
import org.hyperskill.app.android.code.presentation.model.ProgrammingLanguage.*

enum class ProgrammingLanguage(val serverPrintableName: String) : Parcelable {
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

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ordinal)
    }

    companion object CREATOR : Parcelable.Creator<ProgrammingLanguage> {

        override fun createFromParcel(parcel: Parcel): ProgrammingLanguage =
            values()[parcel.readInt()]

        override fun newArray(size: Int): Array<ProgrammingLanguage?> =
            arrayOfNulls(size)
    }
}

private fun serverNameToLanguage(serverName: String): ProgrammingLanguage? =
    ProgrammingLanguage.values()
        .find {
            it.serverPrintableName.equals(serverName, ignoreCase = true)
        }

fun symbolsForLanguage(lang: String, context: Context): Array<String> {
    val programmingLanguage = serverNameToLanguage(lang)
    val arrayRes = when (programmingLanguage) {
        PYTHON3, PYTHON31, PYTHON ->
            R.array.frequent_symbols_py
        CPP11, CPP, C, C_VALGRIND ->
            R.array.frequent_symbols_cpp
        JAVA, JAVA8, JAVA9, JAVA11, JAVA17 ->
            R.array.frequent_symbols_java
        CS, CS_MONO ->
            R.array.frequent_symbols_cs
        JAVASCRIPT ->
            R.array.frequent_symbols_js
        SQL ->
            R.array.frequent_symbols_sql
        PHP ->
            R.array.frequent_symbols_php

        HASKELL, HASKELL7, HASKELL8, HASKELL8_8,
        OCTAVE,
        ASM32, ASM64,
        SHELL,
        RUST,
        R,
        RUBY,
        CLOJURE,
        SCALA, SCALA3,
        KOTLIN,
        GO,
        PASCAL,
        PERL,
        SWIFT,
        JULIA,
        DART,
        null -> R.array.frequent_symbols_default
    }
    return context.resources.getStringArray(arrayRes)
}

fun extensionForLanguage(lang: String): String =
    when (serverNameToLanguage(lang)) {
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
        null -> ""
    }