import SwiftUI

extension StepQuizTableRowView {
    struct Appearance {
        let titleFont = UIFont.preferredFont(forTextStyle: .body)
        let titleTextColor = UIColor.primaryText

        let subtitleFont = UIFont.preferredFont(forTextStyle: .subheadline)
        let subtitleTextColor = UIColor.secondaryText

        let interItemSpacing = LayoutInsets.smallInset

        let chevronRightIconWidthHeight: CGFloat = 24
    }
}

struct StepQuizTableRowView: View {
    private(set) var appearance = Appearance()

    var title: String

    var subtitle: String?

    var onTap: (() -> Void)

    var body: some View {
        Button(action: onTap) {
            HStack(spacing: appearance.interItemSpacing) {
                VStack(alignment: .leading, spacing: appearance.interItemSpacing) {
                    Group {
                        LatexView(
                            text: .constant(title),
                            configuration: .quizContent(
                                textFont: appearance.titleFont,
                                textColor: appearance.titleTextColor
                            )
                        )

                        if let subtitle = subtitle, !subtitle.isEmpty {
                            LatexView(
                                text: .constant(subtitle),
                                configuration: .quizContent(
                                    textFont: appearance.subtitleFont,
                                    textColor: appearance.subtitleTextColor
                                )
                            )
                        }
                    }
                    .multilineTextAlignment(.leading)
                    .frame(maxWidth: .infinity, alignment: .leading)
                }

                Button(action: onTap) {
                    Image(systemName: "chevron.right")
                        .imageScale(.large)
                        .frame(widthHeight: appearance.chevronRightIconWidthHeight)
                        .foregroundColor(Color(ColorPalette.onSurfaceAlpha38))
                }
            }
        }
        .frame(maxWidth: .infinity)
    }
}

struct StepQuizTableRowView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizTableRowView(title: "Variant A", subtitle: "Variant Option", onTap: {})

            StepQuizTableRowView(title: "Variant A", subtitle: nil, onTap: {})

            StepQuizTableRowView(title: "Variant A", subtitle: "Variant Option", onTap: {})
                .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
