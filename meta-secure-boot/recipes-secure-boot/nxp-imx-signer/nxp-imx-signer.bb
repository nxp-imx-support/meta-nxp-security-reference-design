SUMMARY = "NXP IMX Signer"
DESCRIPTION = "Image signing automation tool using CST/SPSDK"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=1f6f1c0be32491a0c8d2915607a28f36"

inherit deploy

SRC_URI = "${IMX_SIGNER};branch=${SRCBRANCH}"
IMX_SIGNER ?= "git://github.com/nxp-imx-support/nxp-imx-signer.git;protocol=https"
SRCBRANCH = "master"
SRCREV = "48f3108627201c9b31c27faa97ced912e9d1e313"

BOOT_TOOLS = "imx-boot-tools"

do_deploy () {
    install -d ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${S}/src/imx_signer ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${S}/csf_hab4.cfg.sample ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${S}/csf_hab4_pkcs11.cfg.sample ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${S}/spsdk_ahab.yaml.sample ${DEPLOYDIR}/${BOOT_TOOLS}
}

addtask deploy after do_compile before do_install

BBCLASSEXTEND = "native nativesdk"
