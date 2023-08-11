DEPENDS:append:ahab = " linux-imx"

inherit hab

# Make linux-imx deploy as dependent on the do_compile task of imx-boot
do_compile[depends] += "virtual/kernel:do_deploy"

do_compile:append:ahab() {
    # Copy kernel images to BOOT_STAGING to build kernel container image
    cp ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE} ${BOOT_STAGING}/${KERNEL_IMAGETYPE}
    cp ${DEPLOY_DIR_IMAGE}/*.dtb ${BOOT_STAGING}

    # Backup the flash.bin image which is imx-boot image, as kernel image build 
    # also generates flash.bin filename
    mv ${BOOT_STAGING}/flash.bin ${BOOT_STAGING}/flash.bak

    # Invoke mkimage again to Get container info
    make SOC=${IMX_BOOT_SOC_TARGET} flash_kernel

    # Rename kernel image name and move back the imx-boot flash image name
    mv ${BOOT_STAGING}/flash.bin ${BOOT_STAGING}/flash_os.bin
    mv ${BOOT_STAGING}/flash.bak ${BOOT_STAGING}/flash.bin
}

do_deploy:append:ahab() {
    # Copy both kernel and imx-boot flash image to DEPLOYDIR
    install -m 0755 ${BOOT_STAGING}/flash_os.bin ${DEPLOYDIR}
    install -m 0755 ${BOOT_STAGING}/flash.bin ${DEPLOYDIR}
}

do_deploy:append:hab4() {
    # Copy imx-boot flash image to DEPLOYDIR
    install -m 0755 ${BOOT_STAGING}/flash.bin ${DEPLOYDIR}
}

COMPATIBLE_MACHINE = "(mx8-generic-bsp|mx9-generic-bsp)"

