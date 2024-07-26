import SwiftUI

@available(iOS 16.1, *)
struct HackerTextView: View {
    var text: String
    var transition: ContentTransition = .interpolate
    var duration: CGFloat = 1.0
    var speed: CGFloat = 0.1

    @State private var animatedText = ""
    @State private var randomCharacters: [Character] = {
        let string = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-?/#$%@!^&*()="
        return Array(string)
    }()
    @State private var animationID = UUID().uuidString

    var body: some View {
        Text(animatedText)
            .fontDesign(.monospaced)
            .truncationMode(.tail)
            .contentTransition(transition)
            .animation(.easeInOut(duration: 0.1), value: animatedText)
            .frame(maxWidth: .infinity)
            .onAppear {
                guard animatedText.isEmpty else {
                    return
                }

                setRandomCharacters()
                animateText()
                applyFinalTextCharacters()
            }
            .onChange(of: text) { _ in
                animatedText = text
                animationID = UUID().uuidString

                setRandomCharacters()
                animateText()
                applyFinalTextCharacters()
            }
    }

    private func animateText() {
        let currentID = animationID

        for index in text.indices {
            let delay = CGFloat.random(in: 0...duration)
            var timerDuration: CGFloat = 0

            let timer = Timer.scheduledTimer(withTimeInterval: speed, repeats: true) { timer in
                if currentID != animationID {
                    timer.invalidate()
                } else {
                    timerDuration += speed

                    if timerDuration >= delay {
                        if text.indices.contains(index) {
                            let actualCharacter = text[index]
                            replaceCharacter(at: index, character: actualCharacter)
                        }

                        timer.invalidate()
                    } else {
                        guard let randomCharacter = randomCharacters.randomElement() else {
                            return
                        }

                        replaceCharacter(at: index, character: randomCharacter)
                    }
                }
            }
            timer.fire()
        }
    }

    private func setRandomCharacters() {
        animatedText = text

        for index in animatedText.indices {
            guard let randomCharacter = randomCharacters.randomElement() else {
                return
            }
            replaceCharacter(at: index, character: randomCharacter)
        }
    }

    private func replaceCharacter(at index: String.Index, character: Character) {
        guard animatedText.indices.contains(index) else {
            return
        }

        let indexCharacter = String(animatedText[index])

        // Ensure that a random character won't overwrite newlines and empty spaces
        // swiftlint:disable:next empty_string
        if indexCharacter.trimmingCharacters(in: .whitespacesAndNewlines) != "" {
            animatedText.replaceSubrange(index...index, with: String(character))
        }
    }

    private func applyFinalTextCharacters() {
        let currentID = animationID

        DispatchQueue.main.asyncAfter(deadline: .now() + duration) {
            guard currentID == animationID else {
                return
            }

            if animatedText != text {
                animatedText = text
            }
        }
    }
}

#if DEBUG
@available(iOS 16.1, *)
private struct HackerContentView: View {
    @State private var trigger = false

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            HackerTextView(
                text: "Education is a journey, not a destination—enjoy each step towards becoming wiser."
                // trigger: trigger
            )
            .font(.headline)

            Button(
                action: {
                    trigger.toggle()
                },
                label: {
                    Text("Trigger")
                        .fontWeight(.semibold)
                        .padding()
                }
            )
            .buttonStyle(.borderedProminent)
            .buttonBorderShape(.capsule)
            .frame(maxWidth: .infinity)
        }
        .frame(maxWidth: .infinity)
        .padding()
    }
}

@available(iOS 16.1, *)
#Preview {
    HackerContentView()
}
#endif
