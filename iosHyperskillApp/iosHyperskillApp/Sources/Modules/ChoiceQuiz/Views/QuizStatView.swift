import SwiftUI

struct QuizStatView: View {
    var body: some View {
        HStack(alignment: .top) {
            Image("clock_icon")
            Text("2438 users solved this problem. Latest completion was about 13 hours ago.")
                .font(.subheadline)
                .foregroundColor(Color(ColorPalette.onSurfaceAlpha60))
        }
    }
}

struct QuizStatView_Previews: PreviewProvider {
    static var previews: some View {
        QuizStatView()
    }
}
