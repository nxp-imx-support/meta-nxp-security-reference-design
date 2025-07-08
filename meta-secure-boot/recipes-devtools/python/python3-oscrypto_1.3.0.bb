SUMMARY = "A compilation-free, always up-to-date encryption library for Python that works on Windows, OS X, Linux and BSD. "
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b5cda97fbd7959ad47a952651a87051a"

inherit pypi python_setuptools_build_meta

SRC_URI[sha256sum] = "6f5fef59cb5b3708321db7cca56aed8ad7e662853351e7991fcf60ec606d47a4"

BBCLASSEXTEND = "native"
