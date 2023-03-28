import SwiftUI

extension HomeFreemiumBadgeView {
    struct Appearance {
        let padding = LayoutInsets(horizontal: 8, vertical: 4)
        let cornerRadius: CGFloat = 4
    }
}

struct HomeFreemiumBadgeView: View {
    private(set) var appearance = Appearance()

    let text: String

    var body: some View {
        Text(text)
            .font(.caption)
            .foregroundColor(Color(ColorPalette.overlayViolet))
            .padding(appearance.padding.edgeInsets)
            .background(Color(ColorPalette.overlayVioletAlpha12))
            .cornerRadius(appearance.cornerRadius)
    }
}

struct HomeFreemiumBadgeView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            HomeFreemiumBadgeView(text: "Solve unlimited")

            HomeFreemiumBadgeView(text: "Solve unlimited")
                .preferredColorScheme(.dark)
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
