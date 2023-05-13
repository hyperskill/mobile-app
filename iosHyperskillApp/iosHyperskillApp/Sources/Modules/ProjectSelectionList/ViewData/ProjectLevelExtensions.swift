import Foundation
import shared

extension ProjectLevel {
    var title: String? {
        switch self {
        case .easy:
            return Strings.ProjectSelectionList.List.easyCategoryTitle
        case .medium:
            return Strings.ProjectSelectionList.List.mediumCategoryTitle
        case .hard:
            return Strings.ProjectSelectionList.List.hardCategoryTitle
        case .nightmare:
            return Strings.ProjectSelectionList.List.nightmareCategoryTitle
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
