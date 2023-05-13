import Foundation
import shared

extension ProjectLevel {
    var title: String? {
        switch self {
        case .easy:
            return Strings.ProjectSelectionList.List.Category.easyTitle
        case .medium:
            return Strings.ProjectSelectionList.List.Category.mediumTitle
        case .hard:
            return Strings.ProjectSelectionList.List.Category.hardTitle
        case .nightmare:
            return Strings.ProjectSelectionList.List.Category.nightmareTitle
        default:
            assertionFailure("Did receive unsupported ProjectLevel = \(self)")
            return nil
        }
    }

    var imageName: String? {
        switch self {
        case .easy:
            return Images.ProjectSelectionList.ProjectLevel.easy
        case .medium:
            return Images.ProjectSelectionList.ProjectLevel.medium
        case .hard:
            return Images.ProjectSelectionList.ProjectLevel.hard
        case .nightmare:
            return Images.ProjectSelectionList.ProjectLevel.nightmare
        default:
            assertionFailure("Did receive unsupported ProjectLevel = \(self)")
            return nil
        }
    }
}
