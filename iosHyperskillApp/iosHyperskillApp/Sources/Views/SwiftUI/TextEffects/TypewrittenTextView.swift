import SwiftUI

struct TypewrittenTextView: View {
    let text: String

    var minDelay: Double = 0.02
    var maxDelay: Double = 0.1

    @State private var animatedText: String = ""
    @State private var currentIndex: Int = 0

    private let feedbackGenerator = FeedbackGenerator(feedbackType: .impact(.light))

    var body: some View {
        Text(animatedText)
            .onAppear {
                feedbackGenerator.prepare()
                animateNextCharacter()
            }
    }

    private func animateNextCharacter() {
        guard currentIndex < text.count else {
            return
        }

        let randomDelay = Double.random(in: minDelay...maxDelay)

        Timer.scheduledTimer(withTimeInterval: randomDelay, repeats: false) { _ in
            let index = text.index(text.startIndex, offsetBy: currentIndex)
            animatedText.append(text[index])

            DispatchQueue.main.async {
                feedbackGenerator.triggerFeedback()
            }

            currentIndex += 1
            animateNextCharacter()
        }
    }
}

#if DEBUG
#Preview {
    TypewrittenTextView(
        text: "How did you hear about Hyperskill?"
    )
}
#endif
