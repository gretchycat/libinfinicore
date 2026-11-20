# libinfinicore

Resource contracts and Android support extracted from libinfinistore. This library
contains provider-neutral resource identities, capability and error types,
stream/block adapters, guarded content replacement and recovery, the bounded disk
cache, diagnostics, MIME identification, and Android local/SAF adapters. Generic
transfer planning and coordination work against `StorageProvider` interfaces.
It contains no remote clients, torrent engine, mounted filesystem implementation,
or application UI. Existing `com.infinifolder.*` package names are retained for
source compatibility; these classes belong to this library, not the application.

`AndroidStorageProvider` routes local and granted `content://` references through
Android. `ProviderStreams` translates owned backend handles into libinfinistream
contracts. A caller must close each returned stream/device. Capability results
retain supported, unsupported, and unknown states; permission and I/O failures
are not an invitation to fall back to a less restrictive path.

Applications normally use libinfiniselect for external storage selection.
This support library exists for the shared viewer UI and Infinifolder internals;
libinfiniselect does not depend on it.

## Standalone build

Install JDK 17 or 21, Python 3, Android SDK platform 36 and build tools 35.0.0.
Set `ANDROID_HOME` to the SDK, or set `sdk.dir` in this checkout's untracked
`local.properties`. Dependencies are pinned in `dependencies.lock.json`.

```sh
python3 scripts/fetch-dependencies.py
./gradlew :core-android:assembleDebug :core-android:testDebugUnitTest
```

Only libinfinistream is fetched. No sibling checkout is used. Original source
provenance is gretchycat/libinfinistore (the extraction is recorded in its Git
history). Original code is licensed under MIT; see LICENSE.md. Third-party license
terms continue to apply.

## Separation audit update

Original resource types and package names are retained. Core is the owner of the extracted platform support; no classes are copied back into Store. Exception types used by backend implementations are explicit public contracts.

## Compressed format identity

Registered format validators run before generic compressor classification for compressed files/streams. A verified SVGZ image can therefore retain its image identity while arbitrary gzip remains a compressed container. Core continues to depend only on Stream and its declared platform/compression adapters; image validation remains in Canvas through the capability interface.

ARJ identification requires a complete main header with valid length, main-header
file type and CRC32. The two-byte marker alone is insufficient. Numbered ARJ
volume suffixes overlap other file types and remain filename candidates only;
callers must verify content before automatically browsing a container.
