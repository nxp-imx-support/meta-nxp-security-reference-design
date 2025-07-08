SUMMARY = "SPSDK MCU-Link. A debugger probe plugin for SPSDK supporting LPC-Link/MCU-Link from NXP."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=97dc1d251c45d9bc564c70031b51f99f"

inherit pypi python_setuptools_build_meta

PYPI_PACKAGE = "spsdk_mcu_link"

SRC_URI[sha256sum] = "f4f23f878d2f51b76f21121f213a356fa6c1f85c93d424b417cca0c7422040a2"

BBCLASSEXTEND = "native"
