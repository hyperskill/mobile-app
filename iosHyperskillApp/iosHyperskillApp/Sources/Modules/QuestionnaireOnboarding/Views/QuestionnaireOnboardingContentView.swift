import SwiftUI

extension QuestionnaireOnboardingContentView {
    enum Appearance {
        static let spacing = LayoutInsets.defaultInset
        static let interitemSpacing = LayoutInsets.smallInset

        static let textInputHeight: CGFloat = 58
        static let textInputPlaceholderInsets = EdgeInsets(top: 16, leading: 13, bottom: 16, trailing: 16)
    }
}

struct QuestionnaireOnboardingContentView: View {
    let title: String

    let choices: [String]
    let selectedChoice: String?

    var textInputValue: Binding<String>
    let isTextInputVisible: Bool

    var body: some View {
        ScrollView {
            VStack(spacing: Appearance.spacing) {
                Text(title)
                    .font(.title).bold()
                    .foregroundColor(.newPrimaryText)
                    .multilineTextAlignment(.center)

                QuestionnaireOnboardingChoicesView(
                    appearance: .init(spacing: Appearance.interitemSpacing),
                    choices: choices,
                    selectedChoice: selectedChoice,
                    onTap: { _ in }
                )
                .padding(.vertical)

                if isTextInputVisible {
                    textInput
                }
            }
            .padding()
        }
        .scrollBounceBehaviorBasedOnSize()
        .safeAreaInsetBottomCompatibility(
            QuestionnaireOnboardingFooterView(
                appearance: .init(spacing: Appearance.interitemSpacing),
                isSendButtotDisabled: false,
                onSendButtotTap: {},
                onSkipButtotTap: {}
            )
        )
    }

    private var textInput: some View {
        TextEditor(text: textInputValue)
            .foregroundColor(.newPrimaryText)
            .font(.body)
            .multilineTextAlignment(.leading)
            .frame(height: Appearance.textInputHeight)
            .frame(maxWidth: .infinity)
            .padding(LayoutInsets.small.edgeInsets)
            .overlay(
                Text(Strings.QuestionnaireOnboarding.textInputPlaceholder)
                    .font(.body)
                    .foregroundColor(.newSecondaryText)
                    .allowsHitTesting(false)
                    .padding(Appearance.textInputPlaceholderInsets)
                    .opacity(textInputValue.wrappedValue.isEmpty ? 1 : 0)
                ,
                alignment: .topLeading
            )
            .addBorder()
    }
}

#if DEBUG
enum QuestionnaireOnboardingPreviewDefaults {
    static let title = "How did you hear about\nMy Hyperskill?"
    static let choices = [
        "Google Play",
        "Google Search",
        "YouTube",
        "Instagram",
        "TikTok",
        "News/article/blog",
        "Friends/family",
        "Other"
    ]
}

#Preview {
    QuestionnaireOnboardingContentView(
        title: QuestionnaireOnboardingPreviewDefaults.title,
        choices: QuestionnaireOnboardingPreviewDefaults.choices,
        selectedChoice: nil,
        textInputValue: .constant(""),
        isTextInputVisible: false
    )
}

#Preview {
    QuestionnaireOnboardingContentView(
        title: QuestionnaireOnboardingPreviewDefaults.title,
        choices: QuestionnaireOnboardingPreviewDefaults.choices,
        selectedChoice: QuestionnaireOnboardingPreviewDefaults.choices.last,
        textInputValue: .constant(""),
        isTextInputVisible: true
    )
}
#endif
