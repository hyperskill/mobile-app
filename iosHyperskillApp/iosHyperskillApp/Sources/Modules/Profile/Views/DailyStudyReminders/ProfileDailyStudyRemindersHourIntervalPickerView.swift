import SwiftUI

struct ProfileDailyStudyRemindersHourIntervalPickerView: View {
    private let intervals: Range<Int> = 0..<24

    let text: String

    @Binding var selectedInterval: Int

    let onSelectedIntervalChanged: (Int) -> Void

    let onSelectedIntervalTapped: () -> Void

    @State private var showPickerModal = false

    var body: some View {
        HStack(spacing: 0) {
            Text(text)
                .font(.body)
                .foregroundColor(.secondaryText)

            Spacer()

            Button(makeFormattedInterval(selectedInterval)) {
                onSelectedIntervalTapped()
                showPickerModal = true
            }
            .accentColor(Color(ColorPalette.primary))
        }
        .panModal(isPresented: $showPickerModal) {
            ProfileDailyStudyRemindersPickerViewController(
                rows: intervals.map(makeFormattedInterval(_:)),
                initialRowIndex: selectedInterval,
                onDidConfirmRow: { selectedIntervalIndex in
                    selectedInterval = selectedIntervalIndex
                    onSelectedIntervalChanged(selectedInterval)

                    showPickerModal = false
                }
            )
        }
    }

    private func makeFormattedInterval(_ interval: Int) -> String {
        "\(interval < 10 ? "0" : "")\(interval):00 - \(interval + 1 < 10 ? "0" : "")\(interval + 1):00"
    }
}

struct HourIntervalPicker_Previews: PreviewProvider {
    static var previews: some View {
        ProfileDailyStudyRemindersHourIntervalPickerView(
            text: Strings.Profile.DailyStudyReminders.schedule,
            selectedInterval: .constant(1),
            onSelectedIntervalChanged: { _ in },
            onSelectedIntervalTapped: {}
        )
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
