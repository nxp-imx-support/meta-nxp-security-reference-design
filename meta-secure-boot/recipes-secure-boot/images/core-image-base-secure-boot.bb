DESCRIPTION = "Secure Boot image: core-image-base"
LICENSE = "MIT"

require recipes-core/images/core-image-base.bb

inherit secure-boot-image

export IMAGE_BASENAME = "core-image-base-secure-boot"
