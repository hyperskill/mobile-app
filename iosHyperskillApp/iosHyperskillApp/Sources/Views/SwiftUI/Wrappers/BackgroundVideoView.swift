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
            self.play(with: resourceName, ofType: resourceType)
        }
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
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
        playerLayer = AVPlayerLayer(player: player)
        playerLayer?.videoGravity = .resizeAspectFill
        playerLooper = AVPlayerLooper(player: player.require(), templateItem: playerItem)

        layer.sublayers?.forEach { $0.removeFromSuperlayer() }
        layer.addSublayer(playerLayer.require())

        player?.play()
    }
}

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
