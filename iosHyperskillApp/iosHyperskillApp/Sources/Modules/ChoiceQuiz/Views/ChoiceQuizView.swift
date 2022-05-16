import SwiftUI

struct ChoiceQuizView: View {
    var task: String
    @Binding var choices: [ChoiceQuizViewData.Choice]
    var isMultipleChoice: Bool

    var body: some View {
        VStack(alignment: .leading) {
            VStack(alignment: .leading, spacing: 16) {
                Text(task)
                .font(.caption)
                .foregroundColor(Color(ColorPalette.onSurfaceAlpha38))
                Divider()
            }.padding(.bottom, 20)

            VStack(alignment: .leading, spacing: 20) {
                ForEach($choices, id: \.text) { choice in
                    ChoiceQuizElementView(
                        isSelected: choice.isSelected,
                        text: choice.text.wrappedValue,
                        isMultipleChoice: isMultipleChoice,
                        selectChoice: self.selectChoice
                    )
                }
            }
        }
    }

    func selectChoice(selectedChoice: Binding<Bool>) {
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

struct ChoiceView_Previews: PreviewProvider {
    static var previews: some View {
            ChoiceQuizView(
                task: "Task text",
                choices: .constant([
                    .init(text: "choice1", isSelected: true),
                    .init(text: "choice2", isSelected: false)
                ]),
                isMultipleChoice: true
            )

            ChoiceQuizView(
                task: "Task text",
                choices: .constant([
                    .init(text: "choice3", isSelected: true),
                    .init(text: "choice4", isSelected: false)
                ]),
                isMultipleChoice: false
            )
    }
}
