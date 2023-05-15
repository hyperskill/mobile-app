import SwiftUI

extension TrackSelectionListGridCellView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
        let headerGroupSpacing = LayoutInsets.smallInset
        let textGroupSpacing: CGFloat = 4

        let avatarSize = CGSize(width: 36, height: 36)

        let ratingViewAppearance = StarRatingView.Appearance(
            imageSize: CGSize(width: 16, height: 16),
            imageColor: Color(ColorPalette.overlayYellow),
            textFont: .caption,
            textColor: .secondaryText
        )

        fileprivate func buttonStyle(isSelected: Bool) -> OutlineButtonStyle {
            OutlineButtonStyle(
                borderColor: isSelected ? Color(ColorPalette.overlayBlue) : .border,
                alignment: .leading,
                paddingEdgeSet: [],
                backgroundColor: Color(ColorPalette.surface)
            )
        }
    }
}

struct TrackSelectionListGridCellView: View {
    private(set) var appearance = Appearance()

    let track: TrackSelectionListItem
    let isSelected: Bool

    let onTap: (Int64) -> Void

    var body: some View {
        Button(
            action: {
                onTap(track.id)
            },
            label: buildContent
        )
        .buttonStyle(appearance.buttonStyle(isSelected: isSelected))
    }

    @ViewBuilder
    private func buildContent() -> some View {
        VStack(alignment: .leading, spacing: appearance.spacing) {
            HStack(alignment: .top, spacing: appearance.headerGroupSpacing) {
                if let imageSource = track.imageSource {
                    LazyAvatarView(imageSource)
                        .frame(size: appearance.avatarSize)
                }

                VStack(alignment: .leading, spacing: appearance.textGroupSpacing) {
                    Text(track.title)
                        .font(.body)
                        .foregroundColor(.primaryText)

                    if let timeToComplete = track.timeToComplete {
                        Text(timeToComplete)
                            .font(.caption)
                            .foregroundColor(.secondaryText)
                    }
                }

                Spacer()

                StarRatingView(
                    appearance: appearance.ratingViewAppearance,
                    rating: .string(track.rating)
                )
            }

            TrackSelectionListGridCellBadgesView(
                isSelected: isSelected,
                isIdeRequired: track.isIdeRequired,
                isBeta: track.isBeta,
                isCompleted: track.isCompleted
            )
        }
        .padding()
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
    }
}

struct TrackSelectionListGridCellView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            TrackSelectionListGridCellView(
                track: .placeholder,
                isSelected: false,
                onTap: { _ in }
            )

            TrackSelectionListGridCellView(
                track: .placeholder,
                isSelected: true,
                onTap: { _ in }
            )
        }
        .padding()
        .background(Color.systemGroupedBackground)

        VStack(spacing: LayoutInsets.defaultInset) {
            TrackSelectionListGridCellView(
                track: .placeholder,
                isSelected: false,
                onTap: { _ in }
            )

            TrackSelectionListGridCellView(
                track: .placeholder,
                isSelected: true,
                onTap: { _ in }
            )
        }
        .padding()
        .background(Color.systemGroupedBackground)
        .preferredColorScheme(.dark)
    }
}
