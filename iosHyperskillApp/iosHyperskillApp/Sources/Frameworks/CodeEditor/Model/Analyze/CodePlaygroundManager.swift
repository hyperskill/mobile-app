import Foundation

final class CodePlaygroundManager {
    typealias CodeCompletion = (suggestions: [String], prefix: String)
    typealias AnalyzeResult = (text: String, position: Int, autocomplete: CodeCompletion?)
    typealias Changes = (isInsertion: Bool, changes: String)

    private static let closersDict = ["{": "}", "[": "]", "(": ")", "\"": "\"", "'": "'"]

    private static let allowedCharactersSet = Set("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_")

    private var currentCodeCompletionTableViewController: CodeCompletionTableViewController?

    /// Detects the changes string between currentText and previousText.
    /// All changes should be a substring inserted somewhere into the string.
    func getChangesSubstring(currentText: String, previousText: String) -> Changes {
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

    /// Detects if there should be made a new line with tab space and paired closing brace after new line.
    func shouldMakeTabLineAfter(
        symbol: Character,
        language: CodeLanguage
    ) -> (shouldMakeNewLine: Bool, paired: Bool) {
        switch symbol {
        case ":":
            switch language {
            case .python, .python3, .python31:
                return (shouldMakeNewLine: true, paired: false)
            default:
                return (shouldMakeNewLine: false, paired: false)
            }
        case "{":
            switch language {
            case .c,
                    .cValgrind,
                    .cpp,
                    .cpp11,
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

    /// Returns token between cursor position.
    /// - Parameters:
    ///   - text: The text from which token will be located.
    ///   - cursorPosition: The cursor position within provided text.
    func getCurrentToken(text: String, cursorPosition: Int) -> String {
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

    // swiftlint:disable:next function_parameter_count
    private func checkNextLineInsertion(
        currentText: String,
        previousText: String,
        cursorPosition: Int,
        language: CodeLanguage,
        tabSize: Int,
        changes: Changes
    ) -> AnalyzeResult? {
        guard changes.isInsertion && changes.changes == "\n" else {
            return nil
        }

        var text = currentText
        let cursorIndex = text.index(text.startIndex, offsetBy: cursorPosition)

        guard cursorIndex > text.startIndex else {
            return nil
        }

        // Searching previous \n or beginning of the string
        let firstPart = String(text[..<text.index(before: cursorIndex)])
        if let indexOfLineEndBefore = firstPart.lastIndexOf("\n") {
            // Extracting previous line before \n
            let indexAfterEndOfLine = firstPart.index(
                after: firstPart.index(firstPart.startIndex, offsetBy: indexOfLineEndBefore)
            )
            let line = String(firstPart[indexAfterEndOfLine...])

            // Counting spaces in the beginning to know the offset
            var spacesCount = 0
            for character in line {
                if character == " " {
                    spacesCount += 1
                } else {
                    break
                }
            }
            let offset = spacesCount

            // Searching for the last non-space symbol in the string to know if we need to do more than just return
            var characterBeforeEndline: Character?
            for character in line.reversed() {
                if character == " " {
                    continue
                } else {
                    characterBeforeEndline = character
                    break
                }
            }

            // Checking if there is any character before endline (it's not an empty or all-spaces line)
            if let char = characterBeforeEndline {
                let shouldTab = shouldMakeTabLineAfter(symbol: char, language: language)
                if shouldTab.shouldMakeNewLine {
                    if shouldTab.paired {
                        let spacesString = String(repeating: " ", count: offset + tabSize)
                            + "\n"
                            + String(repeating: " ", count: offset)
                        text.insert(
                            contentsOf: spacesString,
                            at: currentText.index(currentText.startIndex, offsetBy: cursorPosition)
                        )
                        return (text: text, position: cursorPosition + offset + tabSize, autocomplete: nil)
                    } else {
                        let spacesString = String(repeating: " ", count: offset + tabSize)
                        text.insert(
                            contentsOf: spacesString,
                            at: currentText.index(currentText.startIndex, offsetBy: cursorPosition)
                        )
                        return (text: text, position: cursorPosition + offset + tabSize, autocomplete: nil)
                    }
                }
            }

            // Returning with just the spaces and offset
            let spacesString = String(repeating: " ", count: offset)
            text.insert(
                contentsOf: spacesString,
                at: currentText.index(currentText.startIndex, offsetBy: cursorPosition)
            )

            return (text: text, position: cursorPosition + offset, autocomplete: nil)
        } else {
            return (text: text, position: cursorPosition, autocomplete: nil)
        }
    }

    // swiftlint:disable:next function_parameter_count
    private func checkPaired(
        currentText: String,
        previousText: String,
        cursorPosition: Int,
        language: CodeLanguage,
        tabSize: Int,
        changes: Changes
    ) -> AnalyzeResult? {
        guard changes.isInsertion,
              let closer = Self.closersDict[changes.changes] else {
            return nil
        }

        var text = currentText

        // Check if there is text after the bracket, not a \n or whitespace
        let cursorIndex = text.index(text.startIndex, offsetBy: cursorPosition)

        if cursorIndex != text.endIndex {
            let textAfter = String(text[cursorIndex...])
            if let indexOfLineEndAfter = textAfter.indexOf("\n") {
                let line = String(textAfter[..<textAfter.index(textAfter.startIndex, offsetBy: indexOfLineEndAfter)])

                var onlySpaces = true
                for character in line where character != " " {
                    onlySpaces = false
                    break
                }

                if onlySpaces {
                    text.insert(
                        closer[closer.startIndex],
                        at: currentText.index(currentText.startIndex, offsetBy: cursorPosition)
                    )
                    return (text: text, position: cursorPosition, autocomplete: nil)
                }
            }
        } else {
            text.insert(
                closer[closer.startIndex],
                at: currentText.index(currentText.startIndex, offsetBy: cursorPosition)
            )
            return (text: text, position: cursorPosition, autocomplete: nil)
        }

        return nil
    }

    private func getAutocompleteSuggestions(
        currentText: String,
        previousText: String,
        cursorPosition: Int,
        language: CodeLanguage
    ) -> AnalyzeResult? {
        // Getting current token of a string
        let token = getCurrentToken(text: currentText, cursorPosition: cursorPosition)

        if token.isEmpty {
            return nil
        }

        let suggestions = CodeCompletionKeywords.autocomplete(for: token, language: language)

        return (
            text: currentText,
            position: cursorPosition,
            autocomplete: (suggestions: suggestions, prefix: token)
        )
    }

    /// Analyzes given text using parameters.
    func analyze(
        currentText: String,
        previousText: String,
        cursorPosition: Int,
        language: CodeLanguage,
        tabSize: Int
    ) -> AnalyzeResult {
        let changes = getChangesSubstring(currentText: currentText, previousText: previousText)

        if let nextLineInsertionResult = checkNextLineInsertion(
            currentText: currentText,
            previousText: previousText,
            cursorPosition: cursorPosition,
            language: language,
            tabSize: tabSize,
            changes: changes
        ) {
            return nextLineInsertionResult
        }

        if let pairedCheckResult = checkPaired(
            currentText: currentText,
            previousText: previousText,
            cursorPosition: cursorPosition,
            language: language,
            tabSize: tabSize,
            changes: changes
        ) {
            return pairedCheckResult
        }

        if let autocompleteSuggestionsResult = getAutocompleteSuggestions(
            currentText: currentText,
            previousText: previousText,
            cursorPosition: cursorPosition,
            language: language
        ) {
            return autocompleteSuggestionsResult
        }

        return (text: currentText, position: cursorPosition, autocomplete: nil)
    }

    func countTabSize(text: String) -> Int {
        var minTabSize = Int.max

        text.enumerateLines { line, _ in
            let leadingSpaces = line.prefix(while: { $0 == " " }).count
            // Update the minimum tab size if this line has leading spaces and it's fewer than any previous line
            if leadingSpaces > 0 {
                minTabSize = min(minTabSize, leadingSpaces)
            }
        }

        return minTabSize == Int.max ? 4 : minTabSize
    }

    private func hideCodeCompletion() {
        currentCodeCompletionTableViewController?.view.removeFromSuperview()
        currentCodeCompletionTableViewController?.removeFromParent()
        currentCodeCompletionTableViewController = nil
    }

    // swiftlint:disable:next function_parameter_count
    private func presentCodeCompletion(
        suggestions: [String],
        prefix: String,
        cursorPosition: Int,
        inViewController viewController: UIViewController,
        textView: UITextView,
        suggestionsDelegate: CodeCompletionDelegate
    ) {
        if currentCodeCompletionTableViewController == nil {
            let codeCompletionTableViewController = CodeCompletionTableViewController()
            viewController.addChild(codeCompletionTableViewController)
            textView.addSubview(codeCompletionTableViewController.view)
            codeCompletionTableViewController.delegate = suggestionsDelegate
            currentCodeCompletionTableViewController = codeCompletionTableViewController
        }

        currentCodeCompletionTableViewController?.suggestions = suggestions
        currentCodeCompletionTableViewController?.prefix = prefix

        if let selectedRange = textView.selectedTextRange {
            // `caretRect` is in the `textView` coordinate space.
            let caretRect = textView.caretRect(for: selectedRange.end)

            var suggestionsFrameMinX = caretRect.minX
            var suggestionsFrameMinY = caretRect.maxY

            let suggestionsHeight = currentCodeCompletionTableViewController?.suggestionsHeight ?? 0

            // Check if we need to move suggestionsFrame
            if suggestionsFrameMinY + suggestionsHeight > (textView.frame.maxY - textView.frame.origin.y) {
                suggestionsFrameMinY = caretRect.minY - suggestionsHeight
            }

            if suggestionsFrameMinX + 100 > (textView.frame.maxX - textView.frame.origin.x) {
                suggestionsFrameMinX = (textView.frame.maxX - textView.frame.origin.x - 102)
            }

            currentCodeCompletionTableViewController?.view.frame = CGRect(
                x: suggestionsFrameMinX,
                y: suggestionsFrameMinY,
                width: 100,
                height: suggestionsHeight
            )
        }
    }

    func textRangeFrom(position: Int, textView: UITextView) -> UITextRange {
        let firstCharacterPosition = textView.beginningOfDocument
        let characterPosition = textView.position(from: firstCharacterPosition, offset: position).require()
        let characterRange = textView.textRange(from: characterPosition, to: characterPosition).require()
        return characterRange
    }

    func insertAtCurrentPosition(symbols: String, textView: UITextView) {
        guard let selectedRange = textView.selectedTextRange else {
            return
        }

        if selectedRange.isEmpty {
            let cursorPosition = textView.offset(from: textView.beginningOfDocument, to: selectedRange.start)
            var text = textView.text ?? ""
            text.insert(contentsOf: symbols, at: text.index(text.startIndex, offsetBy: cursorPosition))
            textView.text = text
            // Import here to update selectedTextRange before calling textViewDidChange #APPS-2352
            textView.selectedTextRange = textRangeFrom(position: cursorPosition + symbols.count, textView: textView)
        } else {
            textView.replace(selectedRange, withText: symbols)
        }

        // Manually call textViewDidChange, because when manually setting the text of a UITextView with code,
        // the textViewDidChange: method does not get called.
        textView.delegate?.textViewDidChange?(textView)
    }

    // swiftlint:disable:next function_parameter_count
    func analyzeAndComplete(
        textView: UITextView,
        previousText: String,
        language: CodeLanguage,
        tabSize: Int,
        inViewController viewController: UIViewController,
        suggestionsDelegate: CodeCompletionDelegate
    ) {
        guard let selectedRange = textView.selectedTextRange else {
            return
        }

        let cursorPosition = textView.offset(from: textView.beginningOfDocument, to: selectedRange.start)

        let analyzed = analyze(
            currentText: textView.text,
            previousText: previousText,
            cursorPosition: cursorPosition,
            language: language,
            tabSize: tabSize
        )

        if textView.text != analyzed.text {
            textView.text = analyzed.text
            textView.delegate?.textViewDidChange?(textView)
        }

        if textView.selectedTextRange != textRangeFrom(position: analyzed.position, textView: textView) {
            textView.selectedTextRange = textRangeFrom(position: analyzed.position, textView: textView)
        }

        if let autocomplete = analyzed.autocomplete {
            if autocomplete.suggestions.isEmpty {
                hideCodeCompletion()
            } else {
                presentCodeCompletion(
                    suggestions: autocomplete.suggestions,
                    prefix: autocomplete.prefix,
                    cursorPosition: analyzed.position,
                    inViewController: viewController,
                    textView: textView,
                    suggestionsDelegate: suggestionsDelegate
                )
            }
        } else {
            hideCodeCompletion()
        }
    }
}
