import SnapKit
import UIKit

extension StepQuizFillBlanksSelectOptionsView {
    struct Appearance {
        let collectionViewMinLineSpacing = LayoutInsets.defaultInset
        let collectionViewMinInteritemSpacing = LayoutInsets.defaultInset
        let collectionViewSectionInset = LayoutInsets(top: LayoutInsets.defaultInset).uiEdgeInsets

        let backgroundColor = UIColor.clear
    }
}

final class StepQuizFillBlanksSelectOptionsView: UIView {
    let appearance: Appearance

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
        collectionView.automaticallyAdjustsScrollIndicatorInsets = false
        collectionView.register(cellClass: StepQuizFillBlanksSelectOptionsCollectionViewCell.self)

        return collectionView
    }()

    override var intrinsicContentSize: CGSize {
        let collectionViewContentSize = self.collectionView.collectionViewLayout.collectionViewContentSize
        return CGSize(width: UIView.noIntrinsicMetric, height: collectionViewContentSize.height)
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

extension StepQuizFillBlanksSelectOptionsView: ProgrammaticallyInitializableViewProtocol {
    func setupView() {
        self.backgroundColor = self.appearance.backgroundColor
    }

    func addSubviews() {
        self.addSubview(self.collectionView)
    }

    func makeConstraints() {
        self.collectionView.translatesAutoresizingMaskIntoConstraints = false
        self.collectionView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
    }
}
