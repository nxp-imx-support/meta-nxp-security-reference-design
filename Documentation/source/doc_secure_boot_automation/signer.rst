NXP IMX Signer
==============

.. note::

    See `NXP IMX Signer Tool <https://www.nxp.com/webapp/Download?colCode=IMX_CST_TOOL_NEW&appType=license>`_ for more documentation.

The NXP IMX Signer Tool created by NXP, analyzes the input NXP BSP image to extract the offset and size of the image(s) that need to be signed and prepares the corresponding configuration file (Command Sequence File for CST or YAML configuration file for SPSDK) to sign. This tool is used in the meta layer to support image signing. However, it can also be used as a standalone tool.

.. note::

    This tool only support HAB devices' signing using CST and AHAB devices' signing using SPSDK.

The NXP IMX Signer tool has the following properties:

* Supports signing both HAB (i.MX 6/7/8M Family) and AHAB (i.MX 8/8x/8ULP/9 Family) devices' images.

* Analyzes input image to sign IVT/FIT/Container format images.

* Extracts offsets and sizes from the input image and constructs CSFs or YAML configuration files.

* Default configuration files are present in the NXP CST Signer repository, filled with the basic information related to keys, certificate and flags to be used, both for HAB and AHAB devices.

.. note::

    The image signing process using SPSDK is straight-forward as most heavy lifting is done by SPSDK. Thus this tool is simply a wrap-around of SPSDK.

