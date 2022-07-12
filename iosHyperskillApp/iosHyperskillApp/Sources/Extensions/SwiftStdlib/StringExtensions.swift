import Foundation

extension String {
    func trimmedNonEmptyOrNil() -> String? {
        let trimmedString = self.trimmed()
        return trimmedString.isEmpty ? nil : trimmedString
    }

    // MARK: IndexOf

    func indexOf(_ target: String) -> Int? {
        if let range = self.range(of: target) {
            return self.distance(from: startIndex, to: range.lowerBound)
        } else {
            return nil
        }
    }

    func lastIndexOf(_ target: String) -> Int? {
        if let range = self.range(of: target, options: .backwards) {
            return self.distance(from: startIndex, to: range.lowerBound)
        } else {
            return nil
        }
    }

    // MARK: - SafeSubscript -

    /// Safely subscript string with index.
    ///
    ///     "Hello World!"[safe: 3] -> "l"
    ///     "Hello World!"[safe: 20] -> nil
    ///
    /// - Parameter index: index.
    subscript(safe index: Int) -> Character? {
        guard index >= 0 && index < count else {
            return nil
        }

        return self[self.index(startIndex, offsetBy: index)]
    }

    /// Safely subscript string within a half-open range.
    ///
    ///     "Hello World!"[safe: 6..<11] -> "World"
    ///     "Hello World!"[safe: 21..<110] -> nil
    ///
    /// - Parameter range: Half-open range.
    subscript(safe range: CountableRange<Int>) -> String? {
        guard let lowerIndex = self.index(
            startIndex,
            offsetBy: max(0, range.lowerBound),
            limitedBy: endIndex
        )
        else {
            return nil
        }

        guard let upperIndex = self.index(
            lowerIndex,
            offsetBy: range.upperBound - range.lowerBound,
            limitedBy: endIndex
        )
        else {
            return nil
        }

        return String(self[lowerIndex..<upperIndex])
    }

    /// Safely subscript string within a closed range.
    ///
    ///     "Hello World!"[safe: 6...11] -> "World!"
    ///     "Hello World!"[safe: 21...110] -> nil
    ///
    /// - Parameter range: Closed range.
    subscript(safe range: ClosedRange<Int>) -> String? {
        guard let lowerIndex = self.index(
            startIndex,
            offsetBy: max(0, range.lowerBound),
            limitedBy: endIndex
        )
        else {
            return nil
        }

        guard let upperIndex = self.index(
            lowerIndex,
            offsetBy: range.upperBound - range.lowerBound,
            limitedBy: endIndex
        )
        else {
            return nil
        }

        return String(self[lowerIndex...upperIndex])
    }

    // MARK: - Whitespaces -

    var containsWhitespace: Bool {
        self.rangeOfCharacter(from: .whitespacesAndNewlines) != nil
    }

    /// String with no spaces or new lines in beginning and end.
    func trimmed() -> String {
        self.trimmingCharacters(in: .whitespacesAndNewlines)
    }

    func condenseWhitespace() -> String {
        let components = self.components(separatedBy: .whitespacesAndNewlines)
        return components.filter { !$0.isEmpty }.joined(separator: " ")
    }

    func normalizeNewline() -> String {
        self.replacingOccurrences(of: "\n+", with: "\n", options: .regularExpression, range: nil)
    }
}
