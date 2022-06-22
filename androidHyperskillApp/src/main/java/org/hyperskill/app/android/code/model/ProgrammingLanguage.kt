package org.hyperskill.app.android.code.model

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.android.R

@Serializable
enum class ProgrammingLanguage(val serverPrintableName: String) : Parcelable {
    @SerialName("python3")
    PYTHON("python3"),

    @SerialName("c++11")
    CPP11("c++11"),

    @SerialName("c++")
    CPP("c++"),

    @SerialName("c")
    C("c"),

    @SerialName("haskell")
    HASKELL("haskell"),

    @SerialName("haskell 7.10")
    HASKELL7("haskell 7.10"),

    @SerialName("haskell 8.0")
    HASKELL8("haskell 8.0"),

    @SerialName("java")
    JAVA("java"),

    @SerialName("java8")
    JAVA8("java8"),

    @SerialName("octave")
    OCTAVE("octave"),

    @SerialName("asm32")
    ASM32("asm32"),

    @SerialName("asm64")
    ASM64("asm64"),

    @SerialName("shell")
    SHELL("shell"),

    @SerialName("rust")
    RUST("rust"),

    @SerialName("r")
    R("r"),

    @SerialName("ruby")
    RUBY("ruby"),

    @SerialName("clojure")
    CLOJURE("clojure"),

    @SerialName("mono c#")
    CS("mono c#"),

    @SerialName("javascript")
    JAVASCRIPT("javascript"),

    @SerialName("scala")
    SCALA("scala"),

    @SerialName("kotlin")
    KOTLIN("kotlin"),

    @SerialName("go")
    GO("go"),

    @SerialName("pascalabc")
    PASCAL("pascalabc"),

    @SerialName("perl")
    PERL("perl"),
    SQL("sql");

    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ordinal)
    }

    companion object CREATOR : Parcelable.Creator<ProgrammingLanguage> {

        override fun createFromParcel(parcel: Parcel): ProgrammingLanguage =
            values()[parcel.readInt()]

        override fun newArray(size: Int): Array<ProgrammingLanguage?> =
            arrayOfNulls(size)

        // make it public and resolve highlighting
        @Suppress("unused")
        private fun highlighting(serverName: String) {
            when (serverNameToLanguage(serverName)) {
                PYTHON -> TODO()
                CPP11 -> TODO()
                CPP -> TODO()
                C -> TODO()
                HASKELL -> TODO()
                HASKELL7 -> TODO()
                HASKELL8 -> TODO()
                JAVA -> TODO()
                JAVA8 -> TODO()
                OCTAVE -> TODO()
                ASM32 -> TODO()
                ASM64 -> TODO()
                SHELL -> TODO()
                RUST -> TODO()
                R -> TODO()
                RUBY -> TODO()
                CLOJURE -> TODO()
                CS -> TODO()
                JAVASCRIPT -> TODO()
                SCALA -> TODO()
                KOTLIN -> TODO()
                GO -> TODO()
                PASCAL -> TODO()
                PERL -> TODO()
                SQL -> TODO()
                null -> TODO()
            }
        }
    }
}

private fun serverNameToLanguage(serverName: String): ProgrammingLanguage? =
    ProgrammingLanguage.values()
        .find {
            it.serverPrintableName.equals(serverName, ignoreCase = true)
        }

fun symbolsForLanguage(lang: String, context: Context): Array<String> {
    val programmingLanguage = serverNameToLanguage(lang)
    with(context.resources) {
        return when (programmingLanguage) {
            ProgrammingLanguage.PYTHON ->
                getStringArray(R.array.frequent_symbols_py)
            ProgrammingLanguage.CPP11, ProgrammingLanguage.CPP, ProgrammingLanguage.C ->
                getStringArray(R.array.frequent_symbols_cpp)
            ProgrammingLanguage.HASKELL, ProgrammingLanguage.HASKELL7, ProgrammingLanguage.HASKELL8 ->
                getStringArray(R.array.frequent_symbols_default)
            ProgrammingLanguage.JAVA, ProgrammingLanguage.JAVA8 ->
                getStringArray(R.array.frequent_symbols_java)
            ProgrammingLanguage.OCTAVE ->
                getStringArray(R.array.frequent_symbols_default)
            ProgrammingLanguage.ASM32, ProgrammingLanguage.ASM64 ->
                getStringArray(R.array.frequent_symbols_default)
            ProgrammingLanguage.SHELL ->
                getStringArray(R.array.frequent_symbols_default)
            ProgrammingLanguage.RUST ->
                getStringArray(R.array.frequent_symbols_default)
            ProgrammingLanguage.R ->
                getStringArray(R.array.frequent_symbols_default)
            ProgrammingLanguage.RUBY ->
                getStringArray(R.array.frequent_symbols_default)
            ProgrammingLanguage.CLOJURE ->
                getStringArray(R.array.frequent_symbols_default)
            ProgrammingLanguage.CS ->
                getStringArray(R.array.frequent_symbols_cs)
            ProgrammingLanguage.JAVASCRIPT ->
                getStringArray(R.array.frequent_symbols_js)
            ProgrammingLanguage.SCALA ->
                getStringArray(R.array.frequent_symbols_default)
            ProgrammingLanguage.KOTLIN ->
                getStringArray(R.array.frequent_symbols_default)
            ProgrammingLanguage.GO ->
                getStringArray(R.array.frequent_symbols_default)
            ProgrammingLanguage.PASCAL ->
                getStringArray(R.array.frequent_symbols_default)
            ProgrammingLanguage.PERL ->
                getStringArray(R.array.frequent_symbols_default)
            ProgrammingLanguage.SQL ->
                getStringArray(R.array.frequent_symbols_sql)
            null ->
                getStringArray(R.array.frequent_symbols_default)
        }
    }
}

fun extensionForLanguage(lang: String): String =
    when (serverNameToLanguage(lang)) {
        ProgrammingLanguage.PYTHON -> "py"
        ProgrammingLanguage.CPP11,
        ProgrammingLanguage.CPP,
        ProgrammingLanguage.C -> "cpp"
        ProgrammingLanguage.HASKELL,
        ProgrammingLanguage.HASKELL7,
        ProgrammingLanguage.HASKELL8 -> "hs"
        ProgrammingLanguage.JAVA,
        ProgrammingLanguage.JAVA8 -> "java"
        ProgrammingLanguage.OCTAVE -> "matlab"
        ProgrammingLanguage.ASM32,
        ProgrammingLanguage.ASM64 -> "asm"
        ProgrammingLanguage.SHELL -> "sh"
        ProgrammingLanguage.RUST -> "rust"
        ProgrammingLanguage.R -> "r"
        ProgrammingLanguage.RUBY -> "rb"
        ProgrammingLanguage.CLOJURE -> "clj"
        ProgrammingLanguage.CS -> "cs"
        ProgrammingLanguage.JAVASCRIPT -> "js"
        ProgrammingLanguage.SCALA -> "scala"
        ProgrammingLanguage.KOTLIN -> "kt"
        ProgrammingLanguage.GO -> "go"
        ProgrammingLanguage.PASCAL -> "pascal"
        ProgrammingLanguage.PERL -> "perl"
        ProgrammingLanguage.SQL -> "sql"
        null -> ""
    }