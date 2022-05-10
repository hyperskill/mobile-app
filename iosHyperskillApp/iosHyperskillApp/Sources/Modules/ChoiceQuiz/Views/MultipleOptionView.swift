import SwiftUI

struct MultipleOptionView: View {
    var option: QuizOption
    @Binding var chosenValues: Set<Int>
    var chosen: Bool { chosenValues.contains(option.value) }

    var body: some View {
        
        Button(
            action: {
                if chosen {
                    chosenValues.remove(option.value)
                } else {
                    chosenValues.insert(option.value)
                }
            },
            label: {
                HStack(spacing: 8) {
                    RoundedRectangle(cornerRadius: 4)
                        .frame(width: 18, height: 18)
                        .foregroundColor(Color(.white))
                        .padding(2)
                        .overlay(
                            Image("choice-quiz-check-icon")
                                .foregroundColor(.white)
                                .background(Color(chosen ? ColorPalette.primary : .white))
                                .frame(width: 12, height: 12)
                        )
                        .addBorder(
                            color: Color(
                                chosen ?
                                ColorPalette.primary : ColorPalette.onSurfaceAlpha60
                            ),
                            width: 2,
                            cornerRadius: 4
                        )
                    Text(option.desc)
                        .font(.body)
                        .foregroundColor(.primaryText)
                }
            }
        )
    }
}

struct MultipleOptionView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            MultipleOptionView(option: ChoiceView.options[0], chosenValues: .constant([0]))
            MultipleOptionView(option: ChoiceView.options[0], chosenValues: .constant([1]))
        }
    }
}
