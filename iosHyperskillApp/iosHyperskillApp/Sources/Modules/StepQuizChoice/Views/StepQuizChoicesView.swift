import SwiftUI

extension StepQuizChoicesView {
    struct Appearance {
        let interItemSpacing: CGFloat = 20
    }
}

struct StepQuizChoicesView: View {
    private(set) var appearance = Appearance()

    var quizTitle: String

    @Binding var choices: [StepQuizChoiceViewData.Choice]

    var isMultipleChoice: Bool

    var body: some View {
        VStack(alignment: .leading, spacing: appearance.interItemSpacing) {
            VStack(alignment: .leading, spacing: LayoutInsets.defaultInset) {
                Text(quizTitle)
                    .font(.caption)
                    .foregroundColor(.disabledText)
                Divider()
            }

            VStack(alignment: .leading, spacing: appearance.interItemSpacing) {
                ForEach($choices, id: \.text) { choice in
                    StepQuizChoiceElementView(
                        isSelected: choice.isSelected,
                        text: choice.text.wrappedValue,
                        isMultipleChoice: isMultipleChoice,
                        selectChoice: self.selectChoice
                    )
                }
            }
        }
    }

    private func selectChoice(selectedChoice: Binding<Bool>) {
        if !isMultipleChoice {
            for i in choices.indices {
                choices[i].isSelected = false
            }
            selectedChoice.wrappedValue = true
        } else {
            selectedChoice.wrappedValue.toggle()
        }
    }
}

struct StepQuizChoicesView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizChoicesView(
                quizTitle: "Task text",
                choices: .constant([
                    .init(text: "choice1", isSelected: true),
                    .init(text: "choice2", isSelected: false)
                ]),
                isMultipleChoice: true
            )

            StepQuizChoicesView(
                quizTitle: "Task text",
                choices: .constant([
                    .init(text: "choice3", isSelected: true),
                    .init(text: "choice4", isSelected: false)
                ]),
                isMultipleChoice: false
            )
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
