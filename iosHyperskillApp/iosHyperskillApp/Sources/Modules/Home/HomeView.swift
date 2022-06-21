import SwiftUI

struct HomeView: View {
    @State private var stepInput = ""

    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Step navigation")) {
                    VStack(spacing: 0) {
                        TextField(
                            "Enter step id",
                            text: $stepInput
                        )
                        .keyboardType(.numberPad)
                        .disableAutocorrection(true)
                        .frame(minHeight: 44)

                        NavigationLink("Open step") {
                            if let stepID = Int(stepInput) {
                                StepAssembly(stepID: stepID)
                                    .makeModule()
                            } else {
                                EmptyView()
                            }
                        }
                        .frame(minHeight: 44)
                    }
                }
            }
            .navigationTitle(Strings.Home.title)
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}

struct HomeView_Previews: PreviewProvider {
    static var previews: some View {
        HomeView()
    }
}
