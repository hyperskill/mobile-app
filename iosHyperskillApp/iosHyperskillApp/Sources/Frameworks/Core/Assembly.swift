import SwiftUI

protocol Assembly {
    associatedtype View: SwiftUI.View

    func makeModule() -> View
}

protocol UIKitAssembly: AnyObject {
    func makeModule() -> UIViewController
}
