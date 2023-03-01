import SnapKit
import UIKit

extension CodeEditorView {
    struct Appearance {
        var textViewAppearance = CodeTextView.Appearance()
    }
}

final class CodeEditorView: UIView {
    let appearance: Appearance

    weak var delegate: CodeEditorViewDelegate?

    private lazy var codeTextView: CodeTextView = {
        let codeTextView = CodeTextView(appearance: self.appearance.textViewAppearance)
        codeTextView.delegate = self
        // Disable features
        codeTextView.autocapitalizationType = .none
        codeTextView.autocorrectionType = .no
        codeTextView.spellCheckingType = .no
        codeTextView.smartDashesType = .no
        codeTextView.smartQuotesType = .no
        codeTextView.smartInsertDeleteType = .no
        codeTextView.automaticallyAdjustsScrollIndicatorInsets = false
        return codeTextView
    }()

    private let codePlaygroundManager = CodePlaygroundManager()
    // Uses by codePlaygroundManager for analysis between current code and old one (suggestions & completions).
    private var oldCode: String?

    private let codeEditorThemeService: CodeEditorThemeServiceProtocol

    private let elementsSize: CodeQuizElementsSize
    private var tabSize = 0

    var code: String? {
        get {
            codeTextView.text
        }
        set {
            codeTextView.text = newValue
            if oldCode == nil {
                oldCode = newValue
            }
        }
    }

    var codeTemplate: String? {
        didSet {
            tabSize = codePlaygroundManager.countTabSize(text: codeTemplate ?? "")
        }
    }

    var language: CodeLanguage? {
        didSet {
            codeTextView.language = language?.highlightr
            setupAccessoryView(isEditable: isEditable)
        }
    }

    var theme: CodeEditorTheme? {
        didSet {
            if let theme = theme {
                codeTextView.updateTheme(name: theme.name, font: theme.font)
            }
        }
    }

    var shouldHighlightCurrentLine = false {
        didSet {
            codeTextView.shouldHighlightCurrentLine = shouldHighlightCurrentLine
        }
    }

    var isEditable = true {
        didSet {
            setupAccessoryView(isEditable: isEditable)
        }
    }

    var textInsets: UIEdgeInsets = .zero {
        didSet {
            codeTextView.textContainerInset = textInsets
        }
    }

    init(
        frame: CGRect = .zero,
        appearance: Appearance = Appearance(),
        codeEditorThemeService: CodeEditorThemeServiceProtocol = CodeEditorThemeService(),
        elementsSize: CodeQuizElementsSize = DeviceInfo.current.isPad ? .big : .small
    ) {
        self.appearance = appearance
        self.codeEditorThemeService = codeEditorThemeService
        self.elementsSize = elementsSize
        super.init(frame: frame)

        setupView()
        addSubviews()
        makeConstraints()

        theme = self.codeEditorThemeService.theme
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    deinit {
        NotificationCenter.default.removeObserver(self)
    }

    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)

        performBlockIfAppearanceChanged(from: previousTraitCollection) {
            theme = codeEditorThemeService.theme
        }
    }

    private func setupAccessoryView(isEditable: Bool) {
        defer {
            codeTextView.reloadInputViews()
        }

        guard let language = language, isEditable else {
            codeTextView.inputAccessoryView = nil
            return
        }

        codeTextView.inputAccessoryView = CodeInputAccessoryBuilder.buildAccessoryView(
            size: elementsSize.elements.toolbar,
            language: language,
            tabAction: { [weak self] in
                guard let strongSelf = self else {
                    return
                }

                strongSelf.codePlaygroundManager.insertAtCurrentPosition(
                    symbols: String(repeating: " ", count: strongSelf.tabSize),
                    textView: strongSelf.codeTextView
                )
            },
            insertStringAction: { [weak self] symbols in
                guard let strongSelf = self else {
                    return
                }

                strongSelf.codePlaygroundManager.insertAtCurrentPosition(
                    symbols: symbols,
                    textView: strongSelf.codeTextView
                )
                strongSelf.analyzeCodeAndComplete()
            },
            hideKeyboardAction: { [weak self] in
                self?.codeTextView.resignFirstResponder()
            },
            pasteConfigurationSupporting: codeTextView
        )
    }

    private func analyzeCodeAndComplete() {
        guard let language = language,
              let viewController = delegate?.codeEditorViewDidRequestSuggestionPresentationController(self)
        else {
            return
        }

        codePlaygroundManager.analyzeAndComplete(
            textView: codeTextView,
            previousText: oldCode ?? "",
            language: language,
            tabSize: tabSize,
            inViewController: viewController,
            suggestionsDelegate: self
        )

        oldCode = code
    }
}

// MARK: - CodeEditorView: ProgrammaticallyInitializableViewProtocol -

extension CodeEditorView: ProgrammaticallyInitializableViewProtocol {
    func addSubviews() {
        addSubview(codeTextView)
    }

    func makeConstraints() {
        codeTextView.translatesAutoresizingMaskIntoConstraints = false
        codeTextView.snp.makeConstraints { make in
            make.edges.equalToSuperview()
        }
    }
}

// MARK: - CodeEditorView: UITextViewDelegate -

extension CodeEditorView: UITextViewDelegate {
    func textViewShouldBeginEditing(_ textView: UITextView) -> Bool {
        delegate?.codeEditorView(self, beginEditing: isEditable)
        return isEditable
    }

    func textViewDidBeginEditing(_ textView: UITextView) {
        delegate?.codeEditorViewDidBeginEditing(self)
    }

    func textViewDidEndEditing(_ textView: UITextView) {
        delegate?.codeEditorViewDidEndEditing(self)
    }

    func textViewDidChange(_ textView: UITextView) {
        if textView.text.isEmpty {
            textView.text = "\n"
            DispatchQueue.main.async {
                textView.selectedRange = NSRange(location: 0, length: 0)
            }
        }

        analyzeCodeAndComplete()
        delegate?.codeEditorViewDidChange(self)
    }

    func textView(
        _ textView: UITextView,
        shouldInteractWith URL: URL,
        in characterRange: NSRange,
        interaction: UITextItemInteraction
    ) -> Bool {
        false
    }

    func textView(
        _ textView: UITextView,
        shouldInteractWith textAttachment: NSTextAttachment,
        in characterRange: NSRange,
        interaction: UITextItemInteraction
    ) -> Bool {
        false
    }

    func textView(_ textView: UITextView, shouldInteractWith URL: URL, in characterRange: NSRange) -> Bool { false }

    func textView(
        _ textView: UITextView,
        shouldInteractWith textAttachment: NSTextAttachment,
        in characterRange: NSRange
    ) -> Bool {
        false
    }
}

// MARK: - CodeEditorView: CodeCompletionDelegate -

extension CodeEditorView: CodeCompletionDelegate {
    var codeCompletionSuggestionsSize: CodeSuggestionsSize { elementsSize.elements.suggestions }

    func didSelectCodeCompletionSuggestion(_ suggestion: String, prefix: String) {
        guard codeTextView.isEditable else {
            return
        }

        codeTextView.becomeFirstResponder()

        let symbols = String(suggestion[suggestion.index(suggestion.startIndex, offsetBy: prefix.count)...])
        codePlaygroundManager.insertAtCurrentPosition(symbols: symbols, textView: codeTextView)

        analyzeCodeAndComplete()
    }
}
