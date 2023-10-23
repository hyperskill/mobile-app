import UIKit

protocol FillBlanksQuizCollectionViewAdapterDelegate: AnyObject {
    func fillBlanksQuizCollectionViewAdapter(
        _ adapter: FillBlanksQuizCollectionViewAdapter,
        inputDidChange inputText: String,
        forComponent component: StepQuizFillBlankComponent
    )

    func fillBlanksQuizCollectionViewAdapter(
        _ adapter: FillBlanksQuizCollectionViewAdapter,
        didSelectComponentAt indexPath: IndexPath
    )
    func fillBlanksQuizCollectionViewAdapter(
        _ adapter: FillBlanksQuizCollectionViewAdapter,
        didDeselectComponentAt indexPath: IndexPath
    )
}

final class FillBlanksQuizCollectionViewAdapter: NSObject {
    weak var delegate: FillBlanksQuizCollectionViewAdapterDelegate?

    var components: [StepQuizFillBlankComponent]
    var isUserInteractionEnabled = true

    init(components: [StepQuizFillBlankComponent] = []) {
        self.components = components
        super.init()
    }
}

// MARK: - FillBlanksQuizCollectionViewAdapter: UICollectionViewDataSource -

extension FillBlanksQuizCollectionViewAdapter: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        self.components.count
    }

    func collectionView(
        _ collectionView: UICollectionView,
        cellForItemAt indexPath: IndexPath
    ) -> UICollectionViewCell {
        let component = self.components[indexPath.row]

        switch component.type {
        case .text:
            let cell: FillBlanksTextCollectionViewCell = collectionView.dequeueReusableCell(for: indexPath)
            cell.attributedText = component.attributedText
            return cell
        case .input:
            let cell: FillBlanksInputCollectionViewCell = collectionView.dequeueReusableCell(for: indexPath)
            cell.text = component.inputText
            cell.isEnabled = self.isUserInteractionEnabled
            cell.state = component.isFirstResponder ? .firstResponder : .default
            cell.onInputChanged = { [weak self] text in
                guard let strongSelf = self else {
                    return
                }

                strongSelf.delegate?.fillBlanksQuizCollectionViewAdapter(
                    strongSelf,
                    inputDidChange: text,
                    forComponent: component
                )
            }
            cell.onBecameFirstResponder = { [weak self, weak cell] in
                guard let strongSelf = self,
                      let strongCell = cell else {
                    return
                }

                strongCell.state = .firstResponder
                strongSelf.components[indexPath.row].isFirstResponder = true

                strongSelf.delegate?.fillBlanksQuizCollectionViewAdapter(
                    strongSelf,
                    didSelectComponentAt: indexPath
                )
            }
            cell.onResignedFirstResponder = { [weak self, weak cell] in
                guard let strongSelf = self,
                      let strongCell = cell else {
                    return
                }

                strongCell.state = .default
                strongSelf.components[indexPath.row].isFirstResponder = false

                strongSelf.delegate?.fillBlanksQuizCollectionViewAdapter(
                    strongSelf,
                    didDeselectComponentAt: indexPath
                )
            }
            return cell
        }
    }
}

// MARK: - FillBlanksQuizCollectionViewAdapter: UICollectionViewDelegateFlowLayout -

extension FillBlanksQuizCollectionViewAdapter: UICollectionViewDelegateFlowLayout {
    func collectionView(
        _ collectionView: UICollectionView,
        layout collectionViewLayout: UICollectionViewLayout,
        sizeForItemAt indexPath: IndexPath
    ) -> CGSize {
        guard let flowLayout = collectionViewLayout as? UICollectionViewFlowLayout else {
            return .zero
        }

        let maxWidth = collectionView.bounds.width
          - flowLayout.sectionInset.left
          - flowLayout.sectionInset.right

        let component = self.components[indexPath.row]

        switch component.type {
        case .text:
            return FillBlanksTextCollectionViewCell.calculatePreferredContentSize(
                attributedText: component.attributedText,
                maxWidth: maxWidth
            )
        case .input:
            return FillBlanksInputCollectionViewCell.calculatePreferredContentSize(
                text: component.inputText ?? "",
                maxWidth: maxWidth
            )
        }
    }

    func collectionView(_ collectionView: UICollectionView, shouldSelectItemAt indexPath: IndexPath) -> Bool {
        if !self.isUserInteractionEnabled {
            return false
        }

        switch self.components[indexPath.row].type {
        case .text:
            return false
        case .input:
            return true
        }
    }

    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if self.components[indexPath.row].type == .input,
           let cell = collectionView.cellForItem(at: indexPath) as? FillBlanksInputCollectionViewCell {
            _ = cell.becomeFirstResponder()
        }

        self.delegate?.fillBlanksQuizCollectionViewAdapter(self, didSelectComponentAt: indexPath)
    }
}
