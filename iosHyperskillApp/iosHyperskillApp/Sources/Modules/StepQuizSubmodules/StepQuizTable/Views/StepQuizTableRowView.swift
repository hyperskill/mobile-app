import SwiftUI

extension StepQuizTableRowView {
    struct Appearance {
        let titleFont = UIFont.preferredFont(forTextStyle: .body)
        let titleTextColor = UIColor.primaryText

        let subtitleFont = UIFont.preferredFont(forTextStyle: .subheadline)
        let subtitleTextColor = UIColor.secondaryText

        let interItemSpacing = LayoutInsets.smallInset

        let minHeight: CGFloat = 44
    }
}

struct StepQuizTableRowView: View {
    private(set) var appearance = Appearance()

    var title: String

    var subtitle: String?

    var action: () -> Void

    var body: some View {
        Button(action: action) {
            VStack(alignment: .leading, spacing: appearance.interItemSpacing) {
                Group {
                    LatexView(
                        text: title,
                        configuration: .quizContent(
                            textFont: appearance.titleFont,
                            textColor: appearance.titleTextColor
                        )
                    )

                    if let subtitle, !subtitle.isEmpty {
                        LatexView(
                            text: subtitle,
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

            Button(action: action) {
                Image(systemName: "chevron.right")
                    .font(.title2.bold())
                    .padding(.vertical, LayoutInsets.smallInset / 2)
                    .foregroundColor(Color(ColorPalette.primary))
            }
        }
        .frame(maxWidth: .infinity, minHeight: appearance.minHeight)
    }
}

struct StepQuizTableRowView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ScrollView {
                VStack {
                    StepQuizTableRowView(title: "Variant A", subtitle: "Variant Option", action: {})
                    Spacer()
                }
            }

            StepQuizTableRowView(title: "Variant A", subtitle: nil, action: {})

            StepQuizTableRowView(title: "Variant A", subtitle: "Variant Option", action: {})
                .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
