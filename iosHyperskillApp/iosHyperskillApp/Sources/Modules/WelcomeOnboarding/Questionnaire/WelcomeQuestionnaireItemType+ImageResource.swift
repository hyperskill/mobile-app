import shared
import SwiftUI

extension WelcomeQuestionnaireItemType {
    // swiftlint:disable switch_case_on_newline
    var imageResource: ImageResource? {
        switch WelcomeQuestionnaireItemTypeKs(self) {
        case .clientSource(let source):
            switch source {
            case .tikTok: .welcomeOnboardingTiktok
            case .googleSearch: .authSocialGoogleLogo
            case .news: .welcomeOnboardingNews
            case .appStore: .authSocialAppleLogo
            case .facebook: .welcomeOnboardingFacebook
            case .friends: .welcomeOnboardingFriends
            case .youtube: .welcomeOnboardingYoutube
            case .other: .welcomeOnboardingOther
            default: nil
            }
        case .learningGoal(let goal):
            switch goal {
            case .startCareer: .welcomeOnboardingStartCareer
            case .currentJob: .welcomeOnboardingCurrentJob
            case .changeStack: .welcomeOnboardingChangeStack
            case .studies: .welcomeOnboardingStudies
            case .fun: .welcomeOnboardingFun
            case .other: .welcomeOnboardingOtherLearningGoal
            default: nil
            }
        case .codingBackground(let background):
            switch background {
            case .noCodingExperience: .welcomeOnboardingNoCodingExperience
            case .basicUnderstanding: .welcomeOnboardingBasicUnderstanding
            case .writtenSomeProjects: .welcomeOnboardingWrittenSomeProjects
            case .workingProfessionally: .welcomeOnboardingWorkingProfessionally
            default: nil
            }
        }
    }
    // swiftlint:enable switch_case_on_newline
}
