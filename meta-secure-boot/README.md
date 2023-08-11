meta-secure-boot
=========
This layer generates signed SD card image needed to perform secure boot in a device.
It allows signing of the bootloader and kernel image component of the boot image.

Supported boards
----------------

   * NXP i.MX 6 Family
   * NXP i.MX 7 Family
   * NXP i.MX 8/8x Family
   * NXP i.MX 8M Family
   * NXP i.MX 8ULP
   * NXP i.MX 9 Family

SD uboot configuration is supported for all devices.

Following mkimage targets are supported in i.MX devices:
| Device | Target image |
|--------|--------------|
| i.MX 8M family| flash_evk |
| i.MX 93 | flash_singleboot |
| i.MX 8ULP | flash_singleboot_m33|
| i.MX 8QM/8QXP | flash_spl |
| i.MX 8DXL | flash |

Releases
--------
Releases are tracked against the i.MX Linux software releases. Supported releases are listed below. 

* Mickledore

Quick Start Guide
-----------------
First add the meta-secure-boot layer to your build environment
From your build directory, use the following bitbake command:

`bitbake-layers add-layer ../sources/meta-nxp-reference-design/meta-secure-boot`

Before building add the following line to the `local.conf` to enable secure boot:

`CST_PATH = "<absolute path to cst package>"`
