import SwiftUI

extension ProgressScreenTrackCardView {
    struct Appearance {
        let imageWidthHeight: CGFloat = 16

        let backgroundColor = Color(ColorPalette.surface)

        let cornerRadius: CGFloat = 8
    }
}

struct ProgressScreenTrackCardView: View {
    private(set) var appearance = Appearance()

    let title: String
    let titleSecondaryText: String?

    let imageName: String
    var imageRenderingMode = Image.TemplateRenderingMode.template

    let progress: Progress?

    let subtitle: String

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            HStack(spacing: LayoutInsets.smallInset) {
                Text(title)
                    .font(.callout)
                    .foregroundColor(.primaryText)

                if let titleSecondaryText {
                    Text(titleSecondaryText)
                        .font(.callout)
                        .foregroundColor(.disabledText)
                }

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
                .frame(maxWidth: .infinity, alignment: .leading)
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
        VStack {
            ProgressScreenTrackCardView(
                title: "48 / 149",
                titleSecondaryText: "• 32%",
                imageName: Images.Track.About.topic,
                progress: .init(value: 0.322147651, isCompleted: false),
                subtitle: "Completed topics"
            )

            ProgressScreenTrackCardView(
                title: "131 / 149",
                titleSecondaryText: "• 91%",
                imageName: Images.Track.About.topic,
                progress: .init(value: 0.91, isCompleted: true),
                subtitle: "Completed topics"
            )

            ProgressScreenTrackCardView(
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
