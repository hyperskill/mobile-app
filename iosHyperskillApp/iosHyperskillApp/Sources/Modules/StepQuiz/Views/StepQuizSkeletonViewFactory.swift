import SwiftUI

enum StepQuizSkeletonViewFactory {
    @ViewBuilder
    static func makeSkeleton(for quizType: StepQuizChildQuizType) -> some View {
        switch quizType {
        case .choice:
            StepQuizChoiceSkeletonView()
        case .code:
            StepQuizCodeSkeletonView()
        case .sql:
            StepQuizSQLSkeletonView()
        case .matching:
            StepQuizMatchingSkeletonView()
        case .sorting:
            StepQuizSortingSkeletonView()
        case .table:
            StepQuizTableSkeletonView()
        case .string, .number, .math:
            StepQuizStringSkeletonView()
        case .unsupported:
            SkeletonRoundedView()
                .frame(height: 100)
        }
    }
}
