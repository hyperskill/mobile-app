import XCTest

final class CodePlaygroundManagerCountTabSizePerformanceTests: XCTestCase {
    func countTabSize(text: String) -> Int {
        var minTabSize = 100

        text.enumerateLines { line, _ in
            var spacesBeforeFirstCharacter = 0

            for character in line {
                if character == " " {
                    spacesBeforeFirstCharacter += 1
                } else {
                    break
                }
            }

            if spacesBeforeFirstCharacter > 0 && minTabSize > spacesBeforeFirstCharacter {
                minTabSize = spacesBeforeFirstCharacter
            }
        }

        if minTabSize == 100 {
            minTabSize = 4
        }

        return minTabSize
    }

    func countTabSizeOptimized(text: String) -> Int {
        // Start with a very high default value, indicating no tabs found yet
        var minTabSize = Int.max

        // Enumerate through each line of the text
        text.enumerateLines { line, _ in
            // Count leading spaces using prefix(while:)
            let leadingSpaces = line.prefix(while: { $0 == " " }).count

            // Update the minimum tab size if this line has leading spaces and it's fewer than any previous line
            if leadingSpaces > 0 {
                minTabSize = min(minTabSize, leadingSpaces)
            }
        }

        // If minTabSize is still the default value, then no tabs were found, default to 4
        return minTabSize == Int.max ? 4 : minTabSize
    }

    func testPerformanceOfOriginalCountTabSize() {
        let text = generateTestString(lines: 1000, spaces: 5, extraContent: "some content after spaces")
        measure {
            _ = countTabSize(text: text)
        }
    }

    func testPerformanceOfOptimizedCountTabSize() {
        let text = generateTestString(lines: 1000, spaces: 5, extraContent: "some content after spaces")
        measure {
            _ = countTabSizeOptimized(text: text)
        }
    }

    // Helper function to generate test strings
    private func generateTestString(lines: Int, spaces: Int, extraContent: String) -> String {
        let line = String(repeating: " ", count: spaces) + extraContent + "\n"
        return String(repeating: line, count: lines)
    }
}
