import XCTest

@testable import iosHyperskillApp

final class CodePlaygroundManagerCountTabSizeTests: XCTestCase {
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

    func testBasicFunctionality() {
        let text = "    func testExample() {\n        var x = 10\n  return x\n}"
        XCTAssertEqual(manager.countTabSize(text: text), 2)
    }

    func testNoLeadingSpaces() {
        let text = "func testExample() {\nvar x = 10\nreturn x\n}"
        XCTAssertEqual(manager.countTabSize(text: text), 4)  // Default to 4 since no spaces
    }

    func testMixedContent() {
        let text = "func testExample() {\n     var x = 10\n    return x\n}"
        XCTAssertEqual(manager.countTabSize(text: text), 4)
    }

    func testEmptyText() {
        let text = ""
        XCTAssertEqual(manager.countTabSize(text: text), 4)  // Default to 4 since empty
    }

    func testTextWithNoSpaces() {
        let text = "func testExample(){\nvar x=10\nreturn x\n}"
        XCTAssertEqual(manager.countTabSize(text: text), 4)  // Default to 4 since no spaces
    }

    func testLinesWithNoCharacters() {
        let text = "\n\n\n   \n"
        XCTAssertEqual(manager.countTabSize(text: text), 3)
    }
}
