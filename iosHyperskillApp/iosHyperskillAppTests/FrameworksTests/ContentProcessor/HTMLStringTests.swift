@testable
import iosHyperskillApp

import XCTest

final class HTMLStringTests: XCTestCase {
    private let givenStrings = [
        "elif population &gt; 100000:",
        "print(&quot;This is a small city&quot;)",
        "print(&quot;This is a medium city&quot;)",
        "print(&quot;This is a large city&quot;)",
        "print(&quot;This is a million city&quot;)",
        "elif population &gt; 350000:",
        "else:",
        "if population &gt; 1000000:"
    ]
    private let expectedStrings = [
        "elif population > 100000:",
        "print(\"This is a small city\")",
        "print(\"This is a medium city\")",
        "print(\"This is a large city\")",
        "print(\"This is a million city\")",
        "elif population > 350000:",
        "else:",
        "if population > 1000000:"
    ]
    
    func testUnescape() throws {
        for (given, expected) in zip(givenStrings, expectedStrings) {
            let actual = HTMLString.unescape(string: given)
            XCTAssertEqual(actual, expected)
        }
    }
}
