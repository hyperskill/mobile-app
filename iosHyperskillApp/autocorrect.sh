swiftlint_executable="${PWD}/Pods/SwiftLint/swiftlint"

if [[ -f $swiftlint_executable ]]; then
    $swiftlint_executable --fix --config ${PWD}/.swiftlint.yml --path ${PWD}/iosHyperskillApp/Sources
else
    echo "warning: SwiftLint not installed, run pod install"
fi
