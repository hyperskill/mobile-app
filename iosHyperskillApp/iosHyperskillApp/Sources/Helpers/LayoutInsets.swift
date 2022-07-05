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
        guard let value = topInset else {
            fatalError("Top inset is undefined")
        }
        return value
    }

    var leading: CGFloat {
        guard let value = leadingInset else {
            fatalError("Leading inset is undefined")
        }
        return value
    }

    var trailing: CGFloat {
        guard let value = trailingInset else {
            fatalError("Trailing inset is undefined")
        }
        return value
    }

    var bottom: CGFloat {
        guard let value = bottomInset else {
            fatalError("Bottom inset is undefined")
        }
        return value
    }

    var edgeInsets: EdgeInsets {
        EdgeInsets(
            top: topInset ?? 0,
            leading: leadingInset ?? 0,
            bottom: bottomInset ?? 0,
            trailing: trailingInset ?? 0
        )
    }

    var uiEdgeInsets: UIEdgeInsets {
        UIEdgeInsets(
            top: topInset ?? 0,
            left: leadingInset ?? 0,
            bottom: bottomInset ?? 0,
            right: trailingInset ?? 0
        )
    }

    init(top: CGFloat? = nil, leading: CGFloat? = nil, bottom: CGFloat? = nil, trailing: CGFloat? = nil) {
        topInset = top
        leadingInset = leading
        bottomInset = bottom
        trailingInset = trailing
    }

    init(insets: EdgeInsets) {
        topInset = insets.top
        leadingInset = insets.leading
        bottomInset = insets.bottom
        trailingInset = insets.trailing
    }

    init(uiEdgeInsets: UIEdgeInsets) {
        topInset = uiEdgeInsets.top
        leadingInset = uiEdgeInsets.left
        bottomInset = uiEdgeInsets.bottom
        trailingInset = uiEdgeInsets.right
    }

    init(inset: CGFloat) {
        topInset = inset
        leadingInset = inset
        bottomInset = inset
        trailingInset = inset
    }

    init(horizontal: CGFloat) {
        topInset = nil
        leadingInset = horizontal
        bottomInset = nil
        trailingInset = horizontal
    }

    init(vertical: CGFloat) {
        topInset = vertical
        leadingInset = nil
        bottomInset = vertical
        trailingInset = nil
    }
}
