SUMMARY = "NXP CST Signer"
DESCRIPTION = "Image signing automation tool using CST"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=1f6f1c0be32491a0c8d2915607a28f36"

inherit deploy

SRC_URI = "${CST_SIGNER};branch=${SRCBRANCH}"
CST_SIGNER ?= "git://github.com/nxp-imx-support/nxp-cst-signer.git;protocol=https"
SRCBRANCH = "master"
SRCREV = "770f09b5ed8f43f301a7e933fb0d9da9e34eaad3"

S = "${WORKDIR}/git"

BOOT_TOOLS = "imx-boot-tools"

do_deploy () {
    install -d ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${S}/src/cst_signer ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${S}/csf_ahab.cfg.sample ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${S}/csf_hab4.cfg.sample ${DEPLOYDIR}/${BOOT_TOOLS}
}

addtask deploy after do_compile before do_install

BBCLASSEXTEND = "native nativesdk"
