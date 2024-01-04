import SwiftUI

extension InterviewPreparationWidgetContentView {
    struct Appearance {
        var backgroundColor = Color(ColorPalette.surface)

        let spacing = LayoutInsets.defaultInset

        let arrowIconSize: CGFloat = 32
    }
}

struct InterviewPreparationWidgetContentView: View {
    private(set) var appearance = Appearance()

    let countText: String
    let description: String

    let onTap: () -> Void

    var body: some View {
        Button(
            action: onTap,
            label: {
                VStack(alignment: .leading, spacing: appearance.spacing) {
                    HStack(spacing: LayoutInsets.smallInset) {
                        Text(Strings.InterviewPreparationWidget.title)
                            .font(.headline)
                            .foregroundColor(.primaryText)

                        Spacer()

                        Image(.problemOfDayArrowUncompleted)
                            .renderingMode(.original)
                            .resizable()
                            .frame(widthHeight: appearance.arrowIconSize)
                    }

                    HomeWidgetCountView(countText: countText, description: description)
                }
                .padding()
                .background(appearance.backgroundColor)
                .addBorder()
            }
        )
        .buttonStyle(BounceButtonStyle())
    }
}

#Preview {
    InterviewPreparationWidgetContentView(
        countText: "25",
        description: "problems at the hard level to solve",
        onTap: {}
    )
    .padding()
    .background(Color.systemGroupedBackground)
}
