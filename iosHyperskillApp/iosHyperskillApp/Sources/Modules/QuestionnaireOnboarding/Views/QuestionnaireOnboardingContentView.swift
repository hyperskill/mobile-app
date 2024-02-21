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
    let onChoiceTap: (String) -> Void

    var textInputValue: Binding<String>
    let isTextInputVisible: Bool

    let isSendButtonEnabled: Bool
    let onSendButtotTap: () -> Void
    let onSkipButtotTap: () -> Void

    let isKeyboardVisible: Bool

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
                    onTap: onChoiceTap
                )
                .padding(.vertical)

                if isTextInputVisible {
                    textInput
                        .animation(nil)
                }
            }
            .padding()
        }
        .scrollBounceBehaviorBasedOnSize()
        .safeAreaInsetBottomCompatibility(footerView)
    }

    private var textInput: some View {
        TextEditor(text: textInputValue)
            .foregroundColor(.newPrimaryText)
            .font(.body)
            .multilineTextAlignment(.leading)
            .keyboardType(.asciiCapable)
            .disableAutocorrection(true)
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

    @ViewBuilder private var footerView: some View {
        if isKeyboardVisible {
            EmptyView()
        } else {
            QuestionnaireOnboardingFooterView(
                appearance: .init(spacing: Appearance.interitemSpacing),
                isSendButtonEnabled: isSendButtonEnabled,
                onSendButtotTap: onSendButtotTap,
                onSkipButtotTap: onSkipButtotTap
            )
        }
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
        onChoiceTap: { _ in },
        textInputValue: .constant(""),
        isTextInputVisible: false,
        isSendButtonEnabled: false,
        onSendButtotTap: {},
        onSkipButtotTap: {},
        isKeyboardVisible: false
    )
}

#Preview {
    QuestionnaireOnboardingContentView(
        title: QuestionnaireOnboardingPreviewDefaults.title,
        choices: QuestionnaireOnboardingPreviewDefaults.choices,
        selectedChoice: QuestionnaireOnboardingPreviewDefaults.choices.last,
        onChoiceTap: { _ in },
        textInputValue: .constant(""),
        isTextInputVisible: true,
        isSendButtonEnabled: false,
        onSendButtotTap: {},
        onSkipButtotTap: {},
        isKeyboardVisible: false
    )
}
#endif
