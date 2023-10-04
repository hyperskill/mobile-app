import SwiftUI
import XCTest

@testable import iosHyperskillApp

final class IntrospectScrollViewTests: XCTestCase {
    private struct ScrollViewTestView: View {
        let spy: () -> Void

        var body: some View {
            NavigationView {
                ScrollView {
                    VStack {
                        EmptyView()
                    }
                    .introspectScrollView { _ in
                        self.spy()
                    }
                }
            }
        }
    }

    func testScrollView() {
        let expectation = XCTestExpectation()
        let view = ScrollViewTestView(spy: {
            expectation.fulfill()
        })
        IntrospectTestUtils.present(view: view)
        wait(for: [expectation], timeout: IntrospectTestUtils.Constants.timeout)
    }
}
