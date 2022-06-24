import UIKit

protocol CodeCompletionDelegate: AnyObject {
    var suggestionsSize: CodeSuggestionsSize { get }

    func didSelectSuggestion(suggestion: String, prefix: String)
}

final class CodeCompletionTableViewController: UITableViewController {
    private static let defaultSuggestionHeight: CGFloat = 22
    private static let maxSuggestionCount = 4

    weak var delegate: CodeCompletionDelegate?

    private var suggestionRowHeight: CGFloat {
        if let size = delegate?.suggestionsSize {
            return size.realSizes.suggestionHeight
        } else {
            return Self.defaultSuggestionHeight
        }
    }

    var suggestions: [String] = [] {
        didSet {
            tableView.reloadData()
        }
    }

    var prefix: String = "" {
        didSet {
            tableView.reloadData()
        }
    }

    var suggestionsHeight: CGFloat {
        suggestionRowHeight * CGFloat(min(Self.maxSuggestionCount, suggestions.count))
    }

    init(suggestions: [String] = [], prefix: String = "") {
        self.suggestions = suggestions
        self.prefix = prefix
        super.init(style: .plain)
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        tableView.register(cellClass: CodeCompletionTableViewCell.self)

        tableView.separatorStyle = .singleLine
        tableView.separatorInset = .zero

        tableView.allowsSelection = false
        clearsSelectionOnViewWillAppear = false
        tableView.rowHeight = suggestionRowHeight

        //Adding tap gesture recognizer to catch selection to avoid resignFirstResponder call and keyboard disappearance
        let tapGestureRecognizer = UITapGestureRecognizer(
            target: self,
            action: #selector(didTap(recognizer:))
        )
        tableView.addGestureRecognizer(tapGestureRecognizer)
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        suggestions.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: CodeCompletionTableViewCell = tableView.dequeueReusableCell(for: indexPath)
        cell.updateConstraintsIfNeeded()

        cell.setSuggestion(
            suggestions[indexPath.row],
            prefixLength: prefix.count,
            size: delegate?.suggestionsSize
        )

        return cell
    }

    override func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        suggestionRowHeight
    }

    // MARK: Private API

    @objc
    private func didTap(recognizer: UITapGestureRecognizer) {
        let location = recognizer.location(in: tableView)
        let indexPath = tableView.indexPathForRow(at: location)

        guard let row = indexPath?.row else {
            return
        }

        delegate?.didSelectSuggestion(suggestion: suggestions[row], prefix: prefix)
    }
}
