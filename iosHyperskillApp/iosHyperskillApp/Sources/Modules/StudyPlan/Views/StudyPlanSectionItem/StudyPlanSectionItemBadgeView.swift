import SwiftUI

extension StudyPlanSectionItemBadgeView {
    struct Appearance {
        let cornerRadius: CGFloat = 4
        let insets = LayoutInsets(horizontal: 8, vertical: 4)
    }
}

struct StudyPlanSectionItemBadgeView: View {
    private(set) var appearance = Appearance()

    let text: String

    let style: Style

    var body: some View {
        Text(text)
            .font(.caption)
            .foregroundColor(style.foregroundColor)
            .padding(appearance.insets.edgeInsets)
            .background(style.backgroundColor)
            .cornerRadius(appearance.cornerRadius)
    }

    enum Style {
        case blue
        case green
        case violet

        var foregroundColor: Color {
            switch self {
            case .blue:
                return Color(ColorPalette.primary)
            case .green:
                return Color(ColorPalette.secondary)
            case .violet:
                return Color(ColorPalette.overlayViolet)
            }
        }

        var backgroundColor: Color {
            switch self {
            case .blue:
                return Color(ColorPalette.overlayBlueAlpha12)
            case .green:
                return Color(ColorPalette.overlayGreenAlpha12)
            case .violet:
                return Color(ColorPalette.overlayVioletAlpha12)
            }
        }
    }
}

extension StudyPlanSectionItemBadgeView {
    static func ideRequired() -> StudyPlanSectionItemBadgeView {
        StudyPlanSectionItemBadgeView(
            text: Strings.StageImplement.UnsupportedModal.title,
            style: .violet
        )
    }

    static func current() -> StudyPlanSectionItemBadgeView {
        StudyPlanSectionItemBadgeView(text: Strings.StudyPlan.badgeCurrent, style: .blue)
    }
}

struct StudyPlanSectionItemBadgeView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StudyPlanSectionItemBadgeView(
                text: "50%",
                style: .green
            )

            StudyPlanSectionItemBadgeView.ideRequired()

            StudyPlanSectionItemBadgeView.current()
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
