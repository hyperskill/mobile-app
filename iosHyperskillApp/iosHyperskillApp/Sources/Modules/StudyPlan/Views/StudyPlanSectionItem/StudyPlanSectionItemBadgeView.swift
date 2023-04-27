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
        case violet
        case green

        var foregroundColor: Color {
            switch self {
            case .green:
                return Color(ColorPalette.secondary)
            case .violet:
                return Color(ColorPalette.overlayViolet)
            }
        }

        var backgroundColor: Color {
            switch self {
            case .green:
                return Color(ColorPalette.overlayGreenAlpha12)
            case .violet:
                return Color(ColorPalette.overlayVioletAlpha12)
            }
        }
    }
}

struct StudyPlanSectionItemBadgeView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StudyPlanSectionItemBadgeView(
                text: "50%",
                style: .green
            )

            StudyPlanSectionItemBadgeView(
                text: "IDE Required",
                style: .violet
            )
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
