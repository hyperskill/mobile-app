import SwiftUI

extension BrandLinearGradient {
    struct Appearance {
        let colorFirst = Color(red: 0.296, green: 1, blue: 0.873, opacity: 1)
        let colorSecond = Color(red: 0.3, green: 0.623, blue: 1, opacity: 1)

        let startPoint = UnitPoint(x: 0.25, y: -0.5)
        let endPoint = UnitPoint(x: 0.5, y: 1)
    }
}

struct BrandLinearGradient: View {
    private(set) var appearance = Appearance()

    var body: some View {
        LinearGradient(
            gradient: Gradient(
                colors: [
                    appearance.colorFirst,
                    appearance.colorSecond
                ]
            ),
            startPoint: appearance.startPoint,
            endPoint: appearance.endPoint
        )
    }
}

struct BrandLinearGradient_Previews: PreviewProvider {
    static var previews: some View {
        BrandLinearGradient()
            .mask(Rectangle().cornerRadius(8))
            .frame(widthHeight: 64)
    }
}
