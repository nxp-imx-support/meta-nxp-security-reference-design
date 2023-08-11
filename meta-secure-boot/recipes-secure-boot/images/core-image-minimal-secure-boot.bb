DESCRIPTION = "Secure Boot image: core-image-minimal"
LICENSE = "MIT"

require recipes-core/images/core-image-minimal.bb

inherit secure-boot-image

export IMAGE_BASENAME = "core-image-minimal-secure-boot"
