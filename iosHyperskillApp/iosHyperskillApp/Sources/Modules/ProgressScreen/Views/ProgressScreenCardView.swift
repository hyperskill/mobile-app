import SwiftUI

extension ProgressScreenCardView {
    struct Appearance {
        let spacing: CGFloat
        let interitemSpacing: CGFloat

        let imageWidthHeight: CGFloat = 16

        let backgroundColor: Color
        let cornerRadius: CGFloat
    }
}

struct ProgressScreenCardView: View {
    let appearance: Appearance

    let title: String
    let titleSecondaryText: String?

    let imageName: String
    var imageRenderingMode = Image.TemplateRenderingMode.template

    let progress: Progress?

    let subtitle: String

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            HStack(spacing: appearance.interitemSpacing) {
                Text("\(title) ")
                    .font(.callout)
                    .foregroundColor(.primaryText) +
                Text(titleSecondaryText)
                    .font(.callout)
                    .foregroundColor(.tertiaryText)

                Spacer()

                Image(imageName)
                    .resizable()
                    .renderingMode(imageRenderingMode)
                    .aspectRatio(contentMode: .fit)
                    .frame(widthHeight: appearance.imageWidthHeight)
                    .foregroundColor(.primaryText)
            }

            if let progress {
                ProgressView(value: progress.value)
                    .progressViewStyle(LinearProgressViewStyle(tint: progress.tintColor))
            }

            Text(subtitle)
                .font(.callout)
                .foregroundColor(.secondaryText)
                .fixedSize(horizontal: false, vertical: true)
        }
        .padding()
        .background(appearance.backgroundColor)
        .cornerRadius(appearance.cornerRadius)
    }

    struct Progress {
        let value: Float
        let isCompleted: Bool

        fileprivate var tintColor: Color {
            if isCompleted {
                return Color(ColorPalette.secondary)
            }
            return Color(ColorPalette.blue400)
        }
    }
}

struct ProgressScreenTrackCardView_Previews: PreviewProvider {
    static var previews: some View {
        let appearance = ProgressScreenCardView.Appearance(
            spacing: LayoutInsets.defaultInset,
            interitemSpacing: LayoutInsets.smallInset,
            backgroundColor: Color(ColorPalette.surface),
            cornerRadius: 8
        )

        VStack {
            ProgressScreenCardView(
                appearance: appearance,
                title: "48 / 149",
                titleSecondaryText: "• 32%",
                imageName: Images.Common.topic,
                progress: .init(value: 0.322147651, isCompleted: false),
                subtitle: "Completed topics"
            )

            ProgressScreenCardView(
                appearance: appearance,
                title: "131 / 149",
                titleSecondaryText: "• 91%",
                imageName: Images.Common.topic,
                progress: .init(value: 0.91, isCompleted: true),
                subtitle: "Completed topics"
            )

            ProgressScreenCardView(
                appearance: appearance,
                title: "~ 56 h",
                titleSecondaryText: nil,
                imageName: Images.Step.clock,
                progress: nil,
                subtitle: "Time to complete the track"
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
        .background(Color.systemGroupedBackground)
    }
}
