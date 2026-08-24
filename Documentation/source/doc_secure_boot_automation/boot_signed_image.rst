Booting a signed image
======================

The output of the Yocto build contains three signed artifacts:

* Signed Bootloader

* Signed Kernel image

* SDcard (WIC) image (Contains signed bootloader and signed kernel images only)

Either the signed bootloader or the signed kernel image can be flashed to the target, depending on your development scenario. The signed WIC image is provided to deploy both the signed bootloader and kernel artifacts to the device.

* Secure boot in OPEN/OEM OPEN lifecycle

It is recommended to download the signed image in the OPEN lifecycle state and verify secure boot before closing the part. For more information on how to program the SRK fuses, verify the signature, and close the part to enable secure boot, refer to the secure boot user guide for your SoC: the HAB4 U-Boot Guide or the AHAB U-Boot Guides.

* Secure boot in CLOSED/OEM CLOSED lifecycle

When the SoC is in CLOSED lifecycle state, the same pre-provisioned signed image must boot the device in a closed security state, ensuring that only a trusted image runs on the device.
