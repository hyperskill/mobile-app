fastlane documentation
----

# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```sh
xcode-select --install
```

For _fastlane_ installation instructions, see [Installing _fastlane_](https://docs.fastlane.tools/#installing-fastlane)

# Available Actions

## iOS

### ios release

```sh
[bundle exec] fastlane ios release
```

Deploy a new version to the App Store

Options: scheme, target, install_pods

### ios beta

```sh
[bundle exec] fastlane ios beta
```

Submit a new Beta Build to Firebase.

This will also make sure the profile is up to date.

Options: scheme, target, install_pods

### ios build

```sh
[bundle exec] fastlane ios build
```

Builds iOS App without packaging.

Options: scheme, target, install_pods

### ios sentry_upload_shared_framework_dsym

```sh
[bundle exec] fastlane ios sentry_upload_shared_framework_dsym
```

Uploads shared framework dSYM to Sentry

### ios run_unit_tests

```sh
[bundle exec] fastlane ios run_unit_tests
```

Run all unit tests for scheme

Options: install_pods

### ios run_ui_tests

```sh
[bundle exec] fastlane ios run_ui_tests
```

Run all UI tests for scheme

Options: install_pods

### ios sync_device_info

```sh
[bundle exec] fastlane ios sync_device_info
```

Registers new devices to the Apple Dev Portal

### ios match_all

```sh
[bundle exec] fastlane ios match_all
```

Match all certificates for scheme

Options: scheme, read_only

### ios match_dev

```sh
[bundle exec] fastlane ios match_dev
```

Match development certificates for scheme

Options: scheme, read_only

### ios match_adhoc

```sh
[bundle exec] fastlane ios match_adhoc
```

Match adhoc certificates for scheme

Options: scheme, read_only

### ios match_release

```sh
[bundle exec] fastlane ios match_release
```

Match appstore certificates for scheme.

Options: scheme, read_only

### ios increment_build

```sh
[bundle exec] fastlane ios increment_build
```

Increment the build number of project

### ios set_version

```sh
[bundle exec] fastlane ios set_version
```

Set the version of project

Options: version

### ios increment_minor_version

```sh
[bundle exec] fastlane ios increment_minor_version
```

Increment the minor version of project

### ios register_app

```sh
[bundle exec] fastlane ios register_app
```

Creates new iOS app on both the Apple Developer Portal and App Store Connect

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
