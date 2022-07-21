import SwiftUI

struct HourIntervalPicker: View {
    private let intervals: Range<Int> = 0..<24

    let text: String

    @Binding var selectedInterval: Int

    var body: some View {
        HStack(spacing: 0) {
            Text(text)
                .font(.body)

            Spacer()

            Picker("", selection: $selectedInterval) {
                ForEach(intervals.reversed(), id: \.self) { interval in
                    Text(formatInterval(interval: interval)).tag(interval)
                }
            }
        }
        .foregroundColor(.secondaryText)
    }

    private func formatInterval(interval: Int) -> String {
        "\(interval < 10 ? "0" : "")\(interval):00 - \(interval + 1 < 10 ? "0" : "")\(interval + 1):00"
    }
}

struct HourIntervalPicker_Previews: PreviewProvider {
    static var previews: some View {
        HourIntervalPicker(
            text: Strings.Profile.remindersSchedule,
            selectedInterval: .constant(1)
        )
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
