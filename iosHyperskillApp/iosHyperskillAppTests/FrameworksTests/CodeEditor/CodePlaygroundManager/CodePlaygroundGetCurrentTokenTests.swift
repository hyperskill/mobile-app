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

    func testGetCurrentTokenWithMultipleTokens() {
        XCTAssertEqual(manager.getCurrentToken(text: "first second third", cursorPosition: 6), "second")
    }

    func testGetCurrentTokenConsecutiveNotAllowedCharacters() {
        XCTAssertEqual(manager.getCurrentToken(text: "first!!second", cursorPosition: 6), "")
    }

    func testGetCurrentTokenAtStartOfSpecialCharacters() {
        XCTAssertEqual(manager.getCurrentToken(text: "a_b_c", cursorPosition: 2), "a_b_c")
    }

    func testGetCurrentTokenWithNumericCharacters() {
        XCTAssertEqual(manager.getCurrentToken(text: "def_main(123)", cursorPosition: 10), "123")
    }

    func testGetCurrentTokenJustBeforeNotAllowedCharacter() {
        XCTAssertEqual(manager.getCurrentToken(text: "token#", cursorPosition: 5), "token")
    }
}
