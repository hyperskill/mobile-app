import SwiftUI

extension HomeWidgetCountView {
    struct Appearance {
        let countMinWidthHeight: CGFloat = 32
    }
}

struct HomeWidgetCountView: View {
    private(set) var appearance = Appearance()

    let countText: String

    let description: String

    var body: some View {
        HStack(spacing: LayoutInsets.smallInset) {
            Text(countText)
                .padding(LayoutInsets.smallInset)
                .frame(
                    minWidth: appearance.countMinWidthHeight,
                    minHeight: appearance.countMinWidthHeight
                )
                .font(.subheadline)
                .foregroundColor(.primaryText)
                .background(Color(ColorPalette.overlayBlueAlpha12))
                .cornerRadius(LayoutInsets.smallInset)

            Text(description)
                .font(.subheadline)
                .foregroundColor(.secondaryText)
        }
    }
}

#Preview {
    HomeWidgetCountView(
        countText: "25",
        description: "problems at the hard level to solve"
    )
}
