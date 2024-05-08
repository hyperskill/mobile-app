import XCTest

@testable import iosHyperskillApp

final class CodePlaygroundShouldMakeTabLineAfterPerformanceTests: XCTestCase {
    func shouldMakeTabLineAfter(
        symbol: Character,
        language: CodeLanguage
    ) -> (shouldMakeNewLine: Bool, paired: Bool) {
        switch language {
        case .python, .python3, .python31:
            return symbol == ":"
                ? (shouldMakeNewLine: true, paired: false)
                : (shouldMakeNewLine: false, paired: false)
        case .c,
                .cValgrind,
                .cpp11,
                .cpp,
                .java,
                .java8,
                .java9,
                .java11,
                .java17,
                .cs,
                .csMono,
                .kotlin,
                .swift,
                .rust,
                .javascript,
                .scala,
                .scala3,
                .go,
                .perl,
                .php:
            return symbol == "{"
                ? (shouldMakeNewLine: true, paired: true)
                : (shouldMakeNewLine: false, paired: false)
        default:
            return (shouldMakeNewLine: false, paired: false)
        }
    }

    func shouldMakeTabLineAfterOptimized(
        symbol: Character,
        language: CodeLanguage
    ) -> (shouldMakeNewLine: Bool, paired: Bool) {
        switch symbol {
        case ":":
            if case .python = language,
               case .python3 = language,
               case .python31 = language {
                return (shouldMakeNewLine: true, paired: false)
            }
            return (shouldMakeNewLine: false, paired: false)
        case "{":
            switch language {
            case .c,
                    .cValgrind,
                    .cpp, .cpp11,
                    .java,
                    .java8,
                    .java9,
                    .java11,
                    .java17,
                    .cs,
                    .csMono,
                    .kotlin,
                    .swift,
                    .rust,
                    .javascript,
                    .scala,
                    .scala3,
                    .go,
                    .perl,
                    .php:
                return (shouldMakeNewLine: true, paired: true)
            default:
                return (shouldMakeNewLine: false, paired: false)
            }
        default:
            return (shouldMakeNewLine: false, paired: false)
        }
    }

    // Testing with a language where ":" is significant.
    func testShouldMakeTabLineAfterWithColon() {
        measure {
            let _ = shouldMakeTabLineAfter(symbol: ":", language: .python)
        }
    }

    func testShouldMakeTabLineAfterOptimizedWithColon() {
        measure {
            let _ = shouldMakeTabLineAfterOptimized(symbol: ":", language: .python)
        }
    }

    // Testing with a language where "{" is significant.
    func testShouldMakeTabLineAfterWithCurlyBrace() {
        measure {
            let _ = shouldMakeTabLineAfter(symbol: "{", language: .swift)
        }
    }

    func testShouldMakeTabLineAfterOptimizedWithCurlyBrace() {
        measure {
            let _ = shouldMakeTabLineAfterOptimized(symbol: "{", language: .swift)
        }
    }

    // Testing with a symbol and language where no action is required.
    func testShouldMakeTabLineAfterWithIrrelevantSymbol() {
        measure {
            let _ = shouldMakeTabLineAfter(symbol: ";", language: .java)
        }
    }

    func testShouldMakeTabLineAfterOptimizedWithIrrelevantSymbol() {
        measure {
            let _ = shouldMakeTabLineAfterOptimized(symbol: ";", language: .java)
        }
    }
}
