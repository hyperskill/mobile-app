import SwiftUI

struct InterviewPreparationWidgetView: View {
    let viewStateKs: InterviewPreparationWidgetViewStateKs

    let viewModel: InterviewPreparationWidgetViewModel

    var body: some View {
        Text("Hello, World!")
    }
}

extension InterviewPreparationWidgetView: Equatable {
    static func == (lhs: InterviewPreparationWidgetView, rhs: InterviewPreparationWidgetView) -> Bool {
        lhs.viewStateKs == rhs.viewStateKs
    }
}

#Preview("Error") {
    InterviewPreparationWidgetView(
        viewStateKs: .error,
        viewModel: InterviewPreparationWidgetViewModel()
    )
    .padding()
    .background(Color.systemGroupedBackground)
}
