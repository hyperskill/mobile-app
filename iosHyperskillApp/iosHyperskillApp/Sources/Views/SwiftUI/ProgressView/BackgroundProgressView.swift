import SwiftUI

extension BackgroundProgressView {
    struct Appearance {
        var backgroundColor = Color(ColorPalette.overlayGreenAlpha7)
        var cornerRadius: CGFloat = 8
    }
}

struct BackgroundProgressView: View {
    private(set) var appearance = Appearance()

    let progress: CGFloat

    var body: some View {
        GeometryReader { geometry in
            Rectangle()
                .stroke(lineWidth: 0)
                .cornerRadius(appearance.cornerRadius)
                .background(appearance.backgroundColor)
                .frame(width: geometry.size.width * progress / 100)
        }
    }
}

extension View {
    func backgroundProgress(
        progress: Int,
        appearance: BackgroundProgressView.Appearance = .init()
    ) -> some View {
        background(
            BackgroundProgressView(
                appearance: appearance,
                progress: CGFloat(progress)
            )
        )
    }

    func backgroundProgress(
        progress: Int32,
        appearance: BackgroundProgressView.Appearance = .init()
    ) -> some View {
        background(
            BackgroundProgressView(
                appearance: appearance,
                progress: CGFloat(progress)
            )
        )
    }
}

struct BackgroundProgressView_Previews: PreviewProvider {
    static var previews: some View {
        Button(
            action: {},
            label: {
                HStack {
                    Text("Tap Me")
                }
                .frame(maxWidth: .infinity)
                .padding()
                .backgroundProgress(progress: 50)
            }
        )
        .buttonStyle(OutlineButtonStyle(paddingEdgeSet: []))
        .padding()
    }
}
