Automated image signing for secure boot
======================================================================

.. note::

    See `meta layer documentation <https://github.com/nxp-imx-support/meta-nxp-security-reference-design/tree/wrynose6.18.20-2.0.0/meta-secure-boot>`_ for the information on the supported devices and supported distro for this release.

Secure boot is an industry-standard mechanism to ensure the device only boots trusted OEM software. NXP i.MX devices support Root of Trust (RoT) through the secure boot method using OEM trusted root keys. In general, the secure boot mechanism involves signing and authenticating the bootloader and OS kernel image.

The signing process involves determining which part of the boot image needs to be signed by analyzing the build log or parsing the image structure, and then using the `Code Signing Tool <https://www.nxp.com/webapp/Download?colCode=IMX_CST_TOOL_NEW&appType=license>`_ or `Secure Provisioning SDK <https://github.com/nxp-mcuxpresso/spsdk>`_. This approach is typically manual and error-prone, which can cause signing or authentication failures and ultimately result in boot failures. To eliminate these errors, this meta layer provides an automated method that analyzes the boot image and signs it using CST or SPSDK.

The NXP BSP release contains a ``meta-nxp-security-reference-design/meta-secure-boot`` Yocto meta layer, which supports i.MX boot image signing automation. The usage of this meta layer is described in the following sections.

.. toctree::
   :maxdepth: 2
   :caption: Contents:

   signer.rst
   pre_req.rst
   yocto_setup.rst
   generate_image.rst
   boot_signed_image.rst
