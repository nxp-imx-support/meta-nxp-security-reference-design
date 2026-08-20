SUMMARY = "i.MX SMW Applications"
DESCRIPTION = "Security Middleware Applications for i.MX platforms"
HOMEPAGE = "https://github.com/nxp-imx-support/imx-smw-apps.git"
SECTION = "security"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9eea131c31421a5b320b2bb8e103bf61"

DEPENDS = "smw"
RDEPENDS:${PN} = "smw"

SRC_URI = "${SMWAPPS_SRC};branch=${SMWAPPS_SRCBRANCH};name=imx-smw-apps"
SMWAPPS_SRC = "git://github.com/nxp-imx-support/imx-smw-apps.git;protocol=https"
SMWAPPS_SRCBRANCH = "release"
SRCREV = "466eb3d8530178918e35110e6553c6c025ebc11f"

inherit cmake

CFLAGS[unexport] = "1"
CPPFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

# CMake configuration
EXTRA_OECMAKE = " \
    -DCMAKE_BUILD_TYPE=Release \
    -DCROSS_COMPILE=${TARGET_PREFIX} \
    -DNXP_SMW_DIR=${STAGING_DIR_HOST}${libdir}/cmake \
"

# Package compatibility
COMPATIBLE_MACHINE = "(mx8ulp-nxp-bsp|mx9-nxp-bsp)"
