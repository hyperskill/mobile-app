swiftlint_executable="${PWD}/Pods/SwiftLint/swiftlint"

if [[ -f $swiftlint_executable ]]; then
    $swiftlint_executable --fix --config ${PWD}/.swiftlint.yml \
        ${PWD}/iosHyperskillApp/Sources \
        ${PWD}/iosHyperskillAppTests \
        ${PWD}/iosHyperskillAppUITests \
        ${PWD}/NotificationServiceExtension
else
    echo "warning: SwiftLint not installed, run pod install"
fi
