import SwiftUI

struct HourIntervalPicker: View {
    private let intervals: Range<Int> = 0..<24

    let text: String

    @Binding var selectedInterval: Int

    var body: some View {
        HStack(spacing: 0) {
            Text(text)
                .font(.body)
                .foregroundColor(.secondaryText)

            Spacer()

            Picker("", selection: $selectedInterval) {
                ForEach(intervals.reversed(), id: \.self) { interval in
                    Text(makeFormattedInterval(interval))
                }
            }
            .accentColor(Color(ColorPalette.primary))
            .pickerStyle(.menu)
        }
    }

    private func makeFormattedInterval(_ interval: Int) -> String {
        "\(interval < 10 ? "0" : "")\(interval):00 - \(interval + 1 < 10 ? "0" : "")\(interval + 1):00"
    }
}

struct HourIntervalPicker_Previews: PreviewProvider {
    static var previews: some View {
        HourIntervalPicker(
            text: Strings.Profile.DailyStudyReminders.schedule,
            selectedInterval: .constant(1)
        )
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
