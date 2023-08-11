DESCRIPTION = "Secure Boot image: imx-image-full"
LICENSE = "MIT"

require dynamic-layers/qt6-layer/recipes-fsl/images/imx-image-full.bb

inherit secure-boot-image

export IMAGE_BASENAME = "imx-image-full-secure-boot"
