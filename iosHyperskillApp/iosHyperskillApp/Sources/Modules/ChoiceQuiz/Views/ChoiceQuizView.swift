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
                        isMultipleChoice: isMultipleChoice
                    )
                }
            }
        }
    }
}

struct ChoiceView_Previews: PreviewProvider {
    @State var choiceQuizSingle = ChoiceQuizViewData(type: .single, status: nil)
    static var previews: some View {
        Group {
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
                    .init(text: "choice1", isSelected: true),
                    .init(text: "choice2", isSelected: false)
                ]),
                isMultipleChoice: false
            )
        }
    }
}
