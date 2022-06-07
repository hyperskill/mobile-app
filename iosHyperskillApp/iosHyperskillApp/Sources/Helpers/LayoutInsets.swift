import SwiftUI

struct LayoutInsets {
    static let smallInset: CGFloat = 8
    static let defaultInset: CGFloat = 16

    static let small = LayoutInsets(inset: Self.smallInset)
    static let `default` = LayoutInsets(inset: Self.defaultInset)

    private let topInset: CGFloat?
    private let leadingInset: CGFloat?
    private let trailingInset: CGFloat?
    private let bottomInset: CGFloat?

    var top: CGFloat {
        guard let value = self.topInset else {
            fatalError("Top inset is undefined")
        }
        return value
    }

    var leading: CGFloat {
        guard let value = self.leadingInset else {
            fatalError("Leading inset is undefined")
        }
        return value
    }

    var trailing: CGFloat {
        guard let value = self.trailingInset else {
            fatalError("Trailing inset is undefined")
        }
        return value
    }

    var bottom: CGFloat {
        guard let value = self.bottomInset else {
            fatalError("Bottom inset is undefined")
        }
        return value
    }

    var edgeInsets: EdgeInsets {
        EdgeInsets(
            top: self.topInset ?? 0,
            leading: self.leadingInset ?? 0,
            bottom: self.bottomInset ?? 0,
            trailing: self.trailingInset ?? 0
        )
    }

    init(top: CGFloat? = nil, left: CGFloat? = nil, bottom: CGFloat? = nil, right: CGFloat? = nil) {
        self.topInset = top
        self.leadingInset = left
        self.trailingInset = right
        self.bottomInset = bottom
    }

    init(insets: EdgeInsets) {
        self.topInset = insets.top
        self.leadingInset = insets.leading
        self.trailingInset = insets.trailing
        self.bottomInset = insets.bottom
    }

    init(uiEdgeInsets: UIEdgeInsets) {
        self.topInset = uiEdgeInsets.top
        self.leadingInset = uiEdgeInsets.left
        self.trailingInset = uiEdgeInsets.right
        self.bottomInset = uiEdgeInsets.bottom
    }

    init(inset: CGFloat) {
        self.topInset = inset
        self.leadingInset = inset
        self.trailingInset = inset
        self.bottomInset = inset
    }

    init(horizontal: CGFloat) {
        self.topInset = nil
        self.leadingInset = horizontal
        self.trailingInset = horizontal
        self.bottomInset = nil
    }

    init(vertical: CGFloat) {
        self.topInset = vertical
        self.leadingInset = nil
        self.trailingInset = nil
        self.bottomInset = vertical
    }
}
