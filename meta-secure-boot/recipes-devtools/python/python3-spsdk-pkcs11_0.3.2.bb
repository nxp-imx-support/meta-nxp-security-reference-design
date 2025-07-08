SUMMARY = "Signature Provider plugin for SPSDK using PKCS#11 interface"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fe2a425fb1f291c670f58b9e3771878e"

inherit pypi python_setuptools_build_meta

PYPI_PACKAGE = "spsdk_pkcs11"

SRC_URI[sha256sum] = "a353e301df694319c7d75451f7ffe99fb9ee8c1db856f129f6ae72ab4518b151"

DEPENDS += "python3-pkcs11-native"

BBCLASSEXTEND = "native"
