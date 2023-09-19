import shared
import SwiftUI

extension BadgeRankView {
    enum Appearance {
        static func foregroundColor(for rank: BadgeRank) -> Color {
            let uiColor: UIColor = {
                switch rank {
                case .apprentice, .expert:
                    return ColorPalette.overlayBlueBrand
                case .master:
                    return ColorPalette.overlayBlue
                case .legendary:
                    return .primaryText
                case .locked, .unknown:
                    return .disabledText
                default:
                    return .disabledText
                }
            }()

            return Color(uiColor)
        }
    }
}

struct BadgeRankView: View {
    let text: String
    let rank: BadgeRank

    var body: some View {
        Text(text)
            .foregroundColor(Appearance.foregroundColor(for: rank))
            .font(.headline)
    }
}

struct BadgeRankView_Previews: PreviewProvider {
    static var previews: some View {
        BadgeRankView(
            text: "Apprentice",
            rank: .apprentice
        )
    }
}
