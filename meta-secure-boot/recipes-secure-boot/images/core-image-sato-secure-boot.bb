DESCRIPTION = "Secure Boot image: core-image-sato"
LICENSE = "MIT"

require recipes-sato/images/core-image-sato.bb

inherit secure-boot-image

export IMAGE_BASENAME = "core-image-sato-secure-boot"
