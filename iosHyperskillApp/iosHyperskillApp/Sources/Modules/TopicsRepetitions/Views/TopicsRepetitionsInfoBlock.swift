import SwiftUI

extension TopicsRepetitionsInfoBlock {
    struct Appearance {
        let bookImageWidthHeight: CGFloat = 80
    }
}

struct TopicsRepetitionsInfoBlock: View {
    private(set) var appearance = Appearance()

    var body: some View {
        VStack(alignment: .leading, spacing: LayoutInsets.largeInset) {
            Image(Images.TopicsRepetitions.bookImage)
                .resizable()
                .frame(widthHeight: appearance.bookImageWidthHeight)

            Text(Strings.TopicsRepetitions.InfoBlock.title)
                .font(.title3)
                .foregroundColor(.primaryText)
                .bold()

            Text(Strings.TopicsRepetitions.InfoBlock.description)
                .font(.subheadline)
                .foregroundColor(.secondaryText)
        }
        .padding()
        .background(Color(ColorPalette.surface))
    }
}

struct TopicsRepetitionsInfoBlock_Previews: PreviewProvider {
    static var previews: some View {
        TopicsRepetitionsInfoBlock()
    }
}
