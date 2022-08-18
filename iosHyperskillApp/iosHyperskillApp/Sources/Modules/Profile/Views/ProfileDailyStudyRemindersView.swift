import SwiftUI

struct ProfileDailyStudyRemindersView: View {
    @State private var isActivated: Bool

    @State private var selectedHour: Int

    private var setActivated: (Bool) async -> Void

    private var setSelectedHour: (Int) async -> Void

    init(
        isActivated: Bool,
        selectedHour: Int,
        setActivated: @escaping (Bool) async -> Void,
        setSelectedHour: @escaping (Int) async -> Void
    ) {
        self.isActivated = isActivated
        self.selectedHour = selectedHour
        self.setActivated = setActivated
        self.setSelectedHour = setSelectedHour
    }

    var body: some View {
        VStack {
            Toggle(Strings.Profile.DailyStudyReminders.title, isOn: $isActivated)
                .font(.title3)
                .foregroundColor(.primaryText)
                .onChange(of: isActivated) { value in
                    Task {
                        await setActivated(value)
                    }
                }

            if isActivated {
                Divider()

                HourIntervalPicker(
                    text: Strings.Profile.DailyStudyReminders.schedule,
                    selectedInterval: $selectedHour
                )
                .onChange(of: selectedHour) { value in
                    Task {
                        await setSelectedHour(value)
                    }
                }
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
            setActivated: { _ in },
            setSelectedHour: { _ in }
        )
        .previewLayout(.sizeThatFits)
    }
}
