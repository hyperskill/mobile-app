import Highlightr
import UIKit

protocol CodeTextViewDelegate: UITextViewDelegate {
    func codeTextViewDidChangeHeight(_ textView: CodeTextView, height: CGFloat)
}

extension CodeTextView {
    struct Appearance {
        var gutterWidth: CGFloat = 24
        var gutterBackgroundColor = UIColor.systemGroupedBackground
        var gutterBorderColor = UIColor.opaqueSeparator
        var gutterBorderWidth: CGFloat = 0.5

        var lineNumberFont = UIFont.monospacedDigitSystemFont(ofSize: 10, weight: .regular)
        var lineNumberTextColor = UIColor.secondaryText
        var lineNumberInsets = LayoutInsets(trailing: 4)
        var lineSpacing: CGFloat = 1.2

        var currentLineNumberTextColor = UIColor.primaryText
        var currentLineColor = UIColor.tertiaryText
        var currentLineWidth: CGFloat = 24

        var colorsUpdateStrategy = ColorsUpdateStrategy.invertThemeBackgroundColor()

        enum ColorsUpdateStrategy {
            case onlyOriginal
            case invertThemeBackgroundColor(alphaComponents: AlphaComponents = .init())

            struct AlphaComponents {
                var gutterBorderColorAlpha: CGFloat = 0.5
                var lineNumberTextColorAlpha: CGFloat = 0.5
                var currentLineColorAlpha: CGFloat = 0.15
                var currentLineNumberTextColorAlpha: CGFloat = 1
            }
        }
    }
}

final class CodeTextView: UITextView {
    private(set) var appearance: Appearance

    private lazy var codeTextViewLayoutManager = layoutManager as? CodeTextViewLayoutManager
    private lazy var codeAttributedString = textStorage as? CodeAttributedString

    // Calculate textview's height
    private var oldText: String = ""
    private var oldSize: CGSize = .zero

    var language: String? {
        didSet {
            guard language != oldValue,
                  let codeAttributedString = codeAttributedString
            else {
                return
            }

            codeAttributedString.language = language
        }
    }

    var shouldHighlightCurrentLine = true {
        didSet {
            self.setNeedsDisplay()
        }
    }

    override var selectedTextRange: UITextRange? {
        didSet {
            if shouldHighlightCurrentLine {
                self.setNeedsDisplay()
            }
        }
    }

    override var selectedRange: NSRange {
        didSet {
            if shouldHighlightCurrentLine {
                self.setNeedsDisplay()
            }
        }
    }

    init(appearance: Appearance = Appearance()) {
        self.appearance = appearance

        let textStorage = CodeAttributedString()
        textStorage.language = language

        let layoutManager = CodeTextViewLayoutManager(
            appearance: .init(
                lineSpacing: self.appearance.lineSpacing,
                gutterWidth: self.appearance.gutterWidth,
                lineNumberFont: self.appearance.lineNumberFont,
                lineNumberTextColor: self.appearance.lineNumberTextColor,
                lineNumberInsets: self.appearance.lineNumberInsets,
                currentLineNumberTextColor: self.appearance.currentLineNumberTextColor,
                currentLineColor: self.appearance.currentLineColor,
                currentLineWidth: self.appearance.currentLineWidth
            )
        )

        let textContainer = NSTextContainer()
        textContainer.widthTracksTextView = true
        // Exclude (move right) the line number gutter from the display area of the text container.
        textContainer.exclusionPaths = [
            UIBezierPath(
                rect: CGRect(
                    origin: .zero,
                    size: CGSize(width: appearance.gutterWidth, height: CGFloat.greatestFiniteMagnitude)
                )
            )
        ]

        layoutManager.addTextContainer(textContainer)
        textStorage.addLayoutManager(layoutManager)

        super.init(frame: .zero, textContainer: textContainer)

        layoutManager.delegate = self
        contentMode = .redraw
    }

    @available(*, unavailable)
    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    override func layoutSubviews() {
        super.layoutSubviews()
        codeTextViewLayoutManager?.appearance.currentLineWidth = bounds.width
        calculateBestFitsSize()
    }

    override func draw(_ rect: CGRect) {
        defer {
            super.draw(rect)
        }

        guard let context = UIGraphicsGetCurrentContext() else {
            return
        }

        let bounds = bounds

        context.setFillColor(appearance.gutterBackgroundColor.cgColor)
        let fillRect = CGRect(
            origin: bounds.origin,
            size: CGSize(width: appearance.gutterWidth, height: bounds.height)
        )
        context.fill(fillRect)

        context.setStrokeColor(appearance.gutterBorderColor.cgColor)
        context.setLineWidth(appearance.gutterBorderWidth)
        let strokeRect = CGRect(
            x: bounds.origin.x + appearance.gutterWidth - appearance.gutterBorderWidth,
            y: bounds.origin.y,
            width: appearance.gutterBorderWidth,
            height: bounds.height
        )
        context.stroke(strokeRect)

        invalidateDisplayOfCurrentLine()
    }

    func updateTheme(name: String, font: UIFont) {
        guard let codeAttributedString = codeAttributedString else {
            return
        }

        codeAttributedString.highlightr.setTheme(to: name)

        if let highlightrTheme = codeAttributedString.highlightr.theme {
            highlightrTheme.setCodeFont(font)
            codeAttributedString.highlightr.theme = highlightrTheme

            updateColors(highlightr: codeAttributedString.highlightr)
        }
    }

    private func invalidateDisplayOfCurrentLine() {
        guard let codeTextViewLayoutManager = codeTextViewLayoutManager else {
            return
        }

        guard shouldHighlightCurrentLine else {
            codeTextViewLayoutManager.shouldHighlightCurrentLine = false
            codeTextViewLayoutManager.selectedRange = nil
            return
        }

        codeTextViewLayoutManager.shouldHighlightCurrentLine = true
        codeTextViewLayoutManager.selectedRange = selectedRange

        let textStorageString = textStorage.string as NSString

        var glyphRange = textStorageString.paragraphRange(for: selectedRange)
        glyphRange = codeTextViewLayoutManager.glyphRange(forCharacterRange: glyphRange, actualCharacterRange: nil)

        codeTextViewLayoutManager.selectedRange = glyphRange
        codeTextViewLayoutManager.invalidateDisplay(forGlyphRange: glyphRange)
    }

    private func updateColors(highlightr: Highlightr) {
        guard let themeBackgroundColor = highlightr.theme.themeBackgroundColor else {
            return
        }

        let invertedThemeBackgroundColor = invertColor(themeBackgroundColor)
        // Change cursor color.
        tintColor = invertedThemeBackgroundColor

        backgroundColor = themeBackgroundColor
        appearance.gutterBackgroundColor = themeBackgroundColor

        guard case .invertThemeBackgroundColor(let alphas) = appearance.colorsUpdateStrategy else {
            return
        }

        appearance.gutterBorderColor = invertedThemeBackgroundColor.withAlphaComponent(alphas.gutterBorderColorAlpha)

        guard let codeTextViewLayoutManager = codeTextViewLayoutManager else {
            return
        }

        codeTextViewLayoutManager.appearance.lineNumberTextColor
            = invertedThemeBackgroundColor.withAlphaComponent(alphas.lineNumberTextColorAlpha)
        codeTextViewLayoutManager.appearance.currentLineColor
            = invertedThemeBackgroundColor.withAlphaComponent(alphas.currentLineColorAlpha)
        codeTextViewLayoutManager.appearance.currentLineNumberTextColor
            = invertedThemeBackgroundColor.withAlphaComponent(alphas.currentLineNumberTextColorAlpha)
    }

    private func invertColor(_ color: UIColor) -> UIColor {
        var r: CGFloat = 0
        var g: CGFloat = 0
        var b: CGFloat = 0

        color.getRed(&r, green: &g, blue: &b, alpha: nil)

        return UIColor(red: 1.0 - r, green: 1.0 - g, blue: 1.0 - b, alpha: 1)
    }

    private func calculateBestFitsSize() {
        guard bounds.size.width > 0,
              let delegate = delegate as? CodeTextViewDelegate else {
            return
        }

        if text == oldText && bounds.size == oldSize {
            return
        }

        oldText = text
        oldSize = bounds.size

        let size = sizeThatFits(CGSize(width: bounds.size.width, height: CGFloat.greatestFiniteMagnitude))
        delegate.codeTextViewDidChangeHeight(self, height: size.height)
    }
}

// MARK: - CodeTextView: NSLayoutManagerDelegate -

extension CodeTextView: NSLayoutManagerDelegate {
    func layoutManager(
        _ layoutManager: NSLayoutManager,
        didCompleteLayoutFor textContainer: NSTextContainer?,
        atEnd layoutFinishedFlag: Bool
    ) {
        if layoutFinishedFlag {
            self.setNeedsDisplay()
        }
    }

    func layoutManager(
        _ layoutManager: NSLayoutManager,
        lineSpacingAfterGlyphAt glyphIndex: Int,
        withProposedLineFragmentRect rect: CGRect
    ) -> CGFloat {
        appearance.lineSpacing
    }
}
