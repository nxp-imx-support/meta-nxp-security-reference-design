DESCRIPTION = "Secure Boot image: fsl-image-machine-test2"
LICENSE = "MIT"

require recipes-fsl/images/fsl-image-machine-test.bb

inherit secure-boot-image

export IMAGE_BASENAME = "fsl-image-machine-test-secure-boot"
