inherit hab

EXTRA_IMAGEDEPENDS:append = " linux-imx-signature"
EXTRA_IMAGEDEPENDS:append:ahab = " imx-boot-signature"
EXTRA_IMAGEDEPENDS:append:hab4 = " u-boot-imx-signature"
EXTRA_IMAGEDEPENDS:append:mx8m-generic-bsp = " imx-boot-signature"
EXTRA_IMAGEDEPENDS:remove:mx8m-generic-bsp = "u-boot-imx-signature"

IMAGE_BOOT_FILES:append:ahab = " os_cntr_signed.bin"

COMPATIBLE_MACHINE = "(mx6-generic-bsp|mx7-generic-bsp|mx8-generic-bsp|mx9-generic-bsp)"