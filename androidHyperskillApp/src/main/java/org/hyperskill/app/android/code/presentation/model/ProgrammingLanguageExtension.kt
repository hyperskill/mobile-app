package org.hyperskill.app.android.code.presentation.model

import android.content.Context
import org.hyperskill.app.android.R
import org.hyperskill.app.code.domain.model.ProgrammingLanguage
/* ktlint-disable */
import org.hyperskill.app.code.domain.model.ProgrammingLanguage.*

fun ProgrammingLanguage?.getSymbols(context: Context): Array<String> =
    when (this) {
        PYTHON, PYTHON3, PYTHON3_1, PYTHON3_11 ->
            R.array.frequent_symbols_py
        CPP, CPP11, CPP20, C, C_VALGRIND ->
            R.array.frequent_symbols_cpp
        JAVA, JAVA8, JAVA9, JAVA11, JAVA17 ->
            R.array.frequent_symbols_java
        CS, CS_MONO ->
            R.array.frequent_symbols_cs
        JAVASCRIPT, TYPESCRIPT ->
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
    }.let(context.resources::getStringArray)

fun symbolsForLanguage(lang: String, context: Context): Array<String> =
    ProgrammingLanguage.of(lang).getSymbols(context)