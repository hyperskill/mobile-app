import SwiftUI

struct StepQuizTableView: View {
    @State var viewData: StepQuizTableViewData

    var body: some View {
        Text("Hello, World!")
    }
}

#if DEBUG
struct StepQuizTableView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizTableView(viewData: .placeholder)
            .padding()
    }
}
#endif
