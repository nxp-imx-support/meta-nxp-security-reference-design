SUMMARY = "A python wrapper for libuuu."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e6e72f45f3b08c41bf1aff1f5d610b97"

inherit pypi python_setuptools_build_meta

DEPENDS += "python3-setuptools-scm-native"

SRC_URI[sha256sum] = "6f00050299dfbb4a75a87656712ac99973d94e4a70f471616c6ec19c049b7812"

BBCLASSEXTEND = "native"
