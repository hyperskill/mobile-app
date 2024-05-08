import XCTest

@testable import iosHyperskillApp

final class CodePlaygroundGetCurrentTokenPerformanceTests: XCTestCase {
    private static let allowedCharactersSet = Set("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_")

    func getCurrentToken(text: String, cursorPosition: Int) -> String {
        guard cursorPosition >= 0 && cursorPosition <= text.count else {
            return ""
        }

        var offsetBefore = 0
        while let character = text[safe: (cursorPosition - offsetBefore - 1)],
              Self.allowedCharactersSet.contains(character) {
            offsetBefore += 1
        }

        var offsetAfter = 0
        while let character = text[safe: (cursorPosition + offsetAfter)],
              Self.allowedCharactersSet.contains(character) {
            offsetAfter += 1
        }

        let beforeCursorString = text[safe: (cursorPosition - offsetBefore)..<cursorPosition] ?? ""
        let afterCursorString = text[safe: cursorPosition..<(cursorPosition + offsetAfter)] ?? ""

        return beforeCursorString + afterCursorString
    }

    func getCurrentTokenOptimized(text: String, cursorPosition: Int) -> String {
        guard cursorPosition >= 0 && cursorPosition <= text.count else {
            return ""
        }

        let startIndex = text.startIndex
        let cursorIndex = text.index(startIndex, offsetBy: cursorPosition, limitedBy: text.endIndex) ?? text.endIndex

        var beforeCursorIndex = cursorIndex
        while beforeCursorIndex > text.startIndex {
            let prevIndex = text.index(before: beforeCursorIndex)
            if Self.allowedCharactersSet.contains(text[prevIndex]) {
                beforeCursorIndex = prevIndex
            } else {
                break
            }
        }

        var afterCursorIndex = cursorIndex
        while afterCursorIndex < text.endIndex {
            let nextIndex = text.index(after: afterCursorIndex)
            if Self.allowedCharactersSet.contains(text[afterCursorIndex]) {
                afterCursorIndex = nextIndex
            } else {
                break
            }
        }

        return String(text[beforeCursorIndex..<afterCursorIndex])
    }

    func testPerformanceGetCurrentToken() {
        let text = String(repeating: "abc123_", count: 1000)
        let cursorPosition = text.count / 2

        measure {
            _ = getCurrentToken(text: text, cursorPosition: cursorPosition)
        }
    }

    func testPerformanceGetCurrentTokenOptimized() {
        let text = String(repeating: "abc123_", count: 1000)
        let cursorPosition = text.count / 2

        measure {
            _ = getCurrentTokenOptimized(text: text, cursorPosition: cursorPosition)
        }
    }

    func testPerformanceEdgeCaseAtStart() {
        let text = "1234567890" + String(repeating: " ", count: 1000) + "abcdefg"
        let cursorPosition = 0

        measure {
            _ = getCurrentToken(text: text, cursorPosition: cursorPosition)
        }
    }

    func testPerformanceEdgeCaseAtStartOptimized() {
        let text = "1234567890" + String(repeating: " ", count: 1000) + "abcdefg"
        let cursorPosition = 0

        measure {
            _ = getCurrentTokenOptimized(text: text, cursorPosition: cursorPosition)
        }
    }

    func testPerformanceEdgeCaseAtEnd() {
        let text = "1234567890" + String(repeating: " ", count: 1000) + "abcdefg"
        let cursorPosition = text.count - 1

        measure {
            _ = getCurrentToken(text: text, cursorPosition: cursorPosition)
        }
    }

    func testPerformanceEdgeCaseAtEndOptimized() {
        let text = "1234567890" + String(repeating: " ", count: 1000) + "abcdefg"
        let cursorPosition = text.count - 1

        measure {
            _ = getCurrentTokenOptimized(text: text, cursorPosition: cursorPosition)
        }
    }
}
