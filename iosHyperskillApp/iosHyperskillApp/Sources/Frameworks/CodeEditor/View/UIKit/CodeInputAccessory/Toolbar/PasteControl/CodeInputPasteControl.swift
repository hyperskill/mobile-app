import SnapKit
import UIKit

extension CodeInputPasteControl {
    struct Appearance {
        let backgroundColor = CodeInputAccessoryCollectionViewCell.Appearance.backgroundColor
        let foregroundColor = CodeInputAccessoryCollectionViewCell.Appearance.textColor
        let cornerRadius = CodeInputAccessoryCollectionViewCell.Appearance.cornerRadius

        let imageViewSize = CGSize(width: 26, height: 26)
    }
}

final class CodeInputPasteControl: UIView {
    let appearance: Appearance

    private weak var pasteConfigurationSupporting: UIPasteConfigurationSupporting?

    init(
        frame: CGRect = .zero,
        appearance: Appearance = Appearance(),
        pasteConfigurationSupporting: UIPasteConfigurationSupporting?
    ) {
        self.appearance = appearance
        self.pasteConfigurationSupporting = pasteConfigurationSupporting
        super.init(frame: frame)

        setupView()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    private func setupView() {
        if #available(iOS 16.0, *) {
            let configuration = UIPasteControl.Configuration()
            configuration.baseBackgroundColor = appearance.backgroundColor
            configuration.baseForegroundColor = appearance.foregroundColor
            configuration.cornerRadius = appearance.cornerRadius
            configuration.cornerStyle = .fixed
            configuration.displayMode = .iconOnly

            let pasteControl = OpaqueUIPasteControl(configuration: configuration)
            pasteControl.target = pasteConfigurationSupporting

            addSubview(pasteControl)
            pasteControl.translatesAutoresizingMaskIntoConstraints = false
            pasteControl.snp.makeConstraints { make in
                make.edges.equalToSuperview()
            }
        } else {
            let backgroundView = UIView()
            backgroundView.backgroundColor = appearance.backgroundColor
            backgroundView.layer.cornerRadius = appearance.cornerRadius
            backgroundView.layer.masksToBounds = true

            addSubview(backgroundView)
            backgroundView.translatesAutoresizingMaskIntoConstraints = false
            backgroundView.snp.makeConstraints { make in
                make.edges.equalToSuperview()
            }

            let imageView = UIImageView(image: UIImage(systemName: "doc.on.clipboard"))
            imageView.tintColor = appearance.foregroundColor
            imageView.backgroundColor = appearance.backgroundColor
            imageView.contentMode = .scaleAspectFit
            imageView.isUserInteractionEnabled = true

            let tapGestureRecognizer = UITapGestureRecognizer(
                target: self,
                action: #selector(didTapImageView)
            )
            imageView.addGestureRecognizer(tapGestureRecognizer)

            backgroundView.addSubview(imageView)
            imageView.translatesAutoresizingMaskIntoConstraints = false
            imageView.snp.makeConstraints { make in
                make.center.equalToSuperview()
                make.size.equalTo(appearance.imageViewSize)
            }
        }
    }

    @objc
    private func didTapImageView() {
        guard let pasteConfigurationSupporting else {
            return
        }

        let itemProviders = UIPasteboard.general.itemProviders

        guard pasteConfigurationSupporting.canPaste?(itemProviders) == true else {
            return
        }

        pasteConfigurationSupporting.paste?(itemProviders: itemProviders)
    }
}
