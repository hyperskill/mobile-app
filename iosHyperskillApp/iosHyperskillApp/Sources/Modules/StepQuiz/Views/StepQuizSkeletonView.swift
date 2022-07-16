import SwiftUI

extension StepQuizSkeletonView {
    struct Appearance {
        let cornerRadius: CGFloat = 8
    }
}

struct StepQuizSkeletonView: View {
    private(set) var appearance = Appearance()

    let quizType: StepQuizChildQuizType

    var body: some View {
        VStack(spacing: LayoutInsets.largeInset) {
            ForEach(Array(quizType.skeletons.enumerated()), id: \.offset) { element in
                EmptyView()
                    .skeleton(with: true)
                    .shape(type: .rectangle)
                    .cornerRadius(appearance.cornerRadius)
                    .frame(height: element.element)
            }
        }
    }
}

fileprivate extension StepQuizChildQuizType {
    var skeletons: [CGFloat] {
        switch self {
        case .string:
            return [96, 48]
        default:
            return [21, 21, 21, 21, 48]
        }
    }
}

struct StepQuizSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizSkeletonView(quizType: .choice)
        }
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
