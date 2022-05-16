import SwiftUI

struct ChoiceQuizElementView: View {
    var isSelected: Binding<Bool>
    let text: String
    var isMultipleChoice: Bool
    var body: some View {
        Button(
            action: {
            },
            label: {
                HStack(spacing: 8) {
                    if isMultipleChoice {
                        Checkbox(isSelected: isSelected)
                    } else {
                        RadioButton(isSelected: isSelected)
                    }

                    Text(text)
                        .font(.body)
                        .foregroundColor(.primaryText)
                }
            }
        )
    }
}

struct ChoiceQuizElementView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            ChoiceQuizElementView(isSelected: .constant(true), text: "Some option", isMultipleChoice: true)
            ChoiceQuizElementView(isSelected: .constant(false), text: "Some option", isMultipleChoice: true)
            ChoiceQuizElementView(isSelected: .constant(true), text: "Some option", isMultipleChoice: false)
            ChoiceQuizElementView(isSelected: .constant(false), text: "Some option", isMultipleChoice: false)
        }
    }
}
