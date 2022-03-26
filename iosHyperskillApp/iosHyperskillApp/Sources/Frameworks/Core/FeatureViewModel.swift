import Combine
import Foundation
import shared

class FeatureViewModel<State, Message, ViewAction>: ObservableObject {
    private let feature: Presentation_reduxFeature

    private var isListeningForChanges = false

    private var viewActionQueue = Queue<ViewAction>()

    var state: State {
        get {
            if let state = self.feature.state as? State {
                return state
            }
            #if DEBUG
            fatalError("FeatureViewModel :: unexpected state type = \(String(describing: self.feature.state))")
            #endif
        }
    }
    
    var onViewAction: ((ViewAction) -> Void)?

    init(feature: Presentation_reduxFeature) {
        self.feature = feature

        feature.addStateListener { [weak self] newState in
            self?.handleState(newState)
        }
        feature.addActionListener { [weak self] action in
            self?.handleViewAction(action)
        }
    }

    deinit {
        self.feature.cancel()
    }

    // MARK: Public API

    func onNewMessage(_ message: Message) {
        self.feature.onNewMessage(message_: message)
    }

    func startListening() {
        self.isListeningForChanges = true

        self.updateState(newState: self.state)

        guard let onViewAction = self.onViewAction else {
            return
        }

        while let queuedViewAction = self.viewActionQueue.dequeue() {
            onViewAction(queuedViewAction)
        }
    }

    func stopListening() {
        self.isListeningForChanges = false
    }

    // MARK: Private API

    private func updateState(newState: State) {
        guard self.isListeningForChanges else {
            return
        }

        self.objectWillChange.send()
    }

    private func handleState(_ newState: Any?) {
        guard let newState = newState as? State else {
            return
        }

        self.updateState(newState: newState)
    }

    private func handleViewAction(_ viewAction: Any?) {
        guard let viewAction = viewAction as? ViewAction else {
            return
        }

        if self.isListeningForChanges,
           let onViewAction = self.onViewAction {
            onViewAction(viewAction)
        } else {
            self.viewActionQueue.enqueue(value: viewAction)
        }
    }
}
