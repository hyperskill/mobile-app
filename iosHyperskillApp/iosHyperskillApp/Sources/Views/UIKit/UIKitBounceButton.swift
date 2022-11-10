import UIKit

class UIKitBounceButton: UIButton {
    private var bounceDuration: TimeInterval
    private var bounceScale: CGFloat

    override var isHighlighted: Bool {
        didSet {
            animateBounce(withDuration: bounceDuration, bounceScale: bounceScale)
        }
    }

    init(
        frame: CGRect = .zero,
        bounceDuration: TimeInterval = 0.15,
        bounceScale: CGFloat = 0.95
    ) {
        self.bounceDuration = bounceDuration
        self.bounceScale = bounceScale
        super.init(frame: frame)
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
