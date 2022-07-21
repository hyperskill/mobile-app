import SwiftUI

enum StepQuizSkeletonViewFactory {
    @ViewBuilder
    static func makeSkeleton(for quizType: StepQuizChildQuizType) -> some View {
        switch quizType {
        case .choice:
            StepQuizChoiceSkeletonView()
        case .matching:
            StepQuizMatchingSkeletonView()
        case .sorting:
            StepQuizSortingSkeletonView()
        case .table:
            StepQuizTableSkeletonView()
        case .string, .number, .math:
            StepQuizStringSkeletonView()
        case .unsupported(let blockName):
            // swiftlint:disable:next redundant_discardable_let https://github.com/realm/SwiftLint/issues/3855
            let _ = print("StepQuizSkeletonViewFactory :: did receive unsupported quizType = \(blockName)")
            SkeletonRoundedView()
                .frame(height: 100)
        }
    }
}
