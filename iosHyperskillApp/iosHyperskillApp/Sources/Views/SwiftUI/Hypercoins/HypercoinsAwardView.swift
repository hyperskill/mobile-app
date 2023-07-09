import SwiftUI

extension HypercoinsAwardView {
    struct Appearance {
        var titleFont = Font.subheadline
        var titleAwardForegroundColor = Color(ColorPalette.overlayViolet)
        var titleDescriptionForegroundColor = Color.primaryText

        var iconSize = CGSize(width: 28, height: 28)
    }
}

struct HypercoinsAwardView: View {
    private(set) var appearance = Appearance()

    let award: Int
    let description: String

    var body: some View {
        HypercoinLabel(
            title: {
                Text("+\(award)")
                    .font(appearance.titleFont)
                    .foregroundColor(appearance.titleAwardForegroundColor) +
                Text(" \(description)")
                    .font(appearance.titleFont)
                    .foregroundColor(appearance.titleDescriptionForegroundColor)
            },
            appearance: .init(iconSize: appearance.iconSize)
        )
    }
}

extension HypercoinsAwardView {
    static func stageCompleted(
        award: Int,
        appearance: Appearance = .init()
    ) -> HypercoinsAwardView {
        HypercoinsAwardView(
            appearance: appearance,
            award: award,
            description: Strings.StageImplement.StageCompletedModal.awardDescription
        )
    }

    static func projectCompleted(
        award: Int,
        appearance: Appearance = .init()
    ) -> HypercoinsAwardView {
        HypercoinsAwardView(
            appearance: appearance,
            award: award,
            description: Strings.StageImplement.ProjectCompletedModal.awardDescription
        )
    }
}

struct HypercoinsAwardView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
            HypercoinsAwardView
                .stageCompleted(award: 15)
            HypercoinsAwardView
                .projectCompleted(award: 25)
        }
    }
}
