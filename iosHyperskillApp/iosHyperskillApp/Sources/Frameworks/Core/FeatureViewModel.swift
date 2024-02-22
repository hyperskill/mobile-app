import Combine
import CombineSchedulers
import Foundation
import shared

class FeatureViewModel<State, Message, ViewAction>: ObservableObject {
    private let feature: Presentation_reduxFeature

    /// Schedules state changes and view actions on the next runloop tick.
    /// Helps to solve problem with "Publishing changes from within view updates is not allowed, this will cause undefined behavior."
    let mainScheduler: AnySchedulerOf<RunLoop>

    private var viewActionQueue = Queue<ViewAction>()
    private var isListeningForChanges = false

    /// Represents the last feature state that was rendered.
    /// See `updateState(newState:)` logic for more info.
    private var oldState: State
    /// Represents the current feature state.
    var state: State {
        if let state = self.feature.state as? State {
            return state
        }
        fatalError("FeatureViewModel :: unexpected state type = \(String(describing: self.feature.state))")
    }

    var onViewAction: ((ViewAction) -> Void)?

    init(feature: Presentation_reduxFeature, mainScheduler: AnySchedulerOf<RunLoop> = .main) {
        self.feature = feature
        self.mainScheduler = mainScheduler
        self.oldState = (feature.state as? State).require()

        feature.addStateListener { [weak self] newState in
            self?.handleState(newState)
        }
        feature.addActionListener { [weak self] action in
            self?.handleViewAction(action)
        }
    }

    deinit {
        feature.cancel()
    }

    // MARK: Public API

    func onNewMessage(_ message: Message) {
        mainScheduler.schedule { self.feature.onNewMessage(message_: message) }
    }

    func startListening() {
        isListeningForChanges = true

        updateState(newState: state)

        guard let onViewAction else {
            return
        }

        while let queuedViewAction = viewActionQueue.dequeue() {
            mainScheduler.schedule { onViewAction(queuedViewAction) }
        }
    }

    func stopListening() {
        isListeningForChanges = false
    }

    func shouldNotifyStateDidChange(oldState: State, newState: State) -> Bool { true }

    // MARK: Private API

    private func updateState(newState: State) {
        defer {
            if isListeningForChanges {
                oldState = newState
            }
        }

        guard isListeningForChanges,
              shouldNotifyStateDidChange(oldState: oldState, newState: newState) else {
            return
        }

        mainScheduler.schedule { self.objectWillChange.send() }
    }

    private func handleState(_ newState: Any?) {
        guard let newState = newState as? State else {
            return
        }

        updateState(newState: newState)
    }

    private func handleViewAction(_ viewAction: Any?) {
        guard let viewAction = viewAction as? ViewAction else {
            return
        }

        if isListeningForChanges,
           let onViewAction {
            mainScheduler.schedule { onViewAction(viewAction) }
        } else {
            viewActionQueue.enqueue(value: viewAction)
        }
    }
}
