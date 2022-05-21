import SwiftUI

struct StepQuizProgressView: View {
    @State private var progress = 0.0

    var body: some View {
        ProgressView(value: progress, total: 100)
            .accentColor(Color(ColorPalette.primary))
            .scaleEffect(x: 1, y: 3, anchor: .center)
            .onAppear {
                runCounter(counter: $progress, start: 0, end: 100, speed: 0.05)
            }
    }

    private func runCounter(counter: Binding<Double>, start: Double, end: Double, speed: Double) {
        counter.wrappedValue = start

        Timer.scheduledTimer(withTimeInterval: speed, repeats: true) { timer in
            counter.wrappedValue += 1.0
            if counter.wrappedValue == end {
                timer.invalidate()
            }
        }
    }
}

struct StepQuizProgressView_Previews: PreviewProvider {
    static var previews: some View {
        StepQuizProgressView()
            .previewLayout(.sizeThatFits)
            .padding()
    }
}
