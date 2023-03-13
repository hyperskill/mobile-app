import Foundation
import shared

extension StepQuizFeatureStateKs: Equatable {
    public static func == (lhs: StepQuizFeatureStateKs, rhs: StepQuizFeatureStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.idle, .idle):
            return true
        case (.loading, .loading):
            return true
        case (.networkError, .networkError):
            return true
        case (.unsupported, .unsupported):
            return true
        case (.attemptLoading(let lhsData), .attemptLoading(let rhsData)):
            return lhsData.isEqual(rhsData)
        case (.attemptLoaded(let lhsData), .attemptLoaded(let rhsData)):
            return lhsData.isEqual(rhsData)
        case (.attemptLoaded, .idle):
            return false
        case (.attemptLoaded, .loading):
            return false
        case (.attemptLoaded, .attemptLoading):
            return false
        case (.attemptLoaded, .networkError):
            return false
        case (.attemptLoading, .idle):
            return false
        case (.attemptLoading, .loading):
            return false
        case (.attemptLoading, .attemptLoaded):
            return false
        case (.attemptLoading, .networkError):
            return false
        case (.networkError, .idle):
            return false
        case (.networkError, .loading):
            return false
        case (.networkError, .attemptLoading):
            return false
        case (.networkError, .attemptLoaded):
            return false
        case (.loading, .idle):
            return false
        case (.loading, .attemptLoading):
            return false
        case (.loading, .attemptLoaded):
            return false
        case (.loading, .networkError):
            return false
        case (.idle, .loading):
            return false
        case (.idle, .attemptLoading):
            return false
        case (.idle, .attemptLoaded):
            return false
        case (.idle, .networkError):
            return false
        case (.attemptLoaded, .unsupported):
            return false
        case (.attemptLoading, .unsupported):
            return false
        case (.unsupported, .attemptLoaded):
            return false
        case (.unsupported, .attemptLoading):
            return false
        case (.unsupported, .idle):
            return false
        case (.unsupported, .loading):
            return false
        case (.unsupported, .networkError):
            return false
        case (.networkError, .unsupported):
            return false
        case (.loading, .unsupported):
            return false
        case (.idle, .unsupported):
            return false
        }
    }
}

extension StepQuizFeatureSubmissionStateKs: Equatable {
    public static func == (lhs: StepQuizFeatureSubmissionStateKs, rhs: StepQuizFeatureSubmissionStateKs) -> Bool {
        switch (lhs, rhs) {
        case (.empty(let lhsData), .empty(let rhsData)):
            return lhsData.isEqual(rhsData)
        case (.loaded(let lhsData), .loaded(let rhsData)):
            return lhsData.isEqual(rhsData)
        case (.loaded, .empty):
            return false
        case (.empty, .loaded):
            return false
        }
    }
}
