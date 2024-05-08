import XCTest

@testable import iosHyperskillApp

final class CodePlaygroundGetCurrentTokenTests: XCTestCase {
    let text = "def main()"

    // swiftlint:disable:next implicitly_unwrapped_optional
    var manager: CodePlaygroundManager!

    override func setUp() {
        super.setUp()
        manager = CodePlaygroundManager()
    }

    override func tearDown() {
        manager = nil
        super.tearDown()
    }

    func testGetCurrentTokenAtStart() {
        XCTAssertEqual(manager.getCurrentToken(text: text, cursorPosition: 0), "def")
    }

    func testGetCurrentTokenAtEnd() {
        XCTAssertEqual(manager.getCurrentToken(text: text, cursorPosition: 10), "")
    }

    func testGetCurrentTokenWhenCursorAtEndOfWord() {
        XCTAssertEqual(manager.getCurrentToken(text: "def main", cursorPosition: 8), "main")
    }

    func testGetCurrentTokenWhenCursorAfterWord() {
        XCTAssertEqual(manager.getCurrentToken(text: text, cursorPosition: 3), "def")
    }

    func testGetCurrentTokenWhenCursorBeforeWord() {
        XCTAssertEqual(manager.getCurrentToken(text: text, cursorPosition: 4), "main")
    }

    func testGetCurrentTokenWhenCursorBetweenWords() {
        XCTAssertEqual(manager.getCurrentToken(text: text, cursorPosition: 2), "def")
    }

    func testGetCurrentTokenWhenCursorBetweenNotAllowedCharacters() {
        XCTAssertEqual(manager.getCurrentToken(text: text, cursorPosition: 9), "")
    }

    func testGetCurrentTokenWhenCursorOutOfBounds() {
        XCTAssertEqual(manager.getCurrentToken(text: text, cursorPosition: -1), "")
        XCTAssertEqual(manager.getCurrentToken(text: text, cursorPosition: 100), "")
    }

    func testGetCurrentTokenWhenTextIsEmpty() {
        XCTAssertEqual(manager.getCurrentToken(text: "", cursorPosition: 0), "")
    }
}
