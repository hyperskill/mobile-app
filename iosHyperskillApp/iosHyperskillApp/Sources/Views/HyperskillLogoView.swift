import SwiftUI

struct HyperskillLogoView: View {
    var logoWidthHeight: CGFloat = 48

    var body: some View {
        Image(Images.Common.hyperskillLogo)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(widthHeight: logoWidthHeight)
    }
}

struct HyperskillLogoView_Previews: PreviewProvider {
    static var previews: some View {
        HyperskillLogoView()
            .previewLayout(.sizeThatFits)
    }
}
