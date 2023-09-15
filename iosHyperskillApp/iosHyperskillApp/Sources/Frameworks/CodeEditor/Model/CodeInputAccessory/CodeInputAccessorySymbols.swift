import Foundation

enum CodeInputAccessorySymbols {
    private static let python = [
        "self", ":", "=", ".", "_", ",", "(", ")", "[", "]", "'", "*", "/", "+", "%", ">", "<", "and", "or", "not", "&",
        "|", "#", "\\", "{", "}", "@", "^", "~", ";"
    ]
    private static let cpp = [
        "=", ".", "_", ";", ",", "{", "}", "(", ")", "[", "]", "'", "\"", "*", "+", "/", "%", ">", "<", "&", "|", ":",
        "@", "^", "\\", "~"
    ]
    private static let sql = [
        "(", ")", "'", "`", ".", ",", "*", "/", "+", "-", ";", "<", ">", "=", "#", "@"
    ]
    private static let java = cpp

    static func symbols(for language: CodeLanguage) -> [String] {
        switch language {
        case .python, .python3, .python31:
            return python
        case .java, .java8, .java9, .java11, .java17:
            return java
        case .sql:
            return sql
        default:
            return cpp
        }
    }
}
