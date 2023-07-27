import SwiftUI

extension LinearGradientProgressView {
    struct Appearance {
        let maxHeight: CGFloat = 8
        let cornerRadius: CGFloat = 8
    }
}

struct LinearGradientProgressView: View {
    private(set) var appearance = Appearance()

    let progress: Float

    var body: some View {
        Rectangle()
            .foregroundColor(Color(ColorPalette.onSurfaceAlpha9))
            .frame(
                maxWidth: .infinity,
                maxHeight: appearance.maxHeight
            )
            .background(
                GeometryReader { geometry in
                    LinearGradient(
                        stops: [
                            Gradient.Stop(color: Color(red: 0.65, green: 0.74, blue: 0.96), location: 0.40),
                            Gradient.Stop(color: Color(red: 0.36, green: 0.45, blue: 0.91), location: 0.90)
                        ],
                        startPoint: UnitPoint(x: 1, y: 1),
                        endPoint: UnitPoint(x: 0, y: 0)
                    )
                    .frame(width: geometry.size.width * CGFloat(progress))
                    .cornerRadius(appearance.cornerRadius)
                },
                alignment: .leading
            )
            .cornerRadius(appearance.cornerRadius)
    }
}

struct LinearGradientProgressView_Previews: PreviewProvider {
    static var previews: some View {
        LinearGradientProgressView(
            progress: 0.6
        )
    }
}
