package org.hyperskill.app.android.code.presentation.model

import android.content.Context
import org.hyperskill.app.code.domain.model.ProgrammingLanguage
/* ktlint-disable */
import org.hyperskill.app.code.domain.model.ProgrammingLanguage.*
import org.hyperskill.app.android.R as AppR

// The resources class is imported as AppR, because the star-imported ProgrammingLanguage.R entry shadows R
fun ProgrammingLanguage?.getSymbols(context: Context): Array<String> =
    when (this) {
        PYTHON, PYTHON3, PYTHON3_1, PYTHON3_11 ->
            AppR.array.frequent_symbols_py
        CPP, CPP11, CPP20, C, C_VALGRIND ->
            AppR.array.frequent_symbols_cpp
        JAVA, JAVA8, JAVA9, JAVA11, JAVA17 ->
            AppR.array.frequent_symbols_java
        CS, CS_MONO ->
            AppR.array.frequent_symbols_cs
        JAVASCRIPT, TYPESCRIPT ->
            AppR.array.frequent_symbols_js
        SQL ->
            AppR.array.frequent_symbols_sql
        PHP ->
            AppR.array.frequent_symbols_php

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
        PASCAL_ABC,
        PERL,
        SWIFT,
        JULIA,
        DART,
        null -> AppR.array.frequent_symbols_default
    }.let(context.resources::getStringArray)

fun symbolsForLanguage(lang: String, context: Context): Array<String> =
    ProgrammingLanguage.of(lang).getSymbols(context)