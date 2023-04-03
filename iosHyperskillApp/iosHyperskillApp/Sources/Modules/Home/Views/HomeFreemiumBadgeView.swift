import SwiftUI

extension HomeFreemiumBadgeView {
    struct Appearance {
        let padding = LayoutInsets(horizontal: 8, vertical: 4)
        let cornerRadius: CGFloat = 4
    }
}

struct HomeFreemiumBadgeView: View {
    private(set) var appearance = Appearance()

    let type: `Type`

    var body: some View {
        Text(type.title)
            .font(.caption)
            .foregroundColor(Color(ColorPalette.overlayViolet))
            .padding(appearance.padding.edgeInsets)
            .background(Color(ColorPalette.overlayVioletAlpha12))
            .cornerRadius(appearance.cornerRadius)
    }

    enum `Type` {
        case solve
        case `repeat`

        fileprivate var title: String {
            switch self {
            case .solve:
                return Strings.Home.solveUnlimited
            case .repeat:
                return Strings.Home.repeatUnlimited
            }
        }
    }
}

struct HomeFreemiumBadgeView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            HomeFreemiumBadgeView(type: .solve)

            HomeFreemiumBadgeView(type: .repeat)
                .preferredColorScheme(.dark)
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
