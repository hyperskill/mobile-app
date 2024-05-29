import AVFoundation
import AVKit
import SwiftUI

class BackgroundVideoUIView: UIView {
    private var currentResourceName: String?
    private var currentResourceType: String?

    private var playerLayer: AVPlayerLayer?
    private var playerLooper: AVPlayerLooper?
    private var player: AVQueuePlayer?

    init(frame: CGRect = .zero, resourceName: String?, resourceType: String?) {
        super.init(frame: frame)

        if let resourceName, let resourceType {
            play(with: resourceName, ofType: resourceType)
        }

        addNotificationObservers()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    deinit {
        removeNotificationObservers()
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        playerLayer?.frame = bounds
    }

    func play(with resourceName: String, ofType type: String) {
        guard currentResourceName != resourceName || currentResourceType != type else {
            return
        }

        currentResourceName = resourceName
        currentResourceType = type

        guard let path = Bundle.main.path(forResource: resourceName, ofType: type) else {
            return assertionFailure("Video file not found")
        }

        let url = URL(fileURLWithPath: path)
        let playerItem = AVPlayerItem(url: url)

        player = AVQueuePlayer(playerItem: playerItem)
        player?.isMuted = true

        playerLayer = AVPlayerLayer(player: player)
        playerLayer?.videoGravity = .resizeAspectFill

        playerLooper = AVPlayerLooper(player: player.require(), templateItem: playerItem)

        layer.sublayers?.forEach { $0.removeFromSuperlayer() }
        layer.addSublayer(playerLayer.require())

        player?.play()
    }
}

// MARK: BackgroundVideoUIView (NotificationCenter)

extension BackgroundVideoUIView {
    private func addNotificationObservers() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(handleApplicationWillEnterForeground),
            name: UIApplication.willEnterForegroundNotification,
            object: nil
        )
    }

    private func removeNotificationObservers() {
        NotificationCenter.default.removeObserver(
            self,
            name: UIApplication.willEnterForegroundNotification,
            object: nil
        )
    }

    @objc
    private func handleApplicationWillEnterForeground() {
        // Resume video when application re-enters foreground
        player?.play()
    }
}

// MARK: - BackgroundVideoView: UIViewRepresentable -

struct BackgroundVideoView: UIViewRepresentable {
    let resourceName: String
    let resourceType: String

    func makeUIView(context: Context) -> BackgroundVideoUIView {
        BackgroundVideoUIView(resourceName: resourceName, resourceType: resourceType)
    }

    func updateUIView(_ uiView: BackgroundVideoUIView, context: Context) {
        uiView.play(with: resourceName, ofType: resourceType)
    }
}
