import SwiftUI

enum StepQuizSkeletonViewFactory {
    @ViewBuilder
    static func makeSkeleton(for quizType: StepQuizChildQuizType) -> some View {
        switch quizType {
        case .choice:
            StepQuizChoiceSkeletonView()
        case .code:
            StepQuizCodeSkeletonView()
        case .sql, .pycharm:
            StepQuizSQLSkeletonView()
        case .sorting:
            StepQuizSortingSkeletonView()
        case .table:
            StepQuizTableSkeletonView(itemHeight: 44)
        case .matching:
            StepQuizTableSkeletonView(itemHeight: 50)
        case .string, .number, .math, .prompt:
            StepQuizStringSkeletonView()
        case .parsons:
            StepQuizParsonsSkeletonView()
        case .fillBlanks:
            StepQuizFillBlanksSkeletonView()
        case .unsupported:
            SkeletonRoundedView()
                .frame(height: 100)
        }
    }
}
