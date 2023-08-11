SUMMARY = "CST Signer"
DESCRIPTION = "Image signing automation tool using CST"

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

inherit deploy

SRC_URI = "${CST_SIGNER};branch=${SRCBRANCH}"
CST_SIGNER ?= "git://github.com/nxp-imx-support/nxp-cst-signer.git;protocol=https"
SRCBRANCH = "master"
SRCREV = "de6d62632e55da6a27a5f223782d88d1ceb7821c"

S = "${WORKDIR}/git"

BOOT_TOOLS = "imx-boot-tools"

do_deploy () {
    install -d ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${S}/cst_signer ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${S}/csf_ahab.cfg.sample ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${S}/csf_hab4.cfg.sample ${DEPLOYDIR}/${BOOT_TOOLS}
}

addtask deploy after do_compile before do_install

COMPATIBLE_MACHINE   = "(imx-generic-bsp)"

BBCLASSEXTEND = "native nativesdk"
