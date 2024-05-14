import XCTest

@testable import iosHyperskillApp

final class CodePlaygroundGetChangesSubstringTests: XCTestCase {
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

    func testInsertionAtStart() {
        let result = manager.getChangesSubstring(currentText: "abcdefg", previousText: "bcdefg")
        XCTAssertEqual(result.changes, "a")
        XCTAssertTrue(result.isInsertion)
    }

    func testInsertionInMiddle() {
        let result = manager.getChangesSubstring(currentText: "abcde", previousText: "abde")
        XCTAssertEqual(result.changes, "c")
        XCTAssertTrue(result.isInsertion)
    }

    func testInsertionAtEnd() {
        let result = manager.getChangesSubstring(currentText: "abcdefg", previousText: "abcdef")
        XCTAssertEqual(result.changes, "g")
        XCTAssertTrue(result.isInsertion)
    }

    func testInsertionAfterSameCharacterStart() {
        let result = manager.getChangesSubstring(currentText: "aaaaabc", previousText: "abc")
        XCTAssertEqual(result.changes, "aaaa")
        XCTAssertTrue(result.isInsertion)
    }

    func testInsertionAfterSameCharacterMiddle() {
        let result = manager.getChangesSubstring(currentText: "abcddddddddeefg", previousText: "abcdefg")
        XCTAssertEqual(result.changes, "ddddddde")
        XCTAssertTrue(result.isInsertion)
    }

    func testInsertionAfterSameCharacterEnd() {
        let result = manager.getChangesSubstring(currentText: "abccccc", previousText: "abc")
        XCTAssertEqual(result.changes, "cccc")
        XCTAssertTrue(result.isInsertion)
    }

    func testDeletionAtStart() {
        let result = manager.getChangesSubstring(currentText: "bcdefg", previousText: "abcdefg")
        XCTAssertEqual(result.changes, "a")
        XCTAssertFalse(result.isInsertion)
    }

    func testDeletionInMiddle() {
        let result = manager.getChangesSubstring(currentText: "abcdefg", previousText: "abcddddddddeefg")
        XCTAssertEqual(result.changes, "ddddddde")
        XCTAssertFalse(result.isInsertion)
    }

    func testDeletionAtEnd() {
        let result = manager.getChangesSubstring(currentText: "abc", previousText: "abcd")
        XCTAssertEqual(result.changes, "d")
        XCTAssertFalse(result.isInsertion)
    }

    func testDeletionOfAllText() {
        let result = manager.getChangesSubstring(currentText: "", previousText: "abcd")
        XCTAssertEqual(result.changes, "abcd")
        XCTAssertFalse(result.isInsertion)
    }
}
