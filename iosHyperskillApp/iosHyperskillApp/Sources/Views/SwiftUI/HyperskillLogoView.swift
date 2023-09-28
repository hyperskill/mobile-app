import SwiftUI

struct HyperskillLogoView: View {
    var logoWidthHeight: CGFloat = 48

    var body: some View {
        Image(.hyperskillLogo)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(widthHeight: logoWidthHeight)
    }
}

#Preview {
    HyperskillLogoView()
}
