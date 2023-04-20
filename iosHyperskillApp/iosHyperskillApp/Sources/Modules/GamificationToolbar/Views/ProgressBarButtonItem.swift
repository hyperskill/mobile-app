import SwiftUI

extension ProgressBarButtonItem {
    struct Appearance {
        let circleWidthHeight: CGFloat = 16
        let circleLineWidth: CGFloat = 2
        let circleBackgroundWidthHeight: CGFloat = 28
    }
}

struct ProgressBarButtonItem: View {
    private(set) var appearance = Appearance()

    let progress: Float

    let isCompleted: Bool

    let onTap: () -> Void

    var body: some View {
        Button(
            action: onTap,
            label: {
                HStack(spacing: LayoutInsets.defaultInset) {
                    ZStack {
                        Circle()
                            .stroke(
                                isCompleted
                                ? Color(ColorPalette.secondaryAlpha38)
                                : Color(ColorPalette.primaryAlpha38),
                                lineWidth: appearance.circleLineWidth
                            )

                        Circle()
                            .trim(from: 0, to: CGFloat(progress))
                            .stroke(
                                isCompleted
                                ? Color(ColorPalette.secondary)
                                : Color(ColorPalette.primary),
                                lineWidth: appearance.circleLineWidth
                            )
                            .rotationEffect(.degrees(-90))
                    }
                    .frame(widthHeight: appearance.circleWidthHeight)
                    .background(
                        Circle()
                            .fill(Color(ColorPalette.surface))
                            .frame(widthHeight: appearance.circleBackgroundWidthHeight)
                    )

                    Text(Strings.Track.Progress.title)
                        .foregroundColor(.primaryText)
                        .font(.subheadline)
                }
            }
        )
    }
}

struct ProgressBarButtonItem_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ProgressBarButtonItem(
                progress: 0.65,
                isCompleted: false,
                onTap: {}
            )

            ProgressBarButtonItem(
                progress: 0.95,
                isCompleted: true,
                onTap: {}
            )
        }
        .padding()
        .background(Color(ColorPalette.background))
        .previewLayout(.sizeThatFits)
    }
}
