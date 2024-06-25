import shared
import SwiftUI

extension NotificationsOnboardingView {
    struct Appearance {
        let backgroundColor = Color.systemGroupedBackground
    }
}

struct NotificationsOnboardingView: View {
    private(set) var appearance = Appearance()

    @StateObject var viewModel: NotificationsOnboardingViewModel

    let panModalPresenter: PanModalPresenter

    var body: some View {
        ZStack {
            UIViewControllerEventsWrapper(onViewDidAppear: viewModel.logViewedEvent)

            BackgroundView(color: appearance.backgroundColor)

            NotificationsOnboardingContentView(
                formattedDailyStudyRemindersInterval: viewModel.state.formattedDailyStudyRemindersInterval,
                onDailyStudyRemindsIntervalButtonTap: viewModel.doDailyStudyRemindsIntervalButtonAction,
                onPrimaryButtonTap: viewModel.doPrimaryAction,
                onSecondaryButtonTap: viewModel.doSecondaryAction
            )
        }
        .onAppear {
            viewModel.startListening()
            viewModel.onViewAction = handleViewAction(_:)
        }
        .onDisappear {
            viewModel.stopListening()
            viewModel.onViewAction = nil
        }
    }
}

// MARK: - NotificationsOnboardingView (ViewAction) -

private extension NotificationsOnboardingView {
    func handleViewAction(
        _ viewAction: NotificationsOnboardingFeatureActionViewAction
    ) {
        switch NotificationsOnboardingFeatureActionViewActionKs(viewAction) {
        case .completeNotificationOnboarding:
            viewModel.doCompleteOnboarding()
        case .requestNotificationPermission:
            viewModel.doRequestNotificationPermission()
        case .showDailyStudyRemindersIntervalStartHourPickerModal(let data):
            let panModal = ProfileDailyStudyRemindersPickerViewController(
                rows: data.intervals,
                initialRowIndex: Int(data.dailyStudyRemindersStartHour),
                onDidConfirmRow: { [weak viewModel, weak panModalPresenter] selectedIntervalIndex in
                    guard let viewModel,
                          let panModalPresenter else {
                        return
                    }

                    viewModel.doDailyStudyRemindsIntervalStartHourSelected(startHour: selectedIntervalIndex)
                    panModalPresenter.dismissPanModal()
                }
            )

            panModal.onDidAppear = { [weak viewModel] in
                viewModel?.logDailyStudyRemindersIntervalStartHourPickerModalShownEvent()
            }
            panModal.onDidDisappear = { [weak viewModel] in
                viewModel?.logDailyStudyRemindersIntervalStartHourPickerModalHiddenEvent()
            }

            panModalPresenter.presentPanModal(panModal)
        }
    }
}

// MARK: - Preview -

@available(iOS 17, *)
#Preview {
    NotificationsOnboardingAssembly(output: nil).makeModule()
}
