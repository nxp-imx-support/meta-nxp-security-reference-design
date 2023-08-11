DESCRIPTION = "Secure Boot image: imx-image-core"
LICENSE = "MIT"

require recipes-fsl/images/imx-image-core.bb

inherit secure-boot-image

export IMAGE_BASENAME = "imx-image-core-secure-boot"
