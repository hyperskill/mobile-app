import SwiftUI

struct SingleOptionView: View {
    var option: QuizOption
    @Binding var chosenValue: Int
    var chosen: Bool { option.value == chosenValue }

    var body: some View {
        HStack(spacing: 8) {
            Button(
                action: {
                    chosenValue = option.value
                },
                label: {
                    Circle()
                        .frame(width: 24, height: 24)
                        .foregroundColor(Color(.white))
                        .addBorder(
                            color: Color(
                                chosen ?
                                ColorPalette.primary : ColorPalette.onSurfaceAlpha60
                            ),
                            width: 2,
                            cornerRadius: 24
                        )
                        .overlay(
                            Circle()
                                .foregroundColor(
                                    chosen ?
                                    Color(ColorPalette.primary) : .white
                                )
                                .frame(width: 12, height: 12)
                        )

                    Text(option.desc)
                        .font(.body)
                        .foregroundColor(.primaryText)
                }
            )
        }
    }
}

struct SingleOptionView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            SingleOptionView(option: ChoiceView.options[0], chosenValue: .constant(0))

            SingleOptionView(option: ChoiceView.options[0], chosenValue: .constant(1))
        }
    }
}
