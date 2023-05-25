import SwiftUI

extension TrackSelectionDetailsBlockView {
    struct Appearance {
        let backgroundColor = Color(ColorPalette.surface)
        let cornerRadius: CGFloat = 8

        let spacing: CGFloat = LayoutInsets.defaultInset
    }
}

struct TrackSelectionDetailsBlockView<Content>: View where Content: View {
    private(set) var appearance = Appearance()

    @ViewBuilder var content: () -> Content

    var body: some View {
        Group {
            content()
                .padding()
        }
        .background(appearance.backgroundColor.cornerRadius(appearance.cornerRadius))
    }
}

#if DEBUG
struct TrackSelectionDetailsBlockView_Previews: PreviewProvider {
    static var previews: some View {
        let text = """
Acquire key Python skills to establish a solid foundation for pursuing a career in Backend Development or Data Science.
"""

        Group {
            TrackSelectionDetailsBlockView {
                Text(text)
                    .font(.body)
                    .foregroundColor(.primaryText)
            }

            TrackSelectionDetailsBlockView {
                Text(text)
                    .font(.body)
                    .foregroundColor(.primaryText)
            }
            .preferredColorScheme(.dark)
        }
        .padding()
        .background(Color.systemGroupedBackground)
    }
}
#endif
