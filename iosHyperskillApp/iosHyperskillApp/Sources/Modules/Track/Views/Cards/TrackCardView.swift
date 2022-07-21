import SwiftUI

extension TrackCardView {
    struct Appearance {
        let imageWidthHeight: CGFloat = 16
    }
}

struct TrackCardView: View {
    private(set) var appearance = Appearance()

    let title: String

    let imageName: String
    var imageRenderingMode = Image.TemplateRenderingMode.template

    let progress: Float?

    let subtitle: String

    var body: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            HStack(spacing: LayoutInsets.smallInset) {
                Text(title)
                    .font(.callout)
                    .foregroundColor(.primaryText)

                Spacer()

                Image(imageName)
                    .resizable()
                    .renderingMode(imageRenderingMode)
                    .aspectRatio(contentMode: .fit)
                    .frame(widthHeight: appearance.imageWidthHeight)
                    .foregroundColor(.primaryText)
            }

            if let progress = progress {
                ProgressView(value: progress)
                    .progressViewStyle(LinearProgressViewStyle(tint: Color(ColorPalette.blue400)))
            }

            Text(subtitle)
                .font(.callout)
                .foregroundColor(.secondaryText)
                .frame(maxWidth: .infinity, alignment: .leading)
        }
        .padding()
        .background(Color(ColorPalette.surface))
        .addBorder()
    }
}

struct TrackCardView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            TrackCardView(
                title: "~ 56 h",
                imageName: Images.Step.clock,
                progress: nil,
                subtitle: "Time to complete the track"
            )

            TrackCardView(
                title: "48 / 149",
                imageName: Images.Track.About.topic,
                progress: 0.322_147_651,
                subtitle: "Completed topics"
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
