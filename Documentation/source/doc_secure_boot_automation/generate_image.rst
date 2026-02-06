Generating a signed bootloader/kernel/WIC image in Yocto project
================================================================

To generate a signed bootloader/kernel/WIC image in Yocto project, perform the following steps:

1. Build a signed WIC image.

.. code-block::

    bitbake core-image-minimal-secure-boot

2. Build a signed imx-boot bootloader (for i.MX 8M/8/8x/8ULP/9x).

.. code-block::

    bitbake imx-boot-signature

3. Build a signed U-Boot bootloader (for i.MX 6/7).

.. code-block::

    bitbake u-boot-signature

4. Build a signed Linux kernel image.

.. code-block::
    
    bitbake linux-imx-signature

