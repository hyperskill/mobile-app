import SwiftUI

extension SolveUnlimitedBadgeView {
    struct Appearance {
        let solveUnlimitedHorizontalPadding: CGFloat = 8
        let solveUnlimitedVerticalPadding: CGFloat = 4
        let solveUnlimitedCornerRadius: CGFloat = 4
    }
}

struct SolveUnlimitedBadgeView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        Text(Strings.Home.solveUnlimited)
            .font(.caption)
            .foregroundColor(Color(ColorPalette.overlayViolet))
            .padding(.horizontal, appearance.solveUnlimitedHorizontalPadding)
            .padding(.vertical, appearance.solveUnlimitedVerticalPadding)
            .background(Color(ColorPalette.overlayVioletAlpha12))
            .cornerRadius(appearance.solveUnlimitedCornerRadius)
    }
}

struct SolveUnlimitedBadgeView_Previews: PreviewProvider {
    static var previews: some View {
        SolveUnlimitedBadgeView()
    }
}
