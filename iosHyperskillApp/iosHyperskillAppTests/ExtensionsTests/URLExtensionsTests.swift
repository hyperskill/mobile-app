@testable
import iosHyperskillApp

import XCTest

class URLExtensionsTests: XCTestCase {
    var url = URL(string: "https://www.google.com")!
    let params = ["q": "search iosHyperskillApp"]
    let queryUrl = URL(string: "https://www.google.com?q=search%20iosHyperskillApp")!

    func testAppendingQueryParameters() {
        XCTAssertEqual(self.url.appendingQueryParameters(self.params), self.queryUrl)
    }

    func testAppendQueryParameters() {
        self.url.appendQueryParameters(self.params)
        XCTAssertEqual(self.url, self.queryUrl)
    }

    func testValueForQueryKey() {
        let url = URL(string: "https://google.com?code=12345&empty")!

        let codeResult = url.queryValue(for: "code")
        let emtpyResult = url.queryValue(for: "empty")
        let otherResult = url.queryValue(for: "other")

        XCTAssertEqual(codeResult, "12345")
        XCTAssertNil(emtpyResult)
        XCTAssertNil(otherResult)
    }
}
