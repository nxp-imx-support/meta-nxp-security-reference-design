SUMMARY = "SLY is a 100% Python implementation of the lex and yacc tools commonly used to write parsers and compilers. "
DESCRIPTION = "SLY is a modern library for performing lexing and parsing. It implements the LALR(1) parsing algorithm, \
commonly used for parsing and compiling various programming languages. \
This project has been semi-retired since October 11, 2022"
HOMEPAGE = "https://github.com/dabeaz/sly"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=40c72247cf23e6d5397482dae055914a"

inherit python_setuptools_build_meta

S="${WORKDIR}/git"

SRC_URI += "git://github.com/dabeaz/sly.git;branch=master;protocol=https"
SRCREV = "539a85a5d5818bf4e1cb5a9e749d6e2fab70a351"

BBCLASSEXTEND = "native"
