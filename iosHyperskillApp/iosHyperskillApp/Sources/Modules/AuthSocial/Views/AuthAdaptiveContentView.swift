import SwiftUI

extension AuthAdaptiveContentView {
    struct Appearance {
        let contentMaxWidth: CGFloat = 400

        var insets = LayoutInsets(horizontal: LayoutInsets.defaultInset).edgeInsets
    }
}

struct AuthAdaptiveContentView<Content>: View where Content: View {
    let appearance: Appearance

    var content: (UserInterfaceSizeClass?) -> Content

    var onViewDidAppear: (() -> Void)?

    @Environment(\.horizontalSizeClass) private var horizontalSizeClass

    init(
        appearance: Appearance = Appearance(),
        onViewDidAppear: (() -> Void)? = nil,
        @ViewBuilder content: @escaping (UserInterfaceSizeClass?) -> Content
    ) {
        self.appearance = appearance
        self.onViewDidAppear = onViewDidAppear
        self.content = content
    }

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: onViewDidAppear)

            BackgroundView()

            VerticalCenteredScrollView(showsIndicators: false) {
                VStack(spacing: 0) {
                    if horizontalSizeClass == .regular {
                        Spacer()
                    }

                    content(horizontalSizeClass)

                    Spacer()
                }
                .frame(maxWidth: appearance.contentMaxWidth)
                .padding(appearance.insets)
            }
        }
    }
}

struct AuthAdaptiveContentView_Previews: PreviewProvider {
    static var previews: some View {
        AuthAdaptiveContentView { horizontalSizeClass in
            AuthLogoView(logoWidthHeight: 48)
                .padding(horizontalSizeClass == .regular ? .bottom : .vertical, 48)
        }
    }
}
