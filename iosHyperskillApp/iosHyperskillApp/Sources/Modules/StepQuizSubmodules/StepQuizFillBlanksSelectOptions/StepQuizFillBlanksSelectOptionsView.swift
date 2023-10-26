import SnapKit
import UIKit

extension StepQuizFillBlanksSelectOptionsView {
    struct Appearance {
        let horizontalInset = LayoutInsets.defaultInset

        var collectionViewMaxHeight: CGFloat { UIScreen.main.bounds.height / 3 }
        let collectionViewMinHeight: CGFloat = 44
        let collectionViewMinLineSpacing = LayoutInsets.defaultInset
        let collectionViewMinInteritemSpacing = LayoutInsets.defaultInset
        let collectionViewSectionInset = LayoutInsets.default.uiEdgeInsets

        let backgroundColor = UIColor.systemBackground
    }
}

final class StepQuizFillBlanksSelectOptionsView: UIView {
    let appearance: Appearance

    private lazy var collectionView: UICollectionView = {
        let collectionViewLayout = LeftAlignedCollectionViewFlowLayout()
        collectionViewLayout.scrollDirection = .vertical
        collectionViewLayout.minimumLineSpacing = self.appearance.collectionViewMinLineSpacing
        collectionViewLayout.minimumInteritemSpacing = self.appearance.collectionViewMinInteritemSpacing
        collectionViewLayout.sectionInset = self.appearance.collectionViewSectionInset

        let collectionView = UICollectionView(
            frame: .zero,
            collectionViewLayout: collectionViewLayout
        )
        collectionView.backgroundColor = self.appearance.backgroundColor
        collectionView.isScrollEnabled = false
        collectionView.register(cellClass: StepQuizFillBlanksSelectOptionsCollectionViewCell.self)

        return collectionView
    }()

    private lazy var topSeparatorView = UIKitSeparatorView()

    var onNewHeight: ((CGFloat) -> Void)?

    override var intrinsicContentSize: CGSize {
        let collectionViewHeight = max(
            self.appearance.collectionViewMinHeight,
            self.collectionView.collectionViewLayout.collectionViewContentSize.height
        )
        onNewHeight?(collectionViewHeight)
        return CGSize(width: UIView.noIntrinsicMetric, height: collectionViewHeight)
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

extension StepQuizFillBlanksSelectOptionsView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        self.backgroundColor = self.appearance.backgroundColor
    }

    func addSubviews() {
        self.addSubview(self.collectionView)
        self.addSubview(self.topSeparatorView)
    }

    func makeConstraints() {
        self.collectionView.translatesAutoresizingMaskIntoConstraints = false
        self.collectionView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }

        self.topSeparatorView.translatesAutoresizingMaskIntoConstraints = false
        self.topSeparatorView.snp.makeConstraints { make in
            make.top.leading.trailing.equalToSuperview()
        }
    }
}
