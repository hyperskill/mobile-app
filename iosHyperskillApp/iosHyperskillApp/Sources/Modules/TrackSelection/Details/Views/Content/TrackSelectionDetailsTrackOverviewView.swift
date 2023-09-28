import SwiftUI

extension TrackSelectionDetailsTrackOverviewView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let interitemSpacing = LayoutInsets.smallInset

        let itemFont = Font.body
        let itemForegroundColor = Color.primaryText
        let itemImageSize = CGSize(width: 16, height: 16)

        func makeRatingViewAppearance() -> StarRatingView.Appearance {
            StarRatingView.Appearance(
                spacing: interitemSpacing,
                imageSize: itemImageSize,
                imageColor: Color(ColorPalette.overlayYellow),
                textFont: itemFont,
                textColor: itemForegroundColor
            )
        }
    }
}

struct TrackSelectionDetailsTrackOverviewView: View {
    private(set) var appearance = Appearance()

    let rating: String

    let timeToComplete: String?

    let topicsCount: String
    let projectsCount: String?

    let isCertificateAvailable: Bool

    private var isEmpty: Bool {
        rating.isEmpty
        && (timeToComplete?.isEmpty ?? true)
        && topicsCount.isEmpty
        && (projectsCount?.isEmpty ?? true)
        && !isCertificateAvailable
    }

    private var certificateText: String? {
        if isCertificateAvailable {
            return Strings.TrackSelectionDetails.certificateText
        }
        return nil
    }

    var body: some View {
        if isEmpty {
            EmptyView()
        } else {
            CardView {
                VStack(alignment: .leading, spacing: appearance.spacing) {
                    Text(Strings.TrackSelectionDetails.overviewTitle)
                        .font(.headline)

                    VStack(alignment: .leading, spacing: appearance.spacing) {
                        if !rating.isEmpty {
                            StarRatingView(
                                appearance: appearance.makeRatingViewAppearance(),
                                rating: .string(rating)
                            )
                        }

                        buildItemView(imageResource: .stepTimeToComplete, text: timeToComplete)
                        buildItemView(imageResource: .topic, text: topicsCount)
                        buildItemView(imageResource: .project, text: projectsCount)
                        buildItemView(imageResource: .trophy, text: certificateText)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }
        }
    }

    @ViewBuilder
    private func buildItemView(imageResource: ImageResource, text: String?) -> some View {
        if let text, !text.isEmpty {
            Label(
                title: {
                    Text(text)
                        .font(appearance.itemFont)
                        .foregroundColor(appearance.itemForegroundColor)
                },
                icon: {
                    Image(imageResource)
                        .resizable()
                        .renderingMode(.template)
                        .aspectRatio(contentMode: .fill)
                        .frame(size: appearance.itemImageSize, alignment: .center)
                        .foregroundColor(appearance.itemForegroundColor)
                }
            )
        }
    }
}

#Preview {
    TrackSelectionDetailsTrackOverviewView(
        rating: "It's new track, no rating yet",
        timeToComplete: "249 hours for all learning activities",
        topicsCount: "154 topics with theory and practice adapted to you level",
        projectsCount: "13 projects to choose from for your portfolio",
        isCertificateAvailable: true
    )
    .padding()
    .background(Color.systemGroupedBackground)
}
