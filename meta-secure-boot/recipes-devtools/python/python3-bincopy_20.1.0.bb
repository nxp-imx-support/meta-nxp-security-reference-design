SUMMARY = "bincopy pypi package"
DESCRIPTION = "Mangling of various file formats that conveys binary information (Motorola S-Record, Intel HEX, TI-TXT, Verilog VMEM, ELF and binary files)."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d9aa4ec07de78abae21c490c9ffe61bd"

inherit pypi python_poetry_core

SRC_URI[sha256sum] = "d8a4e8cb82edafbbe367415337d1926c7d8c455617e43bd4b145653772b9b965"

BBCLASSEXTEND = "native"
