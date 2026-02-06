LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit sigtool xhab deploy features_check uboot-config

REQUIRED_MACHINE_FEATURES = "u-boot-imx-signature"

DEPENDS += "nxp-imx-signer-native u-boot"

BOOT_IMAGE_SD = "${SIGNED_TARGET}-${MACHINE}.imx-sd"
BOOT_TOOLS = "imx-boot-tools"

SIG_CFGFILE = "sign.cfg"

# Signs the imx-boot image. This command assumes that the PKI tree was generated.
do_sign_boot_image() {
    bbnote "Signing boot image"

    SIGNDIR="${S}"
}

do_sign_boot_image:append:hab4() {

    # Creating a cfg file for imx_signer. Preference is for file based signing (csf_hab4.cfg)
    # If PKCS11 is needed to be used by default, create a config file in SIG_DATA_PATH
    if [ -e "${SIG_DATA_PATH}/csf_hab4.cfg" ]; then
        # Use user defined keys
        install -m 0755 ${SIG_DATA_PATH}/csf_hab4.cfg ${SIGNDIR}/${SIG_CFGFILE}
    elif [ -e "${SIG_DATA_PATH}/csf_hab4_pkcs11.cfg" ]; then
        # Use user defined keys
        install -m 0755 ${SIG_DATA_PATH}/csf_hab4_pkcs11.cfg ${SIGNDIR}/${SIG_CFGFILE}
    else
        # Use default keys
        install -m 0755 ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/csf_hab4.cfg.sample ${SIGNDIR}/${SIG_CFGFILE}
    fi
}

do_sign_boot_image:append() {

    # Check if SD image is available
    if [ ! -e "${DEPLOY_DIR_IMAGE}/${BOOT_IMAGE_SD}" ]; then
        bbfatal 'U-Boot SD image not available to sign'
    fi
    # Generate signed image using imx_signer
    SIG_TOOL_PATH=${SIG_TOOL_PATH} SIG_DATA_PATH=${SIG_DATA_PATH} ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/imx_signer -d -i ${DEPLOY_DIR_IMAGE}/${BOOT_IMAGE_SD} -c ${SIGNDIR}/${SIG_CFGFILE}
    if [ ! -e "${S}/signed-${BOOT_IMAGE_SD}" ]; then
        bbfatal 'Image signing failed'
    fi
}

do_compile() {
    do_sign_boot_image
}

do_deploy() {

    # Copy signed image to DEPLOYDIR and link it to boot image
    if [ -e "${S}/signed-${BOOT_IMAGE_SD}" ]; then
        install -m 0644 ${S}/signed-${BOOT_IMAGE_SD} ${DEPLOY_DIR_IMAGE}/
        ln -sf ${DEPLOY_DIR_IMAGE}/signed-${BOOT_IMAGE_SD} ${DEPLOY_DIR_IMAGE}/${SIGNED_TARGET}.imx
        # As per https://github.com/Freescale/meta-freescale/commit/161f1b3e69a3cf011a50e9b742fb8c46d61e41e8, create a tagged file.
        cp ${DEPLOY_DIR_IMAGE}/${SIGNED_TARGET}.imx ${DEPLOY_DIR_IMAGE}/${SIGNED_TARGET}.imx.tagged
        stat -L -cUUUBURNXXOEUZX7+A-XY5601QQWWZ%sEND \
                ${DEPLOY_DIR_IMAGE}/${SIGNED_TARGET}.imx.tagged \
                >> ${DEPLOY_DIR_IMAGE}/${SIGNED_TARGET}.imx.tagged
    else
        bbfatal "Could not deploy Signed image"
    fi
}

addtask do_deploy after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(mx6-generic-bsp|mx7-generic-bsp)"

EXCLUDE_FROM_WORLD = "1"
