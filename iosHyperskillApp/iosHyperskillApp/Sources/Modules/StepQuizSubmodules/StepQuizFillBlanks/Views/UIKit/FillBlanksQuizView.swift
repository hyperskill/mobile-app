import SnapKit
import UIKit

extension FillBlanksQuizView {
    struct Appearance {
        let horizontalInset = LayoutInsets.defaultInset

        let collectionViewMinHeight: CGFloat = 44
        let collectionViewMinLineSpacing: CGFloat = 4
        let collectionViewMinInteritemSpacing: CGFloat = 4
        let collectionViewSectionInset = LayoutInsets.default.uiEdgeInsets

        let backgroundColor = ColorPalette.background
    }
}

final class FillBlanksQuizView: UIView {
    let appearance: Appearance

    private lazy var titleView = FillBlanksQuizTitleView(
        appearance: .init(backgroundColor: self.appearance.backgroundColor)
    )

    private lazy var collectionView: UICollectionView = {
        let collectionViewLayout = LeftAlignedCollectionViewFlowLayout()
        collectionViewLayout.scrollDirection = .vertical
        collectionViewLayout.minimumLineSpacing = appearance.collectionViewMinLineSpacing
        collectionViewLayout.minimumInteritemSpacing = appearance.collectionViewMinInteritemSpacing
        collectionViewLayout.sectionInset = appearance.collectionViewSectionInset

        let collectionView = UICollectionView(
            frame: .zero,
            collectionViewLayout: collectionViewLayout
        )
        collectionView.backgroundColor = appearance.backgroundColor
        collectionView.isScrollEnabled = false
        collectionView.register(cellClass: FillBlanksInputCollectionViewCell.self)
        collectionView.register(cellClass: FillBlanksTextCollectionViewCell.self)
        collectionView.register(cellClass: FillBlanksSelectCollectionViewCell.self)

        return collectionView
    }()

    private lazy var bottomSeparatorView = UIKitSeparatorView()

    override var intrinsicContentSize: CGSize {
        let titleViewHeight = self.titleView.intrinsicContentSize.height
        let collectionViewHeight = max(
            self.appearance.collectionViewMinHeight,
            self.collectionView.collectionViewLayout.collectionViewContentSize.height
        )

        let height = titleViewHeight + collectionViewHeight

        return CGSize(width: UIView.noIntrinsicMetric, height: height)
    }

    init(
        frame: CGRect = .zero,
        appearance: Appearance = Appearance()
    ) {
        self.appearance = appearance
        super.init(frame: frame)

        self.setupView()
        self.addSubviews()
        self.makeConstraints()
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        self.invalidateIntrinsicContentSize()
    }

    func updateCollectionViewData(delegate: UICollectionViewDelegate, dataSource: UICollectionViewDataSource) {
        self.collectionView.delegate = delegate
        self.collectionView.dataSource = dataSource
        self.collectionView.reloadData()

        DispatchQueue.main.async {
            self.invalidateIntrinsicContentSize()
        }
    }

    func invalidateCollectionViewLayout() {
        DispatchQueue.main.async {
            UIView.performWithoutAnimation {
                self.collectionView.collectionViewLayout.invalidateLayout()
                self.layoutIfNeeded()
                self.invalidateIntrinsicContentSize()
            }
        }
    }
}

// MARK: - FillBlanksQuizView: ProgrammaticallyInitializableViewProtocol -

extension FillBlanksQuizView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        self.backgroundColor = self.appearance.backgroundColor
    }

    func addSubviews() {
        self.addSubview(self.titleView)
        self.addSubview(self.collectionView)
        self.addSubview(self.bottomSeparatorView)
    }

    func makeConstraints() {
        self.titleView.translatesAutoresizingMaskIntoConstraints = false
        self.titleView.snp.makeConstraints { make in
            make.top.equalToSuperview()
            make.leading.equalToSuperview().offset(-self.appearance.horizontalInset)
            make.trailing.equalToSuperview().offset(self.appearance.horizontalInset)
        }

        self.collectionView.translatesAutoresizingMaskIntoConstraints = false
        self.collectionView.snp.makeConstraints { make in
            make.top.equalTo(self.titleView.snp.bottom)
            make.leading.equalToSuperview().offset(-self.appearance.horizontalInset)
            make.bottom.equalToSuperview()
            make.trailing.equalToSuperview().offset(self.appearance.horizontalInset)
        }

        self.bottomSeparatorView.translatesAutoresizingMaskIntoConstraints = false
        self.bottomSeparatorView.snp.makeConstraints { make in
            make.bottom.equalToSuperview()
            make.leading.equalToSuperview().offset(-self.appearance.horizontalInset)
            make.trailing.equalToSuperview().offset(self.appearance.horizontalInset)
        }
    }
}

@available(iOS 17, *)
#Preview {
    FillBlanksQuizView()
}
