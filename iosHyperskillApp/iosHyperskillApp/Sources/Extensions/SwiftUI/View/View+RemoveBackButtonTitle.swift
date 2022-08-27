import SwiftUI

extension View {
    func navigationBarBackButtonTitleRemoved(action: @escaping () -> Void) -> some View {
        toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(
                    action: action,
                    label: {
                        Image(systemName: "chevron.backward")
                            .font(.body.weight(.semibold))
                            .imageScale(.large)
                    }
                )
            }
        }
        .navigationBarBackButtonHidden(true)
    }
}
