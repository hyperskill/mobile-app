import SwiftUI

extension StudyPlanSectionItemProgressBadgeView {
    struct Appearance {
        let foregroundColor = Color(ColorPalette.secondary)
        let backgroundColor = Color(ColorPalette.overlayGreenAlpha12)
        let cornerRadius: CGFloat = 4
        let insets = LayoutInsets(horizontal: 8, vertical: 4)
    }
}

struct StudyPlanSectionItemProgressBadgeView: View {
    private(set) var appearance = Appearance()

    let formattedProgress: String

    var body: some View {
        Text(formattedProgress)
            .font(.caption)
            .foregroundColor(appearance.foregroundColor)
            .padding(appearance.insets.edgeInsets)
            .background(appearance.backgroundColor)
            .cornerRadius(appearance.cornerRadius)
    }
}

struct StudyPlanSectionItemProgressBadgeView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSectionItemProgressBadgeView(
            formattedProgress: "50%"
        )
    }
}
