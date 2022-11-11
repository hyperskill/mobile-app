import SwiftUI

private struct PanModalViewControllerModifier: ViewModifier {
    @Binding private var isPresented: Bool

    @State private var panModalViewController: PanModalPresentableViewController?

    private let panModalViewControllerFactory: () -> PanModalPresentableViewController

    @EnvironmentObject private var panModalPresenter: PanModalPresenter

    init(
        isPresented: Binding<Bool>,
        panModalViewControllerFactory: @escaping () -> PanModalPresentableViewController
    ) {
        self._isPresented = isPresented
        self.panModalViewControllerFactory = panModalViewControllerFactory
    }

    func body(content: Content) -> some View {
        content.onChange(of: isPresented, perform: updatePresentation)
    }

    private func updatePresentation(_ isPresented: Bool) {
        defer {
            panModalViewController?.onDisappear = {
                self.isPresented = false
            }
        }

        if isPresented {
            panModalViewController = panModalViewControllerFactory()
            panModalPresenter.presentPanModal(panModalViewController.require())
        } else {
            panModalViewController?.dismiss(animated: true)
        }
    }
}

private struct PanModalSwiftUIViewControllerModifier<T: Any, ContentView: View>: ViewModifier {
    @Binding private var isPresented: Bool

    private let contentView: () -> ContentView

    @State private var panModalViewController: PanModalSwiftUIViewController<ContentView>?

    @EnvironmentObject private var panModalPresenter: PanModalPresenter

    init(isPresented: Binding<Bool>, @ViewBuilder contentView: @escaping () -> ContentView) {
        self._isPresented = isPresented
        self.contentView = contentView
    }

    func body(content: Content) -> some View {
        content.onChange(of: isPresented, perform: updatePresentation)
    }

    private func updatePresentation(_ isPresented: Bool) {
        if isPresented {
            panModalViewController = PanModalSwiftUIViewController(isPresented: $isPresented, content: contentView)
            panModalPresenter.presentPanModal(panModalViewController.require())
        } else {
            panModalViewController?.dismiss(animated: true)
        }
    }
}

// MARK: - View+PanModal -

extension View {
    func panModal<ContentView: View>(
        isPresented: Binding<Bool>,
        @ViewBuilder contentView: @escaping () -> ContentView
    ) -> some View {
        modifier(
            PanModalSwiftUIViewControllerModifier<Any, ContentView>(
                isPresented: isPresented,
                contentView: contentView
            )
        )
    }

    func panModal(
        isPresented: Binding<Bool>,
        panModal: @escaping () -> PanModalPresentableViewController
    ) -> some View {
        modifier(
            PanModalViewControllerModifier(
                isPresented: isPresented,
                panModalViewControllerFactory: panModal
            )
        )
    }
}
