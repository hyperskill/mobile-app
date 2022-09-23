import SnapKit
import UIKit

extension CodeCompletionTableViewCell {
    enum Appearance {
        static let defaultFontSize: CGFloat = 11
    }
}

final class CodeCompletionTableViewCell: UITableViewCell, Reusable {
    private lazy var cellView = CodeCompletionCellView()

    override func updateConstraintsIfNeeded() {
        super.updateConstraintsIfNeeded()

        if cellView.superview == nil {
            setupSubview()
        }
    }

    override func prepareForReuse() {
        super.prepareForReuse()
        cellView.attributedText = nil
    }

    func configure(suggestion: String, prefixLength: Int, size: CodeSuggestionsSize?) {
        let fontSize = size?.realSizes.fontSize ?? Appearance.defaultFontSize

        let boldFont = UIFont(name: "Courier-Bold", size: fontSize) ?? UIFont.boldSystemFont(ofSize: fontSize)
        let regularFont = UIFont(name: "Courier", size: fontSize) ?? UIFont.systemFont(ofSize: fontSize)

        let attributedSuggestion = NSMutableAttributedString(string: suggestion, attributes: [.font: regularFont])
        attributedSuggestion.addAttributes([.font: boldFont], range: NSRange(location: 0, length: prefixLength))

        cellView.attributedText = attributedSuggestion
    }

    private func setupSubview() {
        contentView.addSubview(cellView)
        cellView.translatesAutoresizingMaskIntoConstraints = false
        cellView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
    }
}
