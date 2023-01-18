import shared
import SwiftUI

extension StepTheoryContentView {
    struct Appearance {
        let interItemSpacing: CGFloat = 24
    }
}

struct StepTheoryContentView: View {
    private(set) var appearance = Appearance()

    @State var viewData: StepViewData

    let practiceStatus: StepFeaturePracticeStatus

    let onStartPracticingTap: () -> Void

    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading, spacing: appearance.interItemSpacing) {
                StepHeaderView(
                    title: viewData.formattedType,
                    timeToComplete: viewData.formattedTimeToComplete
                )

                buildPracticingButton()
                    .buttonStyle(OutlineButtonStyle(style: .violet))

                StepTextView(text: viewData.text)

                buildPracticingButton()
                    .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
            }
            .padding()
        }
    }

    @ViewBuilder
    private func buildPracticingButton() -> some View {
        if practiceStatus == StepFeaturePracticeStatus.unavailable {
            EmptyView()
        } else {
            Button(
                action: onStartPracticingTap,
                label: {
                    if practiceStatus == StepFeaturePracticeStatus.loading {
                        ProgressView()
                    } else {
                        Text(Strings.Step.startPracticing)
                    }
                }
            )
            .disabled(practiceStatus == StepFeaturePracticeStatus.loading)
        }
    }
}

#if DEBUG
struct StepContentView_Previews: PreviewProvider {
    static var previews: some View {
        StepTheoryContentView(
            viewData: .placeholder,
            practiceStatus: StepFeaturePracticeStatus.available,
            onStartPracticingTap: {}
        )
    }
}
#endif
