SUMMARY = "Click plugin to show the command tree of your CLI"
AUTHOR = "John Ripple"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=122e43f068614f96d22b996070de2fd3"

inherit pypi python_setuptools_build_meta

PYPI_PACKAGE = "click_command_tree"
S = "${WORKDIR}/click-command-tree-${PV}"
SRC_URI[sha256sum] = "3e7f5db9f3eccc2eccab40f7979355efe6d5123c958b748dee9c242a38364d6c"

BBCLASSEXTEND = "native"
