import SwiftUI

struct ProfileDailyStudyRemindersView: View {
    @State private var isActivated: Bool

    @State private var selectedHour: Int

    private var onIsActivatedChanged: (Bool) -> Void

    private var onSelectedHourChanged: (Int) -> Void
    private var onSelectedHourTapped: () -> Void

    init(
        isActivated: Bool,
        selectedHour: Int,
        onIsActivatedChanged: @escaping (Bool) -> Void,
        onSelectedHourChanged: @escaping (Int) -> Void,
        onSelectedHourTapped: @escaping () -> Void
    ) {
        self.isActivated = isActivated
        self.selectedHour = selectedHour
        self.onIsActivatedChanged = onIsActivatedChanged
        self.onSelectedHourChanged = onSelectedHourChanged
        self.onSelectedHourTapped = onSelectedHourTapped
    }

    var body: some View {
        VStack {
            Toggle(Strings.Profile.DailyStudyReminders.title, isOn: $isActivated)
                .font(.title3)
                .foregroundColor(.primaryText)
                .onChange(of: isActivated) { value in
                    onIsActivatedChanged(value)
                }

            if isActivated {
                Divider()

                HourIntervalPicker(
                    text: Strings.Profile.DailyStudyReminders.schedule,
                    selectedInterval: $selectedHour
                )
                .onChange(of: selectedHour) { value in
                    onSelectedHourChanged(value)
                }
                .onTapGesture(perform: onSelectedHourTapped)
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(BackgroundView(color: Color(ColorPalette.surface)))
    }
}

struct ProfileDailyStudyRemindersView_Previews: PreviewProvider {
    static var previews: some View {
        ProfileDailyStudyRemindersView(
            isActivated: false,
            selectedHour: 21,
            onIsActivatedChanged: { _ in },
            onSelectedHourChanged: { _ in },
            onSelectedHourTapped: {}
        )
        .previewLayout(.sizeThatFits)
    }
}
