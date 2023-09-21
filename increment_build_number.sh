#!/bin/bash

############################################################
# Help                                                     #
############################################################
Help() {
    # Display Help
    echo "Increment and commit the build number of Android and iOS projects."
    echo
    echo "Syntax: ./increment_build_number.sh [OPTIONS]"
    echo "options:"
    echo "-b, --both    DEFAULT: Increment the build number of both Android and iOS projects."
    echo "-a, --android Increment the build number of the Android project."
    echo "-i, --ios     Increment the build number of the iOS project."
    echo "-h, --help    Print this Help."
    echo
}

############################################################
############################################################
# Main program                                             #
############################################################
############################################################

IncrementAndroidBuildNumber() {
    # Read the current version code from the TOML file
    version_code=$(grep -E "^versionCode\s*=\s*'([0-9]+)'" gradle/app.versions.toml | cut -d\' -f2 | tr -d '[:space:]')
    echo "Current Android build number is: $version_code"

    # Increment the version code
    new_version_code=$((version_code + 1))
    echo "New Android build number will be: $new_version_code"

    # Update the TOML file with the new version code
    new_file_content=$(sed "s/versionCode.*/versionCode = '$new_version_code'/" gradle/app.versions.toml)
    echo -n "$new_file_content" >gradle/app.versions.toml

    echo "Android build number incremented from $version_code to $new_version_code"
}

IncrementIOSBuildNumber() {
    echo "Incrementing iOS build number..."
    (cd iosHyperskillApp && bundle exec fastlane increment_build)
}

CommitAndroidBuildNumber() {
    git add gradle/app.versions.toml
    git commit -m "Android: Bump build number"
}

CommitIOSBuildNumber() {
    git add iosHyperskillApp/iosHyperskillApp/Info.plist
    git add iosHyperskillApp/iosHyperskillAppTests/Info.plist
    git add iosHyperskillApp/NotificationServiceExtension/Info.plist
    git add iosHyperskillApp/iosHyperskillApp.xcodeproj/project.pbxproj
    git commit -m "iOS: Bump build number"
}

CommitAndroidAndIOSBuildNumber() {
    git add gradle/app.versions.toml
    git add iosHyperskillApp/iosHyperskillApp/Info.plist
    git add iosHyperskillApp/iosHyperskillAppTests/Info.plist
    git add iosHyperskillApp/NotificationServiceExtension/Info.plist
    git add iosHyperskillApp/iosHyperskillApp.xcodeproj/project.pbxproj
    git commit -m "Bump build number"
}

# Set the default option
option="both"

# Parse the command line arguments
while [[ $# -gt 0 ]]; do
    key="$1"

    case $key in
    -h | --help) # Display help
        Help
        exit
        ;;
    -b | --both) # Increment both Android and iOS build numbers
        option="both"
        shift # past argument
        ;;
    -a | --android) # Increment Android build number
        option="android"
        shift # past argument
        ;;
    -i | --ios) # Increment iOS build number
        option="ios"
        shift # past argument
        ;;
    *) # Invalid option
        echo "Invalid option: -$OPTARG" >&2
        Help
        exit 1
        ;;
    esac
done

# Increment and commit the build number based on the selected option
case $option in
"both")
    IncrementAndroidBuildNumber
    IncrementIOSBuildNumber
    CommitAndroidAndIOSBuildNumber
    ;;
"android")
    IncrementAndroidBuildNumber
    CommitAndroidBuildNumber
    ;;
"ios")
    IncrementIOSBuildNumber
    CommitIOSBuildNumber
    ;;
esac
