import SwiftUI

extension CardView {
    struct Appearance {
        var backgroundColor = Color(ColorPalette.surface)
        var cornerRadius: CGFloat = 8
    }
}

struct CardView<Content>: View where Content: View {
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
struct CardView_Previews: PreviewProvider {
    static var previews: some View {
        let text = """
Acquire key Python skills to establish a solid foundation for pursuing a career in Backend Development or Data Science.
"""

        Group {
            CardView {
                Text(text)
                    .font(.body)
                    .foregroundColor(.primaryText)
            }

            CardView {
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
