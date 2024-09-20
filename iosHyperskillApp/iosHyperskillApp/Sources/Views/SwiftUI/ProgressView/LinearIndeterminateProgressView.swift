import SwiftUI

extension LinearIndeterminateProgressView {
    struct Appearance {
        let trackTintColor = Color(ColorPalette.onSurfaceAlpha9)
        let progressTintColor = Color.accentColor

        let height: CGFloat = 6
    }
}

struct LinearIndeterminateProgressView: View {
    private(set) var appearance = Appearance()

    @State private var width: CGFloat = 0
    @State private var offset: CGFloat = 0

    @Environment(\.isEnabled) private var isEnabled

    var body: some View {
        Rectangle()
            .foregroundColor(appearance.trackTintColor)
            .readWidth()
            .overlay(
                Rectangle()
                    .foregroundColor(appearance.progressTintColor)
                    .frame(width: width * 0.26, height: appearance.height)
                    .clipShape(Capsule())
                    .offset(x: -width * 0.6, y: 0)
                    .offset(x: width * 1.2 * offset, y: 0)
                    .animation(.default.repeatForever().speed(0.265), value: offset)
                    .onAppear {
                        withAnimation {
                            offset = 1
                        }
                    }
            )
            .clipShape(Capsule())
            .opacity(isEnabled ? 1 : 0)
            .animation(.default, value: isEnabled)
            .frame(height: appearance.height)
            .onPreferenceChange(WidthPreferenceKey.self) { width in
                self.width = width
            }
    }
}

private struct WidthPreferenceKey: PreferenceKey {
    static var defaultValue: CGFloat = 0

    static func reduce(value: inout CGFloat, nextValue: () -> CGFloat) {
        value = max(value, nextValue())
    }
}

private struct ReadWidthModifier: ViewModifier {
    private var sizeView: some View {
        GeometryReader { proxy in
            Color.clear.preference(key: WidthPreferenceKey.self, value: proxy.size.width)
        }
    }

    func body(content: Content) -> some View {
        content.background(sizeView)
    }
}

private extension View {
    func readWidth() -> some View {
        modifier(ReadWidthModifier())
    }
}

#if DEBUG
#Preview {
    LinearIndeterminateProgressView()
}
#endif
