import SwiftUI

extension View {
    @ViewBuilder
    func onTapWhenDisabled(isDisabled: Bool, action: @escaping () -> Void) -> some View {
        if isDisabled {
            self.overlay(
                Color.clear
                    .contentShape(Rectangle())
                    .onTapGesture(perform: action)
            )
        } else {
            self
        }
    }
}

// #if DEBUG
// @available(iOS 17.0, *)
// #Preview {
//    @Previewable @State var isButtonDisabled = true
//
//    VStack {
//        Button(
//            action: {
//                print("Button tapped!")
//            },
//            label: {
//                Text("Submit")
//                    .padding()
//                    .background(isButtonDisabled ? Color.gray : Color.blue)
//                    .foregroundColor(.white)
//                    .cornerRadius(8)
//            }
//        )
//        .disabled(isButtonDisabled)
//        .onTapWhenDisabled(isDisabled: isButtonDisabled) {
//            print("Button is disabled!")
//        }
//
//        Toggle("Disable View", isOn: $isButtonDisabled)
//            .padding()
//    }
// }
// #endif
