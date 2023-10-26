import UIKit

final class StepQuizFillBlanksSelectOptionsCollectionViewAdapter: NSObject {
    var options: [StepQuizFillBlankOption]
    var selectedIndices = Set<Int>()

    var isUserInteractionEnabled = true

    init(options: [StepQuizFillBlankOption] = [], selectedIndices: Set<Int> = []) {
        self.options = options
        self.selectedIndices = selectedIndices
        super.init()
    }
}

// MARK: - StepQuizFillBlanksSelectOptionsCollectionViewAdapter: UICollectionViewDataSource -

extension StepQuizFillBlanksSelectOptionsCollectionViewAdapter: UICollectionViewDataSource {
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        self.options.count
    }

    func collectionView(
        _ collectionView: UICollectionView,
        cellForItemAt indexPath: IndexPath
    ) -> UICollectionViewCell {
        let option = self.options[indexPath.row]

        let cell: StepQuizFillBlanksSelectOptionsCollectionViewCell = collectionView.dequeueReusableCell(for: indexPath)
        cell.isEnabled = self.isUserInteractionEnabled

        let isSelected = selectedIndices.contains(indexPath.row)
        cell.state = isSelected ? .selected : .default
        cell.text = isSelected ? nil : option.displayText

        return cell
    }
}

// MARK: - StepQuizFillBlanksSelectOptionsCollectionViewAdapter: UICollectionViewDelegateFlowLayout -

extension StepQuizFillBlanksSelectOptionsCollectionViewAdapter: UICollectionViewDelegateFlowLayout {
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

        let option = self.options[indexPath.row]
        let isSelected = selectedIndices.contains(indexPath.row)

        if isSelected {
            return StepQuizFillBlanksSelectOptionsCollectionViewCell.Appearance.defaultSize
        } else {
            return StepQuizFillBlanksSelectOptionsCollectionViewCell.calculatePreferredContentSize(
                text: option.displayText,
                maxWidth: maxWidth
            )
        }
    }

    func collectionView(_ collectionView: UICollectionView, shouldSelectItemAt indexPath: IndexPath) -> Bool {
        if !self.isUserInteractionEnabled {
            return false
        }

        return !selectedIndices.contains(indexPath.row)
    }

    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {}
}
