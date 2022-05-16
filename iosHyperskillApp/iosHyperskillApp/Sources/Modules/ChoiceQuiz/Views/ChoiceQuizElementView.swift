import SwiftUI

struct ChoiceQuizElementView: View {
    var isSelected: Binding<Bool>
    let text: String
    var isMultipleChoice: Bool
    var selectChoice: (Binding<Bool>) -> Void
    var body: some View {
        Button(
            action: {
                selectChoice(isSelected)
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
        let lambda = { (selectedChoice: Binding<Bool>) -> Void in selectedChoice.wrappedValue.toggle() }
        return Group {
            ChoiceQuizElementView(
                isSelected: .constant(true),
                text: "Some option",
                isMultipleChoice: true,
                selectChoice: lambda
            )
            ChoiceQuizElementView(
                isSelected: .constant(false),
                text: "Some option",
                isMultipleChoice: true,
                selectChoice: lambda
            )
            ChoiceQuizElementView(
                isSelected: .constant(true),
                text: "Some option",
                isMultipleChoice: false,
                selectChoice: lambda
            )
            ChoiceQuizElementView(
                isSelected: .constant(false),
                text: "Some option",
                isMultipleChoice: false,
                selectChoice: lambda
            )
        }
    }
}
