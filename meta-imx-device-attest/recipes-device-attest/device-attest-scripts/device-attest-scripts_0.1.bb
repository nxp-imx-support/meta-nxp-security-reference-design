SUMMARY = "Device Attestation Scripts"
DESCRIPTION = "REST client and server scripts to perform device attestation on i.MX devices"
SECTION = "security"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

# Runtime dependencies
RDEPENDS:${PN} = " \
    smw \
    imx-smw-apps \
    python3-core \
"
# When available add following as Runtime dependencies:
#    python3-uvicorn
#    python3-fastapi
# If running rest_server on another target:
#    python3-requests
#    python3-cryptography
#    python3-pydantic
#    python3-json

# Source files
SRC_URI = " \
    file://rest_client.py;subdir=${BP} \
    file://rest_server.py;subdir=${BP} \
"

# No compilation needed
do_compile[noexec] = "1"

do_install() {
    # Create device_attestation directory in home directory
    # Note: Using /root as default home directory for embedded systems
    install -d ${D}/root/device_attestation
    
    # Install Python scripts
    install -m 0755 ${S}/rest_client.py ${D}/root/device_attestation/
    install -m 0755 ${S}/rest_server.py ${D}/root/device_attestation/
}

FILES:${PN} = " \
    /root/device_attestation/* \
"

# Ensure the package creates the home directory structure
ALLOW_EMPTY:${PN} = "1"

COMPATIBLE_MACHINE = "(mx9-nxp-bsp)"
