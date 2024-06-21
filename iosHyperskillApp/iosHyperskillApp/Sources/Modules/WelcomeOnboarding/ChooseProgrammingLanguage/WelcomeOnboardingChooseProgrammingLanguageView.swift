import shared
import SwiftUI

extension WelcomeOnboardingChooseProgrammingLanguageView {
    enum Appearance {
        static let spacing = LayoutInsets.defaultInset

        static let iconSize: CGFloat = 16
    }
}

struct WelcomeOnboardingChooseProgrammingLanguageView: View {
    let languages: [WelcomeOnboardingProgrammingLanguage] = [.java, .javaScript, .kotlin, .python, .sql, .undefined]

    let onViewDidAppear: () -> Void
    let onProgrammingLanguageTap: (WelcomeOnboardingProgrammingLanguage) -> Void

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: onViewDidAppear)

            BackgroundView(color: Color.systemGroupedBackground)

            ScrollView {
                VStack(spacing: Appearance.spacing * 2) {
                    Text(Strings.WelcomeOnboarding.ChooseProgrammingLanguage.title)
                        .font(.title).bold()
                        .foregroundColor(.newPrimaryText)
                        .multilineTextAlignment(.center)

                    list
                }
                .padding()
            }
            .scrollBounceBehaviorBasedOnSize()
        }
    }

    @MainActor private var list: some View {
        VStack(spacing: Appearance.spacing) {
            ForEach(languages, id: \.self) { language in
                listItem(language)
            }
        }
    }

    @MainActor
    private func listItem(_ language: WelcomeOnboardingProgrammingLanguage) -> some View {
        Button(
            action: {
                FeedbackGenerator(feedbackType: .selection).triggerFeedback()
                onProgrammingLanguageTap(language)
            },
            label: {
                HStack(alignment: .center, spacing: Appearance.spacing) {
                    if let imageResource = language.imageResource {
                        Image(imageResource)
                            .renderingMode(.original)
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(widthHeight: Appearance.iconSize)
                    }

                    Text(language.title)
                        .font(.body)
                        .foregroundColor(.newPrimaryText)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding()
                .background(Color.systemSecondaryGroupedBackground)
                .addBorder(color: .clear, width: 0)
            }
        )
        .buttonStyle(BounceButtonStyle())
    }
}

private extension WelcomeOnboardingProgrammingLanguage {
    // swiftlint:disable switch_case_on_newline
    var title: String? {
        switch self {
        case .java: Strings.WelcomeOnboarding.ChooseProgrammingLanguage.langJava
        case .javaScript: Strings.WelcomeOnboarding.ChooseProgrammingLanguage.langJS
        case .kotlin: Strings.WelcomeOnboarding.ChooseProgrammingLanguage.langKotlin
        case .python: Strings.WelcomeOnboarding.ChooseProgrammingLanguage.langPython
        case .sql: Strings.WelcomeOnboarding.ChooseProgrammingLanguage.langSQL
        case .undefined: Strings.WelcomeOnboarding.ChooseProgrammingLanguage.langNotSure
        default: nil
        }
    }

    var imageResource: ImageResource? {
        switch self {
        case .java: .welcomeOnboardingLangJava
        case .javaScript: .welcomeOnboardingLangJs
        case .kotlin: .welcomeOnboardingLangKt
        case .python: .welcomeOnboardingLangPy
        case .sql: .welcomeOnboardingLangSql
        case .undefined: .welcomeOnboardingOther
        default: nil
        }
    }
    // swiftlint:enable switch_case_on_newline
}

#if DEBUG
#Preview {
    WelcomeOnboardingChooseProgrammingLanguageView(
        onViewDidAppear: {},
        onProgrammingLanguageTap: { _ in }
    )
}
#endif
