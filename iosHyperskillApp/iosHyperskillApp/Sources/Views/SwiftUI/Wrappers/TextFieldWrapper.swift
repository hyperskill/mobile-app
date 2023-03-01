import SwiftUI
import UIKit

struct TextFieldWrapper: UIViewRepresentable {
    var placeholder: String?

    @Binding var text: String

    var makeTextField: (() -> UITextField) = { UITextField() }

    var configuration = Configuration.empty

    var firstResponderAction: Binding<FirstResponderAction?>?

    var onTextDidChange: ((String) -> Void)?

    var onReturn: (() -> Void)?

    // MARK: UIViewRepresentable

    static func dismantleUIView(_ uiView: UITextField, coordinator: Coordinator) {
        coordinator.onTextDidChange = nil
        coordinator.onReturn = nil
        coordinator.onDidEndEditing = nil
    }

    func makeUIView(context: Context) -> UITextField {
        let textField = self.makeTextField()

        configuration.configure(textField)
        textField.placeholder = placeholder
        textField.text = text

        textField.delegate = context.coordinator
        textField.addTarget(
            context.coordinator,
            action: #selector(Coordinator.textFieldTextDidChange(_:)),
            for: .editingChanged
        )

        return textField
    }

    func updateUIView(_ textField: UITextField, context: Context) {
        textField.text = text

        handleTextFieldFirstResponderAction(textField)

        context.coordinator.onTextDidChange = { newText in
            self.text = newText
            self.onTextDidChange?(newText)
        }
        context.coordinator.onReturn = {
            self.firstResponderAction?.wrappedValue = nil
            self.onReturn?()
        }
        context.coordinator.onDidEndEditing = {
            self.firstResponderAction?.wrappedValue = nil
        }
    }

    func makeCoordinator() -> Coordinator { Coordinator() }
}

// MARK: - TextFieldWrapper Coordinator -

extension TextFieldWrapper {
    class Coordinator: NSObject, UITextFieldDelegate {
        var onTextDidChange: ((String) -> Void)?

        var onReturn: (() -> Void)?

        var onDidEndEditing: (() -> Void)?

        func textFieldShouldReturn(_ textField: UITextField) -> Bool {
            onReturn?()
            return true
        }

        func textFieldDidEndEditing(_ textField: UITextField) {
            onDidEndEditing?()
        }

        @objc
        fileprivate func textFieldTextDidChange(_ textField: UITextField) {
            onTextDidChange?(textField.text ?? "")
        }
    }
}

// MARK: - TextFieldWrapper (FirstResponderAction) -

extension TextFieldWrapper {
    enum FirstResponderAction {
        case becomeFirstResponder
        case resignFirstResponder
    }

    private func handleTextFieldFirstResponderAction(_ textField: UITextField) {
        guard let firstResponderAction = firstResponderAction?.wrappedValue else {
            return
        }

        DispatchQueue.main.async {
            switch firstResponderAction {
            case .becomeFirstResponder:
                textField.becomeFirstResponder()
            case .resignFirstResponder:
                textField.resignFirstResponder()
            }
        }
    }
}

// MARK: - TextFieldWrapper (Configuration) -

extension TextFieldWrapper {
    struct Configuration {
        var configure: (UITextField) -> Void

        static let empty = Self { _ in }

        static let partOfChain = Self { $0.returnKeyType = .next }

        static let lastOfChainDone = Self { $0.returnKeyType = .done }

        static let lastOfChainGo = Self { $0.returnKeyType = .go }

        static let email = Self {
            $0.keyboardType = .emailAddress
            $0.autocorrectionType = .no
            $0.autocapitalizationType = .none
            $0.spellCheckingType = .no
            $0.clearButtonMode = .whileEditing
        }

        static let password = Self {
            $0.keyboardType = .default
            $0.isSecureTextEntry = true
            $0.spellCheckingType = .no
        }

        static func combined(_ configurations: [Self]) -> Self {
            .init { textField in
                for configuration in configurations {
                    configuration.configure(textField)
                }
            }
        }
    }
}

// MARK: - TextFieldWrapper (Previews) -

struct TextFieldWrapper_Previews: PreviewProvider {
    static var previews: some View {
        TextFieldWrapper(
            placeholder: "Email",
            text: .constant(""),
            configuration: .combined([.email, .partOfChain])
        )
        .frame(height: 44)
        .padding(.horizontal)
    }
}
