Automated image signing for secure boot
======================================================================

.. note::

    See `meta layer documentation <https://github.com/nxp-imx-support/meta-nxp-security-reference-design/tree/walnascar-6.12.49-2.2.0/meta-secure-boot>`_ for the information on the supported devices and supported distro for this release.

Secure boot is an industry standard to ensure that the device boots a trusted OEM software. NXP i.MX devices support Root of Trust (RoT) through the secure boot method using the OEM trusted root keys. In general, the secure boot mechanism involves signing and authentication of Bootloader and OS kernel image.

The signing method involves determining the part of the boot image that needs to be signed by analyzing the build log or parsing the image structure itself, and then using `Code Signing Tool <https://www.nxp.com/webapp/Download?colCode=IMX_CST_TOOL_NEW&appType=license>`_ or `Secure Provisioning SDK <https://github.com/nxp-mcuxpresso/spsdk>`_. This method is usually found to be manual and error prone causing signing/authentication failure, which may eventually lead to boot failures. To eliminate such errors, an automated method is devised, which analyzes the input boot image and signs it using CST/SPSDK.

The NXP BSP release contains a ``meta-nxp-security-reference-design/meta-secure-boot`` Yocto meta layer, which supports i.MX boot image signing automation. The usage of this meta layer is described in the following sections.

.. toctree::
   :maxdepth: 2
   :caption: Contents:

   signer.rst
   pre_req.rst
   yocto_setup.rst
   generate_image.rst
   boot_signed_image.rst
