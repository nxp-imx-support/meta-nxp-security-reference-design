SUMMARY = "Python wrapper for NXP libusbsio binary library"
DESCRIPTION = "The NXP libusbsio is a binary library for Win/Linux/MacOS systems used to exercise SPI, I2C bus \
and GPIO pins over USBSIO interface of NXP LPCLink2 and MCUlink Pro devices. \
This Python component provides a wrapper object which encapsulates the binary library and exposes \
its API to Python applications"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${S}/license/BSD-3-clause.txt;md5=1076c1c40acc679330f3d60bfea1c23b"

inherit pypi python_setuptools_build_meta

SRC_URI[sha256sum] = "df1b9d4b2a9f5eadf0b0574e8017862b59d26343598f1f548664ea6d01975b25"

BBCLASSEXTEND = "native"

do_install:append:class-native() {
    # Remove the .so file that is not needed in the target
    mv ${D}${PYTHON_SITEPACKAGES_DIR}/libusbsio/bin/linux_x86_64 ${WORKDIR}/
    rm -rf ${D}${PYTHON_SITEPACKAGES_DIR}/libusbsio/bin/linux_* || true
    mv ${WORKDIR}/linux_x86_64 ${D}${PYTHON_SITEPACKAGES_DIR}/libusbsio/bin/
}
