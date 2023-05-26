import Foundation
import shared

enum SharedProjectLevelWrapper: Int {
    case easy
    case medium
    case hard
    case nightmare

    var title: String {
        switch self {
        case .easy:
            return Strings.ProjectSelectionList.List.Category.easyTitle
        case .medium:
            return Strings.ProjectSelectionList.List.Category.mediumTitle
        case .hard:
            return Strings.ProjectSelectionList.List.Category.hardTitle
        case .nightmare:
            return Strings.ProjectSelectionList.List.Category.nightmareTitle
        }
    }

    var description: String {
        switch self {
        case .easy:
            return Strings.ProjectSelectionList.List.Category.easyDescription
        case .medium:
            return Strings.ProjectSelectionList.List.Category.mediumDescription
        case .hard:
            return Strings.ProjectSelectionList.List.Category.hardDescription
        case .nightmare:
            return Strings.ProjectSelectionList.List.Category.nightmareDescription
        }
    }

    var iconImageName: String {
        switch self {
        case .easy:
            return Images.ProjectSelectionList.ProjectLevel.easy
        case .medium:
            return Images.ProjectSelectionList.ProjectLevel.medium
        case .hard:
            return Images.ProjectSelectionList.ProjectLevel.hard
        case .nightmare:
            return Images.ProjectSelectionList.ProjectLevel.nightmare
        }
    }
}

extension SharedProjectLevelWrapper: Comparable {
    static func < (lhs: SharedProjectLevelWrapper, rhs: SharedProjectLevelWrapper) -> Bool {
        lhs.rawValue < rhs.rawValue
    }
}

extension SharedProjectLevelWrapper {
    var sharedProjectLevel: ProjectLevel {
        switch self {
        case .easy:
            return ProjectLevel.easy
        case .medium:
            return ProjectLevel.medium
        case .hard:
            return ProjectLevel.hard
        case .nightmare:
            return ProjectLevel.nightmare
        }
    }

    init?(sharedProjectLevel: ProjectLevel) {
        switch sharedProjectLevel {
        case ProjectLevel.easy:
            self = .easy
        case ProjectLevel.medium:
            self = .medium
        case ProjectLevel.hard:
            self = .hard
        case ProjectLevel.nightmare:
            self = .nightmare
        default:
            assertionFailure("Did receive unsupported ProjectLevel = \(sharedProjectLevel)")
            return nil
        }
    }
}
