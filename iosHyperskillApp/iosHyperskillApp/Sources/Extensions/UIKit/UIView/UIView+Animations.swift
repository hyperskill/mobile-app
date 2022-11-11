import UIKit

private enum Animation {
    static let defaultBounceDuration: TimeInterval = 0.15
    static let defaultBounceScale: CGFloat = 0.95
}

extension UIView {
    func animateBounce(
        withDuration duration: TimeInterval = Animation.defaultBounceDuration,
        bounceScale scale: CGFloat = Animation.defaultBounceScale,
        isScaled: Bool
    ) {
        UIView.animate(withDuration: duration) {
            self.transform = isScaled
                ? CGAffineTransform(scaleX: scale, y: scale)
                : .identity
        }
    }
}

extension UIControl {
    func animateBounce(
        withDuration duration: TimeInterval = Animation.defaultBounceDuration,
        bounceScale scale: CGFloat = Animation.defaultBounceScale
    ) {
        animateBounce(withDuration: duration, bounceScale: scale, isScaled: isHighlighted)
    }
}
