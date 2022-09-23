import SwiftUI

extension HSTabBar {
    struct Appearance {
        var interItemSpacing: CGFloat = 0

        var font = Font.body
        var selectedFont = Font.body.weight(.medium)

        var textColor = Color.secondaryText
        var selectedTextColor = Color(ColorPalette.primary)

        var textInsets = LayoutInsets(vertical: LayoutInsets.smallInset).edgeInsets

        var indicatorHeight: CGFloat = 2
        var indicatorColor = Color(ColorPalette.primary)
    }
}

struct HSTabBar: View {
    private(set) var appearance = Appearance()

    let titles: [String]

    @Binding var selectedTabIndex: Int

    var body: some View {
        HStack(spacing: appearance.interItemSpacing) {
            ForEach(Array(titles.enumerated()), id: \.offset) { index, title in
                let isSelected = index == selectedTabIndex

                VStack(spacing: 0) {
                    Button(
                        title,
                        action: {
                            withAnimation {
                                selectedTabIndex = index
                            }
                        }
                    )
                    .frame(maxWidth: .infinity)
                    .font(isSelected ? appearance.selectedFont : appearance.font)
                    .foregroundColor(isSelected ? appearance.selectedTextColor : appearance.textColor)
                    .padding(appearance.textInsets)

                    if isSelected {
                        Divider()
                            .frame(height: appearance.indicatorHeight)
                            .overlay(appearance.indicatorColor)
                    }
                }
            }
        }
    }
}

struct HSTabBar_Previews: PreviewProvider {
    static var previews: some View {
        HSTabBar(
            titles: ["Details", "Code"],
            selectedTabIndex: .constant(1)
        )
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
