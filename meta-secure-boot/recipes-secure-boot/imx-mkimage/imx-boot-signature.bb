LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit sigtool xhab deploy features_check

REQUIRED_MACHINE_FEATURES = "imx-boot-signature"

DEPENDS += "nxp-imx-signer-native imx-boot"

# For signing the imx-boot image after it has been deployed to DEPLOY_DIR_IMAGE
do_compile[depends] += "imx-boot:do_deploy"

BOOT_IMAGE_SD = "imx-boot-${MACHINE}-sd.bin-${SIGNED_TARGET}"
BOOT_TOOLS = "imx-boot-tools"
BOOT_NAME = "imx-boot"

SIG_CFGFILE = "sign.cfg"

# Signs the imx-boot image. This command assumes that the PKI tree was generated.
do_sign_boot_image() {
    bbnote "Signing boot image"

    SIGNDIR="${S}"
}

do_sign_boot_image:append:ahab() {

    # Creating a cfg file for imx_signer
    if [ -e "${SIG_TOOL_PATH}/spsdk" ]; then
        if [ -e "${SIG_DATA_PATH}/spsdk_ahab.yaml" ]; then
            # Use user defined keys
            install -m 0755 ${SIG_DATA_PATH}/spsdk_ahab.yaml ${SIGNDIR}/${SIG_CFGFILE}
        else
            # Use default keys
            install -m 0755 ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/spsdk_ahab.yaml.sample ${SIGNDIR}/${SIG_CFGFILE}
        fi
    fi

    bbnote "Setting SPSDK family to: ${SPSDK_FAMILY}, in ${SIG_CFGFILE} file"
    sed -i "s/^family:.*/family: ${SPSDK_FAMILY}/" ${SIGNDIR}/${SIG_CFGFILE}
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
        bbfatal 'imx-boot SD image is not available to sign'
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
        ln -sf ${DEPLOY_DIR_IMAGE}/signed-${BOOT_IMAGE_SD} ${DEPLOY_DIR_IMAGE}/${BOOT_NAME}
        # As per https://github.com/Freescale/meta-freescale/commit/161f1b3e69a3cf011a50e9b742fb8c46d61e41e8, create a tagged file.
        cp ${DEPLOY_DIR_IMAGE}/${BOOT_NAME} ${DEPLOY_DIR_IMAGE}/${BOOT_NAME}.tagged
        stat -L -cUUUBURNXXOEUZX7+A-XY5601QQWWZ%sEND \
                ${DEPLOY_DIR_IMAGE}/${BOOT_NAME}.tagged \
                >> ${DEPLOY_DIR_IMAGE}/${BOOT_NAME}.tagged
    else
        bbfatal "ERROR: Could not deploy Signed image"
    fi
}

addtask do_deploy after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(mx8-generic-bsp|mx9-generic-bsp)"

EXCLUDE_FROM_WORLD = "1"
