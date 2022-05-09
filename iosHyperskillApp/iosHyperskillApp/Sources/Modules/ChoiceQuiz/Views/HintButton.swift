import SwiftUI

struct HintButton: View {
    var body: some View {
        Button(action: {}, label: {
            HStack(spacing: 8) {
                Image("choice-quiz-lightning-icon")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 16, height: 16)
                Text(Strings.choiceQuizHintButtonText)
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 8)
        })
        .buttonStyle(OutlineButtonStyle(font: .subheadline, maxWidth: nil))
    }
}

struct HintButton_Previews: PreviewProvider {
    static var previews: some View {
        HStack {
            HintButton()
            Spacer()
        }
    }
}
