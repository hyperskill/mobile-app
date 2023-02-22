import UIKit

enum CodeInputAccessoryBuilder {
    static func buildAccessoryView(
        size: CodeInputAccessorySize,
        language: CodeLanguage,
        tabAction: @escaping () -> Void,
        insertStringAction: @escaping (String) -> Void,
        hideKeyboardAction: @escaping () -> Void
    ) -> UIView {
        let symbols = CodeInputAccessorySymbols.symbols(for: language)

        var buttons = [
            CodeInputAccessoryButtonData(title: "Tab", action: tabAction),
            CodeInputAccessoryButtonData(title: "📋", action: {
                insertStringAction(UIPasteboard.general.string ?? "")
            })
        ]

        for symbol in symbols {
            let button = CodeInputAccessoryButtonData(title: symbol, action: { insertStringAction(symbol) })
            buttons.append(button)
        }

        let viewSize = CGSize(width: UIScreen.main.bounds.size.width, height: size.realSizes.viewHeight)
        let frame = CGRect(origin: CGPoint.zero, size: viewSize)

        let accessoryView = CodeInputAccessoryView(
            frame: frame,
            buttons: buttons,
            size: size,
            hideKeyboardAction: { hideKeyboardAction() }
        )

        return accessoryView
    }
}
