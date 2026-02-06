Booting a signed image
======================

The output of the Yocto build contains three signed artifacts:

* Signed Bootloader

* Signed Kernel image

* SDcard (WIC) image (Contains signed bootloader and signed kernel images only)

Either of the signed bootloader or kernel image can be downloaded to the target based on the development scenario. The signed WIC image is provided to deploy the signed bootloader and kernel artifacts to the device.

* Secure boot in OPEN/OEM OPEN lifecycle

It is recommended to download the signed image in the OPEN lifecycle state and verify secure boot before closing the part. For more information on how to program the SRK Fuses, verifying signature and closing the part to enable secure boot, see the secure boot user guide, based on the SoC, at HAB4 UBoot Guide or AHAB UBoot Guides.

* Secure boot in CLOSED/OEM CLOSED lifecycle

When the SoC is in CLOSED lifecycle state, the same pre-provisioned signed image must boot the device in a closed security state ensuring a trusted image is running on the device
