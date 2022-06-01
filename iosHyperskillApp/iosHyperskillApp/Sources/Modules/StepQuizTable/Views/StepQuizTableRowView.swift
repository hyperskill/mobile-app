import SwiftUI

extension StepQuizTableRowView {
    struct Appearance {
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
                        Text(title)
                            .font(.body)
                            .foregroundColor(.primaryText)

                        if let subtitle = subtitle {
                            Text(subtitle)
                                .font(.subheadline)
                                .foregroundColor(.secondaryText)
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

            StepQuizTableRowView(title: "Variant A", subtitle: "Variant Option", onTap: {})
                .preferredColorScheme(.dark)
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
