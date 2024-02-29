import SwiftUI

private struct TaskCompatibilityModifier: ViewModifier {
    let priority: TaskPriority

    let action: @Sendable () async -> Void

    @State var task: Task<Void, Never>?

    func body(content: Content) -> some View {
        content
            .onAppear {
                if task != nil {
                    task?.cancel()
                }
                task = Task(priority: priority, operation: action)
            }
            .onDisappear {
                task?.cancel()
            }
    }
}

extension View {
    @ViewBuilder
    @available(iOS, deprecated: 15.0)
    func taskCompatibility(
        priority: TaskPriority = .userInitiated,
        _ action: @escaping @Sendable () async -> Void
    ) -> some View {
        if #available(iOS 15.0, *) {
            self.task(priority: priority, action)
        } else {
            self.modifier(TaskCompatibilityModifier(priority: priority, action: action))
        }
    }
}
