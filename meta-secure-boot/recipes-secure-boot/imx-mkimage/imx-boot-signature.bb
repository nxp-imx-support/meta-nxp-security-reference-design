LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit cst hab deploy features_check

REQUIRED_MACHINE_FEATURES = "imx-boot-signature"

DEPENDS += "cst-signer imx-boot"

# For signing the imx-boot image after it has been deployed to DEPLOY_DIR_IMAGE
do_compile[depends] += "imx-boot:do_deploy"

BOOT_IMAGE_SD = "imx-boot-${MACHINE}-sd.bin-${SIGNED_TARGET}"
BOOT_TOOLS = "imx-boot-tools"
BOOT_NAME = "imx-boot"

# Signs the imx-boot image. This command assumes that the PKI tree was generated.
do_sign_boot_image() {
    bbnote "Signing boot image"

    SIGNDIR="${S}"
}

do_sign_boot_image:append:ahab() {

    # Creating a cfg file for cst_signer
    if [ -e "${CST_PATH}/csf_ahab.cfg" ]; then
        # Use user defined keys
        install -m 0755 ${CST_PATH}/csf_ahab.cfg ${SIGNDIR}/csf.cfg
    else
        # Use default keys
        install -m 0755 ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/csf_ahab.cfg.sample ${SIGNDIR}/csf.cfg
    fi
}

do_sign_boot_image:append:hab4() {

    # Creating a cfg file for cst_signer
    if [ -e "${CST_PATH}/csf_hab4.cfg" ]; then
        # Use user defined keys
        install -m 0755 ${CST_PATH}/csf_hab4.cfg ${SIGNDIR}/csf.cfg
    else
        # Use default keys
        install -m 0755 ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/csf_hab4.cfg.sample ${SIGNDIR}/csf.cfg
    fi
}

do_sign_boot_image:append() {

    # Check if SD image is available
    if [ ! -e "${DEPLOY_DIR_IMAGE}/${BOOT_IMAGE_SD}" ]; then
        bbfatal 'imx-boot SD image is not available to sign'
    fi
    # Generate signed image using cst_signer
    CST_PATH=${CST_PATH} ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/cst_signer -d -i ${DEPLOY_DIR_IMAGE}/${BOOT_IMAGE_SD} -c ${SIGNDIR}/csf.cfg
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
    else
        bbfatal "ERROR: Could not deploy Signed image"
    fi
}

addtask do_deploy after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(mx8-generic-bsp|mx9-generic-bsp)"

EXCLUDE_FROM_WORLD = "1"
