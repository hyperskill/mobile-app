import SwiftUI

@available(iOS 16.0, *)
struct FlowLayout: Layout {
    var spacing: CGFloat = LayoutInsets.smallInset

    func sizeThatFits(proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) -> CGSize {
        let containerWidth = proposal.width ?? .infinity
        let sizes = subviews.map { $0.sizeThatFits(.init(width: proposal.width, height: proposal.height)) }
        return layout(sizes: sizes, containerWidth: containerWidth).size
    }

    func placeSubviews(in bounds: CGRect, proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) {
        let sizes = subviews.map { $0.sizeThatFits(.init(width: proposal.width, height: proposal.height)) }
        let offsets = layout(sizes: sizes, containerWidth: bounds.width).offsets
        for (offset, subview) in zip(offsets, subviews) {
            subview.place(
                at: .init(x: offset.x + bounds.minX, y: offset.y + bounds.minY),
                proposal: .init(width: proposal.width, height: proposal.height)
            )
        }
    }

    private func layout(
        sizes: [CGSize],
        containerWidth: CGFloat
    ) -> (offsets: [CGPoint], size: CGSize) {
        var result: [CGPoint] = []

        var currentPosition: CGPoint = .zero

        var lineHeight: CGFloat = 0

        var maxX: CGFloat = 0
        for size in sizes {
            if currentPosition.x + size.width > containerWidth {
                currentPosition.x = 0
                currentPosition.y += lineHeight + spacing
                lineHeight = 0
            }
            result.append(currentPosition)
            currentPosition.x += size.width

            maxX = max(maxX, currentPosition.x)
            currentPosition.x += spacing
            lineHeight = max(lineHeight, size.height)
        }

        return (result, .init(width: maxX, height: currentPosition.y + lineHeight))
    }
}

@available(iOS 16.0, *)
#Preview {
    FlowLayout {
        ForEach(
            [
                "print",
                "test",
                "There is a cat on the keyboard, it is true or false",
                "Typing messages out of the blue",
                "print"
            ],
            id: \.self
        ) { suggestion in
            Text(suggestion)
                .padding()
                .background(Color.yellow)
                .cornerRadius(8)
        }
    }
    .background(Color.green)
    .padding()
}
