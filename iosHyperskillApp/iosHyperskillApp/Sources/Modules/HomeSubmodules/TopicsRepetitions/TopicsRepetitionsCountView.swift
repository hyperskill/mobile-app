import SwiftUI

extension TopicsRepetitionsCountView {
    struct Appearance {
        let topicsCountHorizontalPadding: CGFloat = 12
        let countMinWidthHeight: CGFloat = 32
    }
}

struct TopicsRepetitionsCountView: View {
    private(set) var appearance = Appearance()

    private let formatter = Formatter.default

    let topicsToRepeatCount: Int

    var body: some View {
        HStack(spacing: LayoutInsets.smallInset) {
            Text(String(topicsToRepeatCount))
                .padding(LayoutInsets.smallInset)
                .frame(minWidth: appearance.countMinWidthHeight, minHeight: appearance.countMinWidthHeight)
                .font(.subheadline)
                .foregroundColor(.primaryText)
                .background(Color(ColorPalette.overlayBlueAlpha12))
                .cornerRadius(LayoutInsets.smallInset)

            Text(formatter.topicsToRepeatTodayCount(topicsToRepeatCount))
                .font(.subheadline)
                .foregroundColor(.secondaryText)
        }
    }
}

struct TopicsRepetitionsCountView_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsCountView(topicsToRepeatCount: 4)
    }
}
