import XCTest

@testable import iosHyperskillApp

final class CodePlaygroundShouldMakeTabLineAfterTests: XCTestCase {
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

    // MARK: Python tests

    func testShouldMakeNewLineAfterTabForPythonColon() {
        let result = manager.shouldMakeTabLineAfter(symbol: ":", language: .python)
        XCTAssertTrue(result.shouldMakeNewLine)
        XCTAssertFalse(result.paired)
    }

    func testShouldNotMakeNewLineAfterNoneColonSymbolForPython() {
        let result = manager.shouldMakeTabLineAfter(symbol: "{", language: .python)
        XCTAssertFalse(result.shouldMakeNewLine)
        XCTAssertFalse(result.paired)
    }

    // MARK: C tests

    func testShouldMakePairedNewLineAfterOpeningCurlyBraceForC() {
        let result = manager.shouldMakeTabLineAfter(symbol: "{", language: .c)
        XCTAssertTrue(result.shouldMakeNewLine)
        XCTAssertTrue(result.paired)
    }

    func testShouldNotMakeNewLineAfterNoneOpeningCurlyBraceForC() {
        let result = manager.shouldMakeTabLineAfter(symbol: "}", language: .c)
        XCTAssertFalse(result.shouldMakeNewLine)
        XCTAssertFalse(result.paired)
    }

    // MARK: C# tests

    func testShouldMakePairedNewLineAfterOpeningCurlyBraceForCSharp() {
        let result = manager.shouldMakeTabLineAfter(symbol: "{", language: .cs)
        XCTAssertTrue(result.shouldMakeNewLine)
        XCTAssertTrue(result.paired)
    }

    func testShouldNotMakeNewLineAfterNoneOpeningCurlyBraceForCSharp() {
        let result = manager.shouldMakeTabLineAfter(symbol: "}", language: .cs)
        XCTAssertFalse(result.shouldMakeNewLine)
        XCTAssertFalse(result.paired)
    }

    // MARK: C++ tests

    func testShouldMakePairedNewLineAfterOpeningCurlyBraceForCpp() {
        let resultCpp = manager.shouldMakeTabLineAfter(symbol: "{", language: .cpp)
        let resultCpp11 = manager.shouldMakeTabLineAfter(symbol: "{", language: .cpp11)
        XCTAssertTrue(resultCpp.shouldMakeNewLine)
        XCTAssertTrue(resultCpp.paired)
        XCTAssertTrue(resultCpp11.shouldMakeNewLine)
        XCTAssertTrue(resultCpp11.paired)
    }

    func testShouldNotMakeNewLineAfterNoneOpeningCurlyBraceForCpp() {
        let resultCpp = manager.shouldMakeTabLineAfter(symbol: "}", language: .cpp)
        let resultCpp11 = manager.shouldMakeTabLineAfter(symbol: "}", language: .cpp11)
        XCTAssertFalse(resultCpp.shouldMakeNewLine)
        XCTAssertFalse(resultCpp.paired)
        XCTAssertFalse(resultCpp11.shouldMakeNewLine)
        XCTAssertFalse(resultCpp11.paired)
    }

    // MARK: Java tests

    func testShouldMakePairedNewLineAfterOpeningCurlyBraceForJava() {
        let languages: [CodeLanguage] = [.java, .java8, .java9, .java11]
        languages.forEach { language in
            let result = manager.shouldMakeTabLineAfter(symbol: "{", language: language)
            XCTAssertTrue(result.shouldMakeNewLine)
            XCTAssertTrue(result.paired)
        }
    }

    func testShouldNotMakeNewLineAfterNoneOpeningCurlyBraceForJava() {
        let languages: [CodeLanguage] = [.java, .java8, .java9, .java11]
        languages.forEach { language in
            let result = manager.shouldMakeTabLineAfter(symbol: "}", language: language)
            XCTAssertFalse(result.shouldMakeNewLine)
            XCTAssertFalse(result.paired)
        }
    }

    // MARK: Kotlin tests

    func testShouldMakePairedNewLineAfterOpeningCurlyBraceForKotlin() {
        let result = manager.shouldMakeTabLineAfter(symbol: "{", language: .kotlin)
        XCTAssertTrue(result.shouldMakeNewLine)
        XCTAssertTrue(result.paired)
    }

    func testShouldNotMakeNewLineAfterNoneOpeningCurlyBraceForKotlin() {
        let result = manager.shouldMakeTabLineAfter(symbol: "}", language: .kotlin)
        XCTAssertFalse(result.shouldMakeNewLine)
        XCTAssertFalse(result.paired)
    }

    // MARK: Swift tests

    func testShouldMakePairedNewLineAfterOpeningCurlyBraceForSwift() {
        let result = manager.shouldMakeTabLineAfter(symbol: "{", language: .swift)
        XCTAssertTrue(result.shouldMakeNewLine)
        XCTAssertTrue(result.paired)
    }

    func testShouldNotMakeNewLineAfterNoneOpeningCurlyBraceForSwift() {
        let result = manager.shouldMakeTabLineAfter(symbol: "}", language: .swift)
        XCTAssertFalse(result.shouldMakeNewLine)
        XCTAssertFalse(result.paired)
    }

    // MARK: Rust tests

    func testShouldMakePairedNewLineAfterOpeningCurlyBraceForRust() {
        let result = manager.shouldMakeTabLineAfter(symbol: "{", language: .rust)
        XCTAssertTrue(result.shouldMakeNewLine)
        XCTAssertTrue(result.paired)
    }

    func testShouldNotMakeNewLineAfterNoneOpeningCurlyBraceForRust() {
        let result = manager.shouldMakeTabLineAfter(symbol: "}", language: .rust)
        XCTAssertFalse(result.shouldMakeNewLine)
        XCTAssertFalse(result.paired)
    }

    // MARK: Javascript tests

    func testShouldMakePairedNewLineAfterOpeningCurlyBraceForJavaScript() {
        let result = manager.shouldMakeTabLineAfter(symbol: "{", language: .javascript)
        XCTAssertTrue(result.shouldMakeNewLine)
        XCTAssertTrue(result.paired)
    }

    func testShouldNotMakeNewLineAfterNoneOpeningCurlyBraceForJavaScript() {
        let result = manager.shouldMakeTabLineAfter(symbol: "}", language: .javascript)
        XCTAssertFalse(result.shouldMakeNewLine)
        XCTAssertFalse(result.paired)
    }

    // MARK: Scala tests

    func testShouldMakePairedNewLineAfterOpeningCurlyBraceForScala() {
        let result = manager.shouldMakeTabLineAfter(symbol: "{", language: .scala)
        XCTAssertTrue(result.shouldMakeNewLine)
        XCTAssertTrue(result.paired)
    }

    func testShouldNotMakeNewLineAfterNoneOpeningCurlyBraceForScala() {
        let result = manager.shouldMakeTabLineAfter(symbol: "}", language: .scala)
        XCTAssertFalse(result.shouldMakeNewLine)
        XCTAssertFalse(result.paired)
    }

    // MARK: Go tests

    func testShouldMakePairedNewLineAfterOpeningCurlyBraceForGo() {
        let result = manager.shouldMakeTabLineAfter(symbol: "{", language: .go)
        XCTAssertTrue(result.shouldMakeNewLine)
        XCTAssertTrue(result.paired)
    }

    func testShouldNotMakeNewLineAfterNoneOpeningCurlyBraceForGo() {
        let result = manager.shouldMakeTabLineAfter(symbol: "}", language: .go)
        XCTAssertFalse(result.shouldMakeNewLine)
        XCTAssertFalse(result.paired)
    }

    // MARK: Perl tests

    func testShouldMakePairedNewLineAfterOpeningCurlyBraceForPerl() {
        let result = manager.shouldMakeTabLineAfter(symbol: "{", language: .perl)
        XCTAssertTrue(result.shouldMakeNewLine)
        XCTAssertTrue(result.paired)
    }

    func testShouldNotMakeNewLineAfterNoneOpeningCurlyBraceForPerl() {
        let result = manager.shouldMakeTabLineAfter(symbol: "}", language: .perl)
        XCTAssertFalse(result.shouldMakeNewLine)
        XCTAssertFalse(result.paired)
    }

    // MARK: PHP tests

    func testShouldMakePairedNewLineAfterOpeningCurlyBraceForPHP() {
        let result = manager.shouldMakeTabLineAfter(symbol: "{", language: .php)
        XCTAssertTrue(result.shouldMakeNewLine)
        XCTAssertTrue(result.paired)
    }

    func testShouldNotMakeNewLineAfterNoneOpeningCurlyBraceForPHP() {
        let result = manager.shouldMakeTabLineAfter(symbol: "}", language: .php)
        XCTAssertFalse(result.shouldMakeNewLine)
        XCTAssertFalse(result.paired)
    }

    // MARK: ASM tests

    func testShouldNotMakeNewTabLineForAsm() {
        let expectedTuple = (shouldMakeNewLine: false, paired: false)
        let resultAsm32 = manager.shouldMakeTabLineAfter(symbol: "{", language: .asm32)
        let resultAsm64 = manager.shouldMakeTabLineAfter(symbol: "{", language: .asm32)
        XCTAssertFalse(resultAsm32.shouldMakeNewLine)
        XCTAssertFalse(resultAsm32.paired)
        XCTAssertFalse(resultAsm64.shouldMakeNewLine)
        XCTAssertFalse(resultAsm64.paired)
    }
}
