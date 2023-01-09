swiftlint_executable="${PWD}/Pods/SwiftLint/swiftlint"

if [[ -f $swiftlint_executable ]]; then
    $swiftlint_executable lint --config ${PWD}/.swiftlint.yml ${PWD}/iosHyperskillApp/Sources
else
    echo "warning: SwiftLint not installed, run pod install"
fi
