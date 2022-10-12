import SwiftUI

struct HourIntervalPicker: View {
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
            VStack(spacing: 0) {
                Picker("", selection: $selectedInterval) {
                    ForEach(intervals, id: \.self) { interval in
                        Text(makeFormattedInterval(interval))
                    }
                }
                .accentColor(Color(ColorPalette.primary))
                .pickerStyle(.wheel)

                Button(Strings.StepQuizTable.confirmButton) {
                    onSelectedIntervalChanged(selectedInterval)
                    showPickerModal = false
                }
                .buttonStyle(RoundedRectangleButtonStyle(style: .violet))
                .padding(.horizontal, LayoutInsets.smallInset)
            }
        }
        .environmentObject(PanModalPresenter(sourcelessRouter: SourcelessRouter()))
    }

    private func makeFormattedInterval(_ interval: Int) -> String {
        "\(interval < 10 ? "0" : "")\(interval):00 - \(interval + 1 < 10 ? "0" : "")\(interval + 1):00"
    }
}

struct HourIntervalPicker_Previews: PreviewProvider {
    static var previews: some View {
        HourIntervalPicker(
            text: Strings.Profile.DailyStudyReminders.schedule,
            selectedInterval: .constant(1),
            onSelectedIntervalChanged: { _ in },
            onSelectedIntervalTapped: {}
        )
        .padding()
        .previewLayout(.sizeThatFits)
    }
}
