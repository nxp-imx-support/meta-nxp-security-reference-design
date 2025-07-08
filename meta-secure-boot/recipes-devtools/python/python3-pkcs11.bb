SUMMARY = "PKCS#11 (Cryptoki) support for Python"
DESCRIPTION = "A high level, “more Pythonic” interface to the PKCS#11 (Cryptoki) standard to support HSM and Smartcard devices in Python."
HOMEPAGE = "https://pypi.org/project/python-pkcs11/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f68bda54505b4002e6ec86e08125ef79"

inherit setuptools3

S="${WORKDIR}/git"

SRC_URI += "git://git@github.com/pyauth/python-pkcs11.git;branch=master;protocol=ssh"
SRCREV = "f3305010992719db254de83f34cc934d0634f013"

SRC_URI += "file://0001-Correctly-handle-CK_UNAVAILABLE_INFORMATION.patch"

DEPENDS += " \
    python3-setuptools-scm-native \
    python3 \
    python3-cython-native \
"

BBCLASSEXTEND = "native"
