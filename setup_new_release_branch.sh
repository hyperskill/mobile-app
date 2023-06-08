#!/bin/bash

# Exit the script immediately if any command fails
set -e

# Check that a version number was provided
if [ $# -eq 0 ]; then
    echo "Error: No version number provided"
    echo "Syntax: ./setup_new_release_branch.sh {VERSION_NUMBER}"
    echo "Example: ./setup_new_release_branch.sh 1.0.0"
    exit 1
fi

# Check that the current branch is develop
current_branch=$(git branch --show-current)
if [ "$current_branch" != "develop" ]; then
    echo "Error: Current branch is not develop"
    echo "Must be on develop branch to run this script"
    exit 1
fi

# Check that there are no unstaged changes in git
if [ -n "$(git status --porcelain)" ]; then
    echo "Error: There are unstaged changes in git"
    echo "Please commit or stash them before running this script"
    exit 1
fi

version_number=$1
echo "New version number will be: $version_number"

# Enusre that the version number is valid by semver standards
# patch version is optional
if ! [[ $version_number =~ ^[0-9]+\.[0-9]+(\.[0-9]+)?$ ]]; then
    echo "Error: Invalid version number"
    echo "Must be in the format: MAJOR.MINOR.PATCH"
    exit 1
fi

# Check that there is no existing release branch with this version number remote and locally
echo "Checking for existing release branch with this version number..."

if [ -n "$(git branch --list release/$version_number)" ]; then
    echo "Error: Local release branch already exists with this version number"
    exit 1
fi

if [ -n "$(git ls-remote --heads origin release/$version_number)" ]; then
    echo "Error: Remote release branch already exists with this version number"
    exit 1
fi

# Get the latest changes from develop
echo "Pulling latest changes from develop..."
git pull origin develop

# Create a new release branch
echo "Creating new release branch..."
git checkout -b release/$version_number
echo "Created new release branch: release/$version_number"

# Set the version number of Android project
echo "Setting new version number in Android project..."

android_new_file_content=$(sed "s/versionName.*/versionName = '$version_number'/" gradle/app.versions.toml)
echo -n "$android_new_file_content" >gradle/app.versions.toml

echo "New version number set in Android project: $version_number"

# Set the version number of iOS project
echo "Setting new version number in iOS project..."
(cd iosHyperskillApp && bundle exec fastlane set_version version:$version_number)

# Commit the version number changes
echo "Committing version number changes..."

git add gradle/app.versions.toml
git add iosHyperskillApp/iosHyperskillApp/Info.plist

git commit -m "Set version number to $version_number"
echo "Committed version number changes"

# Increment the build number of both projects
echo "Incrementing build numbers..."
./increment_build_number.sh

# Ensure that no unstaged changes remain
if [ -n "$(git status --porcelain)" ]; then
    echo "Error: There are unstaged changes in git"
    exit 1
fi

# Push the new release branch to remote
echo "Pushing new release branch to remote..."
git push --set-upstream origin release/$version_number

echo "Done"
