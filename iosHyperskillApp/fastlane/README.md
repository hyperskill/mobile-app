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

### ios register_app

```sh
[bundle exec] fastlane ios register_app
```

Creates new iOS app on both the Apple Developer Portal and App Store Connect

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

----

This README.md is auto-generated and will be re-generated every time [_fastlane_](https://fastlane.tools) is run.

More information about _fastlane_ can be found on [fastlane.tools](https://fastlane.tools).

The documentation of _fastlane_ can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
