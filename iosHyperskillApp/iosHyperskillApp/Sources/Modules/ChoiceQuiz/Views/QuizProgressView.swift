import SwiftUI

struct QuizProgressView: View {
    var body: some View {
        ProgressView(value: 0.2)
            .accentColor(Color(ColorPalette.primary))
            .scaleEffect(x: 1, y: 3, anchor: .center)
    }
}

struct QuizProgressView_Previews: PreviewProvider {
    static var previews: some View {
        QuizProgressView()
    }
}
