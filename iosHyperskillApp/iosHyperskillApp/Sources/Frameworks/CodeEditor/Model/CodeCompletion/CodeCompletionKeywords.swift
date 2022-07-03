import Foundation

enum CodeCompletionKeywords {
    private static let keywordsDictionary: [String: [String]] = {
        guard let path = Bundle.main.path(forResource: "code-completion-keywords", ofType: "plist"),
              let result = NSDictionary(contentsOfFile: path) as? [String: [String]]
        else {
            return [:]
        }
        return result
    }()

    private static let python = keywordsDictionary["python"]
    private static let cpp = keywordsDictionary["cpp"]
    private static let cs = keywordsDictionary["cs"]
    private static let java = keywordsDictionary["java"]
    private static let ruby = keywordsDictionary["ruby"]
    private static let sql = keywordsDictionary["sql"]
    private static let kotlin = keywordsDictionary["kotlin"]
    private static let js = keywordsDictionary["js"]
    private static let r = keywordsDictionary["r"]
    private static let haskell = keywordsDictionary["haskell"]

    static func autocomplete(for text: String, language: CodeLanguage) -> [String] {
        let keywords: [String] = { () -> [String]? in
            switch language {
            case .python, .python31:
                return python
            case .cpp, .cpp11, .c, .cValgrind:
                return cpp
            case .cs, .csMono:
                return cs
            case .java, .java8, .java9, .java11, .java17:
                return java
            case .javascript:
                return js
            case .ruby:
                return ruby
            case .sql:
                return sql
            case .haskell, .haskell7, .haskell8, .haskell88:
                return haskell
            case .r:
                return r
            case .kotlin:
                return kotlin
            default:
                return nil
            }
        }() ?? []

        return keywords.filter { $0.indexOf(text) == 0 && $0.count > text.count }
    }
}
