import XCTest

@testable import iosHyperskillApp

final class CodePlaygroundGetChangesSubstringPerformanceTests: XCTestCase {
    func getChangesSubstring(currentText: String, previousText: String) -> CodePlaygroundManager.Changes {
        var maxString = ""
        var minString = ""
        var isInsertion = true

        // Determine if something was deleted or inserted
        if currentText.count > previousText.count {
            maxString = currentText
            minString = previousText
            isInsertion = true
        } else {
            maxString = previousText
            minString = currentText
            isInsertion = false
        }

        // Search for the beginning of the changed substring
        var changesBeginningOffset = 0
        while changesBeginningOffset < minString.count
                && minString[minString.index(minString.startIndex, offsetBy: changesBeginningOffset)]
                == maxString[maxString.index(maxString.startIndex, offsetBy: changesBeginningOffset)] {
            changesBeginningOffset += 1
        }

        minString.removeSubrange(
            minString.startIndex..<minString.index(minString.startIndex, offsetBy: changesBeginningOffset)
        )
        maxString.removeSubrange(
            maxString.startIndex..<maxString.index(maxString.startIndex, offsetBy: changesBeginningOffset)
        )

        // Search for the ending of the changed substring
        // swiftlint:disable line_length
        var changesEndingOffset = 0
        while changesEndingOffset < minString.count
                && minString[minString.index(minString.index(before: minString.endIndex), offsetBy: -changesEndingOffset)]
                == maxString[maxString.index(maxString.index(before: maxString.endIndex), offsetBy: -changesEndingOffset)] {
            changesEndingOffset += 1
        }
        // swiftlint:enable line_length

        if !minString.isEmpty {
            minString.removeSubrange(
                minString.index(
                    minString.index(before: minString.endIndex),
                    offsetBy: -changesEndingOffset + 1
                )..<minString.endIndex
            )
        }
        if !maxString.isEmpty {
            maxString.removeSubrange(
                maxString.index(
                    maxString.index(before: maxString.endIndex),
                    offsetBy: -changesEndingOffset + 1
                )..<maxString.endIndex
            )
        }

        return (isInsertion: isInsertion, changes: maxString)
    }

    func getChangesSubstringOptimized1(currentText: String, previousText: String) -> CodePlaygroundManager.Changes {
        let startIndex = zip(currentText, previousText)
            .enumerated()
            .first(where: { $1.0 != $1.1 })?.0 ?? min(currentText.count, previousText.count)
        let endIndexCurrent = currentText.index(currentText.endIndex, offsetBy: -(currentText.count - startIndex))
        let endIndexPrevious = previousText.index(previousText.endIndex, offsetBy: -(previousText.count - startIndex))

        let reversedCurrentText = String(currentText[endIndexCurrent...].reversed())
        let reversedPreviousText = String(previousText[endIndexPrevious...].reversed())

        let reversedIndex = zip(reversedCurrentText, reversedPreviousText)
            .enumerated()
            .first(where: { $1.0 != $1.1 })?.0 ?? min(reversedCurrentText.count, reversedPreviousText.count)

        let isInsertion = currentText.count > previousText.count
        let changes = isInsertion
            ? String(currentText.dropFirst(startIndex).dropLast(reversedIndex))
            : String(previousText.dropFirst(startIndex).dropLast(reversedIndex))

        return (isInsertion: isInsertion, changes: changes)
    }

    func getChangesSubstringOptimized2(currentText: String, previousText: String) -> CodePlaygroundManager.Changes {
        let minLength = min(currentText.count, previousText.count)

        // Find start index of change
        let startIndex = zip(currentText.indices, previousText.indices)
            .first(where: { currentText[$0] != previousText[$1] })?.0 ?? currentText.startIndex

        // Find end index of change from the end, avoiding reversing strings
        let reverseStartIndexCurrent = currentText.index(currentText.endIndex, offsetBy: -minLength)
        let reverseStartIndexPrevious = previousText.index(previousText.endIndex, offsetBy: -minLength)

        let reversedIndexOffset = zip(
            currentText[reverseStartIndexCurrent...].reversed(),
            previousText[reverseStartIndexPrevious...].reversed()
        )
            .enumerated()
            .first(where: { $1.0 != $1.1 })?.0 ?? 0

        // Calculate the actual end index considering the reversed index offset
        let endIndexOffset = minLength - reversedIndexOffset
        let endIndexCurrent = currentText.index(
            currentText.startIndex,
            offsetBy: max(startIndex.utf16Offset(in: currentText), endIndexOffset),
            limitedBy: currentText.endIndex
        ) ?? currentText.endIndex
        let endIndexPrevious = previousText.index(
            previousText.startIndex,
            offsetBy: max(startIndex.utf16Offset(in: previousText), endIndexOffset),
            limitedBy: previousText.endIndex
        ) ?? previousText.endIndex

        // Determine if the operation is an insertion or deletion
        let isInsertion = currentText.count > previousText.count

        // Extract the changed substring based on the operation
        let changes = isInsertion
            ? String(currentText[startIndex..<endIndexCurrent])
            : String(previousText[startIndex..<endIndexPrevious])

        return (isInsertion: isInsertion, changes: changes)
    }

    func testGetChangesSubstring() {
        let currentText = String(repeating: "a", count: 1000) + "b" + String(repeating: "a", count: 1000)
        let previousText = String(repeating: "a", count: 2001)

        measure {
            let _ = getChangesSubstring(currentText: currentText, previousText: previousText)
        }
    }

    func testGetChangesSubstringOptimized() {
        let currentText = String(repeating: "a", count: 1000) + "b" + String(repeating: "a", count: 1000)
        let previousText = String(repeating: "a", count: 2001)

        measure {
            let _ = getChangesSubstringOptimized1(currentText: currentText, previousText: previousText)
        }
    }

    func testGetChangesSubstringOptimized2() {
        let currentText = String(repeating: "a", count: 1000) + "b" + String(repeating: "a", count: 1000)
        let previousText = String(repeating: "a", count: 2001)

        measure {
            let _ = getChangesSubstringOptimized2(currentText: currentText, previousText: previousText)
        }
    }

    // MARK: ShortText

    func testGetChangesSubstringWithShortText() {
        let currentText = "Hello"
        let previousText = "Helloo"
        measure {
            let _ = getChangesSubstring(currentText: currentText, previousText: previousText)
        }
    }

    func testGetChangesSubstringWithShortTextOptimized1() {
        let currentText = "Hello"
        let previousText = "Helloo"
        measure {
            let _ = getChangesSubstringOptimized1(currentText: currentText, previousText: previousText)
        }
    }

    func testGetChangesSubstringWithShortTextOptimized2() {
        let currentText = "Hello"
        let previousText = "Helloo"
        measure {
            let _ = getChangesSubstringOptimized2(currentText: currentText, previousText: previousText)
        }
    }

    // MARK: NoChange

    func testGetChangesSubstringWithNoChange() {
        let currentText = "Hello, world!"
        let previousText = "Hello, world!"
        measure {
            let _ = getChangesSubstring(currentText: currentText, previousText: previousText)
        }
    }

    func testGetChangesSubstringWithNoChangeOptimized1() {
        let currentText = "Hello, world!"
        let previousText = "Hello, world!"
        measure {
            let _ = getChangesSubstringOptimized1(currentText: currentText, previousText: previousText)
        }
    }

    func testGetChangesSubstringWithNoChangeOptimized2() {
        let currentText = "Hello, world!"
        let previousText = "Hello, world!"
        measure {
            let _ = getChangesSubstringOptimized2(currentText: currentText, previousText: previousText)
        }
    }

    // MARK: AllCharactersChanged

    func testGetChangesSubstringWithAllCharactersChanged() {
        let currentText = "Hello, world!"
        let previousText = "Goodbye, earth!"
        measure {
            let _ = getChangesSubstring(currentText: currentText, previousText: previousText)
        }
    }

    func testGetChangesSubstringWithAllCharactersChangedOptimized1() {
        let currentText = "Hello, world!"
        let previousText = "Goodbye, earth!"
        measure {
            let _ = getChangesSubstringOptimized1(currentText: currentText, previousText: previousText)
        }
    }

    func testGetChangesSubstringWithAllCharactersChangedOptimized2() {
        let currentText = "Hello, world!"
        let previousText = "Goodbye, earth!"
        measure {
            let _ = getChangesSubstringOptimized2(currentText: currentText, previousText: previousText)
        }
    }

    // MARK: AtTheBeginning

    func testGetChangesSubstringWithChangesAtTheBeginning() {
        let currentText = "New beginnings, same ends"
        let previousText = "Old beginnings, same ends"
        measure {
            let _ = getChangesSubstring(currentText: currentText, previousText: previousText)
        }
    }

    func testGetChangesSubstringWithChangesAtTheBeginningOptimized1() {
        let currentText = "New beginnings, same ends"
        let previousText = "Old beginnings, same ends"
        measure {
            let _ = getChangesSubstringOptimized1(currentText: currentText, previousText: previousText)
        }
    }

    func testGetChangesSubstringWithChangesAtTheBeginningOptimized2() {
        let currentText = "New beginnings, same ends"
        let previousText = "Old beginnings, same ends"
        measure {
            let _ = getChangesSubstringOptimized2(currentText: currentText, previousText: previousText)
        }
    }

    // MARK: AtTheEnd

    func testGetChangesSubstringWithChangesAtTheEnd() {
        let currentText = "The same start, different conclusions"
        let previousText = "The same start, similar conclusions"
        measure {
            let _ = getChangesSubstring(currentText: currentText, previousText: previousText)
        }
    }

    func testGetChangesSubstringWithChangesAtTheEndOptimized1() {
        let currentText = "The same start, different conclusions"
        let previousText = "The same start, similar conclusions"
        measure {
            let _ = getChangesSubstringOptimized1(currentText: currentText, previousText: previousText)
        }
    }

    func testGetChangesSubstringWithChangesAtTheEndOptimized2() {
        let currentText = "The same start, different conclusions"
        let previousText = "The same start, similar conclusions"
        measure {
            let _ = getChangesSubstringOptimized2(currentText: currentText, previousText: previousText)
        }
    }

    // MARK: SpecialCharacters

    func testGetChangesSubstringWithSpecialCharacters() {
        let currentText = "Fun with emojis 😊😂👍"
        let previousText = "Fun with emojis 😊😂👎"
        measure {
            let _ = getChangesSubstring(currentText: currentText, previousText: previousText)
        }
    }

    func testGetChangesSubstringWithSpecialCharactersOptimized1() {
        let currentText = "Fun with emojis 😊😂👍"
        let previousText = "Fun with emojis 😊😂👎"
        measure {
            let _ = getChangesSubstringOptimized1(currentText: currentText, previousText: previousText)
        }
    }

    func testGetChangesSubstringWithSpecialCharactersOptimized2() {
        let currentText = "Fun with emojis 😊😂👍"
        let previousText = "Fun with emojis 😊😂👎"
        measure {
            let _ = getChangesSubstringOptimized2(currentText: currentText, previousText: previousText)
        }
    }
}
