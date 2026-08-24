NXP IMX Signer
==============

.. note::

    See `NXP IMX Signer Tool <https://github.com/nxp-imx-support/nxp-imx-signer>`_ for more documentation.

The NXP IMX Signer Tool analyzes an NXP BSP boot image to extract the offsets and sizes of the components that need to be signed, then prepares the corresponding configuration file (a Command Sequence File for CST, or a YAML configuration file for SPSDK). This tool is used in the meta layer to automate image signing, but it can also be used as a standalone tool.

.. note::

    This tool only supports signing HAB-based devices using CST, and AHAB-based devices using SPSDK.

The NXP IMX Signer tool has the following properties:

* Supports signing both HAB (i.MX 6/7/8M Family)V and AHAB (i.MX 8/8x/8ULP/9 Family) devices' images.

* Analyzes input image to sign IVT/FIT/Container format images.

* Extracts offsets and sizes from the input image and constructs CSFs or YAML configuration files.

* Default configuration files are present in the NXP IMX Signer repository, filled with the basic information related to keys, certificate and flags to be used, both for HAB and AHAB devices.

.. note::

    The image signing process using SPSDK is straightforward because SPSDK handles most of the complexity. This tool acts as a thin wrapper around SPSDK for that reason.

