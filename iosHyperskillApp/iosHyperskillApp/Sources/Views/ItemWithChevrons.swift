import SwiftUI


struct ItemWithChevrons<Content: View>: View {
    let onMoveUp: (() -> Void)?
    let onMoveDown: (() -> Void)?

    let content: Content

    init(
        onMoveUp: (() -> Void)?,
        onMoveDown: (() -> Void)?,
        @ViewBuilder content: () -> Content
    ) {
        self.onMoveUp = onMoveUp
        self.onMoveDown = onMoveDown
        self.content = content()
    }

    var body: some View {
        HStack(spacing: LayoutInsets.smallInset) {
            content

            VStack(spacing: LayoutInsets.defaultInset) {
                ChevronIcon(direction: .up, onTap: onMoveUp ?? {})
                    .disabled(onMoveUp == nil)

                ChevronIcon(direction: .down, onTap: onMoveDown ?? {})
                    .disabled(onMoveDown == nil)
            }
        }
        .padding()
        .addBorder(color: Color(ColorPalette.onSurfaceAlpha12), cornerRadius: 6)
    }
}

struct ItemWithChevrons_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ItemWithChevrons(
                onMoveUp: nil,
                onMoveDown: {},
                content: {
                    Text("test")
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
            )
            ItemWithChevrons(
                onMoveUp: {},
                onMoveDown: {},
                content: {
                    Text("test")
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
            )
            ItemWithChevrons(
                onMoveUp: {},
                onMoveDown: nil,
                content: {
                    Text("test")
                        .frame(maxWidth: .infinity, alignment: .leading)
                }
            )
        }
        .previewLayout(.sizeThatFits)
    }
}
