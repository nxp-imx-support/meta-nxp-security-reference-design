DESCRIPTION = "Secure Boot image: imx-image-multimedia"
LICENSE = "MIT"

require recipes-fsl/images/imx-image-multimedia.bb

inherit secure-boot-image

export IMAGE_BASENAME = "imx-image-multimedia-secure-boot"
