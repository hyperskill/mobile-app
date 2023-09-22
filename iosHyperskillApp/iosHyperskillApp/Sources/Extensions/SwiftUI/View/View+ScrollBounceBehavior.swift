import SwiftUI

extension View {
    func scrollBounceBehaviorBasedOnSize(axes: Axis.Set = [.vertical]) -> some View {
        if #available(iOS 16.4, *) {
            return self.scrollBounceBehavior(.basedOnSize, axes: axes)
        } else {
            return self
        }
    }

    func scrollBounceBehaviorAtomatic(axes: Axis.Set = [.vertical]) -> some View {
        if #available(iOS 16.4, *) {
            return self.scrollBounceBehavior(.automatic, axes: axes)
        } else {
            return self
        }
    }

    func scrollBounceBehaviorAlways(axes: Axis.Set = [.vertical]) -> some View {
        if #available(iOS 16.4, *) {
            return self.scrollBounceBehavior(.always, axes: axes)
        } else {
            return self
        }
    }
}
