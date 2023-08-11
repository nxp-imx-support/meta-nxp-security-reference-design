LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit cst hab deploy features_check

REQUIRED_MACHINE_FEATURES = "linux-imx-signature"

DEPENDS += "cst-signer linux-imx u-boot-imx"
DEPENDS:append:ahab = " imx-boot"
DEPENDS:append:mx8m-generic-bsp = " imx-boot"

# All deploy tasks of DEPENDS should be done
do_compile[deptask] = "do_deploy"

BOOT_TOOLS = "imx-boot-tools"

# Signs the kernel image.
do_sign_kernel_image() {
    bbnote "Signing kernel image"

	SIGNDIR="${S}"
}

do_sign_kernel_image:append:ahab() {

    # Creating a cfg file for cst_signer
    if [ -e "${CST_PATH}/csf_ahab.cfg" ]; then
        # Use user defined keys
        install -m 0755 ${CST_PATH}/csf_ahab.cfg ${SIGNDIR}/csf.cfg
    else
        # Use default keys
        install -m 0755 ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/csf_ahab.cfg.sample ${SIGNDIR}/csf.cfg
    fi
}

do_sign_kernel_image:append:ahab() {

    IMAGE="${DEPLOY_DIR_IMAGE}/flash_os.bin"

    # Set Absolute path to Image.bin
    install -m 0755 ${IMAGE} ${SIGNDIR}/flash_os.bin
}

do_sign_kernel_image:append:ahab() {

    # Generate signed kernel and dtb image using cst_signer
    CST_PATH=${CST_PATH} ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/cst_signer -d -i ${DEPLOY_DIR_IMAGE}/flash_os.bin -c ${SIGNDIR}/csf.cfg

    mv ${SIGNDIR}/signed-flash_os.bin ${SIGNDIR}/os_cntr_signed.bin
}

do_sign_kernel_image:append:hab4() {

    # Only kernel image type Image and zImage are supported by i.MX devices
    if [ ${KERNEL_IMAGETYPE} != "zImage" ] && \
       [ ${KERNEL_IMAGETYPE} != "Image" ]; then
        bbfatal "Unknown kernel image format"
    fi

    cp ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${MACHINE}.bin ${S}/${KERNEL_IMAGETYPE}
    IMAGE="${S}/${KERNEL_IMAGETYPE}"

    if [ "${KERNEL_IMAGETYPE}" = "Image" ]; then
        # Get kernel image pad size
        image_pad_size="$(od -An -j 16 -N 4 -i ${IMAGE} | tr -d ' ')"
        # Pad the image
    elif [ "${KERNEL_IMAGETYPE}" = "zImage" ]; then
        # Get kernel image pad size after padding it to next 1K
        image_size="$(stat -L --printf=%s ${IMAGE})"
        image_pad_size="$(expr ${image_size} + $(expr 4096 - $(expr ${image_size} % 4096)))"
    else
        bbfatal "Unknown kernel image format"
    fi

    # Pad the image
    objcopy -I binary -O binary --pad-to "${image_pad_size}" --gap-fill=0x00 ${IMAGE} "${SIGNDIR}/${KERNEL_IMAGETYPE}_pad.bin"

    load_address_hex="$(sed -n 's/CONFIG_SYS_LOAD_ADDR=//p' ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/u-boot-imx.config)"
    load_address="$(printf "%u" ${load_address_hex})"
    ivt_pointer="$(expr ${load_address} + ${image_pad_size})"
    csf_pointer="$(expr ${ivt_pointer} + 32)"

    # Generate Image Vector Table script
    GEN_IVT="$(cat <<-EOF
	import struct
	ivt = open('${SIGNDIR}/ivt.bin', 'wb') or Exception('Unable to open ivt file').throw()
	ivt.write(struct.pack('<L', 0x432000D1)) # Signature
	ivt.write(struct.pack('<L', ${load_address})) # Load Address (*load_address)
	ivt.write(struct.pack('<L', 0x0)) # Reserved
	ivt.write(struct.pack('<L', 0x0)) # DCD pointer
	ivt.write(struct.pack('<L', 0x0)) # Boot Data
	ivt.write(struct.pack('<L', ${ivt_pointer})) # Self Pointer (*ivt)
	ivt.write(struct.pack('<L', ${csf_pointer})) # CSF Pointer (*csf)
	ivt.write(struct.pack('<L', 0x0)) # Reserved
	ivt.close()
	EOF
)"

    # Generate Image Vector Table
    python3 -c "${GEN_IVT}"

    # Attach IVT
    cat ${SIGNDIR}/${KERNEL_IMAGETYPE}_pad.bin ${SIGNDIR}/ivt.bin > ${SIGNDIR}/${KERNEL_IMAGETYPE}_pad_ivt.bin
}

do_sign_kernel_image:append:hab4() {

    # Creating a cfg file for cst_signer
    if [ -e "${CST_PATH}/csf_hab4.cfg" ]; then
        # Use user defined keys
        install -m 0755 ${CST_PATH}/csf_hab4.cfg ${SIGNDIR}/csf.cfg
    else
        # Use default keys
        install -m 0755 ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/csf_hab4.cfg.sample ${SIGNDIR}/csf.cfg
    fi
}

do_sign_kernel_image:append:hab4() {

    # Generate signed image data
    CST_PATH=${CST_PATH} ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/cst_signer -d -i ${SIGNDIR}/${KERNEL_IMAGETYPE}_pad_ivt.bin -c ${SIGNDIR}/csf.cfg
    if [ ! -e "${S}/signed-${KERNEL_IMAGETYPE}_pad_ivt.bin" ]; then
        bbfatal 'Kernel Image signing failed'
    fi
}

do_compile() {
    do_sign_kernel_image
}

do_deploy:append:ahab() {

    if [ -e "${S}/os_cntr_signed.bin" ]; then
        install -m 0644 ${S}/os_cntr_signed.bin ${DEPLOYDIR}
    else
        bbfatal "Could not deploy Signed image"
    fi
}

do_deploy:append:hab4() {

    if [ -e "${S}/signed-${KERNEL_IMAGETYPE}_pad_ivt.bin" ]; then
        install -m 0644 ${S}/signed-${KERNEL_IMAGETYPE}_pad_ivt.bin ${DEPLOY_DIR_IMAGE}/signed-${KERNEL_IMAGETYPE}-${MACHINE}.bin
        ln -sf ${DEPLOY_DIR_IMAGE}/signed-${KERNEL_IMAGETYPE}-${MACHINE}.bin ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}
    else
        bbfatal "Could not deploy Signed image"
    fi
}

do_deploy() {
}

addtask do_deploy after do_compile

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(mx6-generic-bsp|mx7-generic-bsp|mx8-generic-bsp|mx9-generic-bsp)"

EXCLUDE_FROM_WORLD = "1"
