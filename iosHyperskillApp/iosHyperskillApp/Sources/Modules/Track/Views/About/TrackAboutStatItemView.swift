import SwiftUI

extension TrackAboutStatItemView {
    struct Appearance {
        let spacing: CGFloat = 4

        let imageWidthHeight: CGFloat = 14
    }
}

struct TrackAboutStatItemView: View {
    private(set) var appearance = Appearance()

    let itemType: ItemType

    let text: String

    var body: some View {
        HStack(spacing: appearance.spacing) {
            Image(itemType.imageName)
                .resizable()
                .renderingMode(.template)
                .aspectRatio(contentMode: .fit)
                .frame(widthHeight: appearance.imageWidthHeight)
                .foregroundColor(itemType.imageTintColor)

            Text(text)
                .font(.caption)
                .foregroundColor(.secondaryText)
        }
    }

    enum ItemType {
        case rating
        case timeToComplete
        case project
        case topic

        fileprivate var imageName: String {
            switch self {
            case .rating:
                return Images.Track.About.rating
            case .timeToComplete:
                return Images.Step.clock
            case .project:
                return Images.Track.About.project
            case .topic:
                return Images.Track.About.topic
            }
        }

        fileprivate var imageTintColor: Color {
            switch self {
            case .rating:
                return Color(ColorPalette.yellow300)
            default:
                return .secondaryText
            }
        }
    }
}

struct TrackAboutStatItemView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            TrackAboutStatItemView(itemType: .rating, text: "4.7")

            TrackAboutStatItemView(itemType: .timeToComplete, text: "104 hours")

            TrackAboutStatItemView(itemType: .project, text: "20 projects")

            TrackAboutStatItemView(itemType: .topic, text: "220 topics")
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
