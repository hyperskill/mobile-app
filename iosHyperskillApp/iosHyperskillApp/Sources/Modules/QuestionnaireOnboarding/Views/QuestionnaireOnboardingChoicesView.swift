import SwiftUI

extension QuestionnaireOnboardingChoicesView {
    struct Appearance {
        var spacing = LayoutInsets.smallInset

        let textFont = UIFont.preferredFont(forTextStyle: .body)
        var radioButtonSize: CGFloat {
            textFont.pointSize * 1.15
        }

        let selectedTextColor = Color.newPrimaryText
        let unselectedTextColor = Color.newSecondaryText
    }
}

struct QuestionnaireOnboardingChoicesView: View {
    private(set) var appearance = Appearance()

    let choices: [String]
    let selectedChoice: String?

    let onTap: (String) -> Void

    var body: some View {
        VStack(spacing: appearance.spacing) {
            ForEach(choices, id: \.self) { choice in
                Button(
                    action: {
                        withAnimation {
                            handleTap(on: choice)
                        }
                    },
                    label: {
                        let isSelected = choice == selectedChoice

                        HStack(spacing: appearance.spacing) {
                            RadioButton(
                                appearance: .init(
                                    borderUnselectedColor: appearance.unselectedTextColor
                                ),
                                isSelected: isSelected,
                                onClick: {
                                    withAnimation {
                                        handleTap(on: choice)
                                    }
                                }
                            )
                            .frame(widthHeight: appearance.radioButtonSize)

                            Text(choice)
                                .font(Font(appearance.textFont))
                                .foregroundColor(
                                    isSelected ? appearance.selectedTextColor : appearance.unselectedTextColor
                                )
                                .frame(maxWidth: .infinity, alignment: .leading)
                        }
                        .padding(.vertical, appearance.spacing)
                        .animation(.default, value: isSelected)
                    }
                )
            }
        }
    }

    @MainActor
    private func handleTap(on choice: String) {
        FeedbackGenerator(feedbackType: .selection).triggerFeedback()
        onTap(choice)
    }
}

#if DEBUG
#Preview {
    QuestionnaireOnboardingChoicesView(
        choices: QuestionnaireOnboardingPreviewDefaults.choices,
        selectedChoice: QuestionnaireOnboardingPreviewDefaults.choices.first,
        onTap: { _ in }
    )
    .padding()
}
#endif
