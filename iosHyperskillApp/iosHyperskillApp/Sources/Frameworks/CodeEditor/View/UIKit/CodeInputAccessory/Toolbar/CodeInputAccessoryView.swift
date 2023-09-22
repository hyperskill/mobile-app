import SnapKit
import UIKit

extension CodeInputAccessoryView {
    struct Appearance {
        let hideKeyboardImageViewTintColor = UIColor.tertiaryText
        let hideKeyboardImageViewInsets = LayoutInsets(top: 4, leading: 4, bottom: 4, trailing: 8)

        let pasteButtonInsets = LayoutInsets(top: 4, leading: 4, bottom: 4, trailing: 8)

        let collectionViewInsets = LayoutInsets(top: 4, leading: 8, bottom: 4, trailing: 0)
        let collectionViewLayoutSectionInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 4)
        let collectionViewLayoutMinimumInteritemSpacing: CGFloat = 4

        let backgroundColor = UIColor.systemGroupedBackground
    }
}

final class CodeInputAccessoryView: UIView {
    let appearance: Appearance

    private let buttons: [CodeInputAccessoryButtonData]

    private let size: CodeInputAccessorySize

    private let hideKeyboardAction: (() -> Void)?

    private weak var pasteConfigurationSupporting: UIPasteConfigurationSupporting?

    private lazy var hideKeyboardImageView: UIImageView = {
        let imageView = UIImageView(image: UIImage(systemName: "keyboard.chevron.compact.down"))
        imageView.tintColor = appearance.hideKeyboardImageViewTintColor
        imageView.contentMode = .scaleAspectFit
        imageView.isUserInteractionEnabled = true
        let tapGestureRecognizer = UITapGestureRecognizer(
            target: self,
            action: #selector(self.didTapHideKeyboardImageView(recognizer:))
        )
        imageView.addGestureRecognizer(tapGestureRecognizer)
        return imageView
    }()

    private lazy var pasteControlView = CodeInputPasteControl(
        pasteConfigurationSupporting: pasteConfigurationSupporting
    )

    private lazy var collectionView: UICollectionView = {
        let collectionViewLayout = UICollectionViewFlowLayout()
        collectionViewLayout.scrollDirection = .horizontal
        collectionViewLayout.sectionInset = self.appearance.collectionViewLayoutSectionInset
        collectionViewLayout.minimumInteritemSpacing = self.appearance.collectionViewLayoutMinimumInteritemSpacing

        let collectionView = UICollectionView(frame: .zero, collectionViewLayout: collectionViewLayout)
        collectionView.isPagingEnabled = false
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.showsVerticalScrollIndicator = false
        collectionView.contentInset = .zero
        collectionView.backgroundColor = .clear
        collectionView.decelerationRate = .fast

        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.register(cellClass: CodeInputAccessoryCollectionViewCell.self)

        return collectionView
    }()

    init(
        frame: CGRect = .zero,
        appearance: Appearance = Appearance(),
        buttons: [CodeInputAccessoryButtonData] = [],
        size: CodeInputAccessorySize = .small,
        hideKeyboardAction: @escaping () -> Void,
        pasteConfigurationSupporting: UIPasteConfigurationSupporting
    ) {
        self.appearance = appearance
        self.buttons = buttons
        self.size = size
        self.hideKeyboardAction = hideKeyboardAction
        self.pasteConfigurationSupporting = pasteConfigurationSupporting
        super.init(frame: frame)

        setupView()
        addSubviews()
        makeConstraints()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    @objc
    private func didTapHideKeyboardImageView(recognizer: UIGestureRecognizer) {
        hideKeyboardAction?()
    }
}

// MARK: - CodeInputAccessoryView: ProgrammaticallyInitializableViewProtocol -

extension CodeInputAccessoryView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        backgroundColor = appearance.backgroundColor
    }

    func addSubviews() {
        addSubview(hideKeyboardImageView)
        addSubview(pasteControlView)
        addSubview(collectionView)
    }

    func makeConstraints() {
        snp.makeConstraints { $0.height.equalTo(size.realSizes.viewHeight) }

        hideKeyboardImageView.translatesAutoresizingMaskIntoConstraints = false
        hideKeyboardImageView.snp.makeConstraints { make in
            make.top.leading.bottom.equalToSuperview().inset(appearance.hideKeyboardImageViewInsets.uiEdgeInsets)
            make.width.equalTo(hideKeyboardImageView.snp.height)
        }

        pasteControlView.translatesAutoresizingMaskIntoConstraints = false
        pasteControlView.snp.makeConstraints { make in
            make.top.bottom.equalToSuperview().inset(appearance.pasteButtonInsets.uiEdgeInsets)
            make.leading.equalTo(hideKeyboardImageView.snp.trailing).offset(appearance.pasteButtonInsets.leading)
            make.width.equalTo(pasteControlView.snp.height)
        }

        collectionView.translatesAutoresizingMaskIntoConstraints = false
        collectionView.snp.makeConstraints { make in
            make.top.bottom.trailing.equalToSuperview().inset(appearance.collectionViewInsets.uiEdgeInsets)
            make.leading.equalTo(pasteControlView.snp.trailing).offset(appearance.collectionViewInsets.leading)
        }
    }
}

// MARK: - CodeInputAccessoryView: UICollectionViewDelegateFlowLayout -

extension CodeInputAccessoryView: UICollectionViewDelegateFlowLayout {
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        sizeForItemAt indexPath: IndexPath
    ) -> CGSize {
        let width = CodeInputAccessoryCollectionViewCell.calculateWidth(for: buttons[indexPath.item].title, size: size)
        return CGSize(width: width, height: collectionView.bounds.height)
    }

    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        buttons[indexPath.item].action()
    }
}

// MARK: - CodeInputAccessoryView: UICollectionViewDataSource -

extension CodeInputAccessoryView: UICollectionViewDataSource {
    func collectionView(
        _ collectionView: UICollectionView,
        numberOfItemsInSection section: Int
    ) -> Int {
        buttons.count
    }

    func collectionView(
        _ collectionView: UICollectionView,
        cellForItemAt indexPath: IndexPath
    ) -> UICollectionViewCell {
        let cell: CodeInputAccessoryCollectionViewCell = collectionView.dequeueReusableCell(for: indexPath)
        cell.configure(text: buttons[indexPath.item].title, size: size)
        return cell
    }
}
