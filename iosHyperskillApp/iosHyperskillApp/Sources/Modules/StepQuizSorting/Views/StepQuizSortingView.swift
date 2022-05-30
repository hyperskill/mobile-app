import SwiftUI

struct StepQuizSortingView: View {
    @State var viewData: StepQuizSortingViewData

    var body: some View {
        VStack(alignment: .leading, spacing: 20) {
            ForEach(viewData.items.indices, id: \.self) { i in
                StepQuizSortingItemView(
                    text: viewData.items[i].text,
                    position: i,
                    maxPosition: viewData.items.capacity - 1,
                    moveUp: moveUp,
                    moveDown: moveDown
                )
            }
        }
    }

    private func moveUp(position: Int) {
        if position == 0 {
            return
        }
        let tmp = viewData.items[position - 1]
        viewData.items[position - 1] = viewData.items[position]
        viewData.items[position] = tmp
    }

    private func moveDown(position: Int) {
        if position == viewData.items.capacity - 1 {
            return
        }
        let tmp = viewData.items[position + 1]
        viewData.items[position + 1] = viewData.items[position]
        viewData.items[position] = tmp
    }
}

#if DEBUG
struct StepQuizSortingView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizSortingView(viewData: StepQuizSortingViewData.makePlaceholder())
    }
}
#endif
