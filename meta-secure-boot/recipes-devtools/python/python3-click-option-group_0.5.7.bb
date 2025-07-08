SUMMARY = "Click-extension package that adds option groups missing in Click."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0b41de2509ab6cfdc7b957c8e82956d0"

inherit pypi python_setuptools_build_meta

PYPI_PACKAGE = "click_option_group"

DEPENDS += "python3-hatchling-native python3-hatch-vcs-native"

SRC_URI[sha256sum] = "8dc780be038712fc12c9fecb3db4fe49e0d0723f9c171d7cda85c20369be693c"

BBCLASSEXTEND = "native"
