import SwiftUI
import XCTest

@testable import iosHyperskillApp

final class IntrospectViewControllerTests: XCTestCase {
    private struct ViewControllerTestView: View {
        let spy: () -> Void

        var body: some View {
            NavigationView {
                VStack {
                    EmptyView()
                }
            }
            .introspectViewController { viewController in
                self.spy()
            }
        }
    }

    private struct HostingControllerTestView: View {
        let spy: () -> Void

        var body: some View {
            NavigationView {
                VStack {
                    EmptyView()
                }
            }
            .introspectHostingController { (hostingController: UIHostingController<HostingControllerTestView>) in
                self.spy()
            }
        }
    }

    func testViewController() {
        let expectation = XCTestExpectation()
        let view = ViewControllerTestView(spy: {
            expectation.fulfill()
        })
        IntrospectTestUtils.present(view: view)
        wait(for: [expectation], timeout: IntrospectTestUtils.Constants.timeout)
    }

    func testHostingController() {
        let expectation = XCTestExpectation()
        let view = HostingControllerTestView(spy: {
            expectation.fulfill()
        })
        IntrospectTestUtils.present(view: view)
        wait(for: [expectation], timeout: IntrospectTestUtils.Constants.timeout)
    }
}
