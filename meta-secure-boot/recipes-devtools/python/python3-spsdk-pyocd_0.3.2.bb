SUMMARY = "Allow SPSDK to use PyOCD as an interface for debugger probes."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=fe2a425fb1f291c670f58b9e3771878e"

inherit pypi python_setuptools_build_meta

PYPI_PACKAGE = "spsdk_pyocd"

SRC_URI[sha256sum] = "39c446619d54e6343246fd8e48ac12dee269bd14115ba15b9bfac3c65cf2c4f8"

BBCLASSEXTEND = "native"
