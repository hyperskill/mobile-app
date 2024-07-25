import XCTest

@testable import iosHyperskillApp

final class UIStackViewExtensionTests: XCTestCase {
    func testRemoveAllArrangedSubviews() {
        let stackView = UIStackView()
        let view1 = UIView()
        stackView.addArrangedSubview(view1)
        let view2 = UIView()
        stackView.addArrangedSubview(view2)

        stackView.removeAllArrangedSubviews()

        XCTAssertTrue(stackView.arrangedSubviews.isEmpty)
    }
}
