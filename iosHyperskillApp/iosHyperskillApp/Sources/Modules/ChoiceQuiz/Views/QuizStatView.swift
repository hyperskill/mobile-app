import SwiftUI

struct QuizStatView: View {
    var text: String
    
    var body: some View {
        HStack(alignment: .top) {
            Image("clock_icon")
                .frame(width: 12, height: 21)
                .foregroundColor(.secondaryText)
            Text(text)
                .font(.subheadline)
                .foregroundColor(.secondaryText)
        }
    }
}

struct QuizStatView_Previews: PreviewProvider {
    static var previews: some View {
        QuizStatView(text: "2438 users solved this problem. Latest completion was about 13 hours ago.")
    }
}
