Yocto setup for secure boot build
=================================

.. note::
    
    See the `i.MX Yocto Project User's Guide (UG10164)` for how to set up and build Yocto project.

To set up the Yocto project for secure boot build, perform the following steps:

1. Set up Yocto Build Environment and Configuration.

.. note::

    If the manifest file does not exist for a specific release, set up the Yocto build according to the `i.MX Yocto Project User's Guide (UG10164)` and download the `meta-nxp-security-reference-design` meta layer from https://github.com/nxp-imx-support/meta-nxp-security-reference-design/ in the sources directory. Then proceed with following step 1.

.. code-block::

    repo init -u https://github.com/nxp-imx/imx-manifest -b imx-linux-walnascar -m imx-6.12.49-2.2.0_security-reference-design.xml
    repo sync
    DISTRO=<DISTRO> MACHINE=<MACHINE> source imx-setup-release.sh -b <build directory>

1. Add the meta-secure-boot layer to the Yocto project.

.. code-block::

    bitbake-layers add-layer ../sources/meta-nxp-security-reference-design/meta-secure-boot

3. Add CST or SPSDK in ``SIG_TOOL_PATH`` in ``local.conf``.

.. note::

    The absolute location of `CST` or `SPSDK` is required.

.. code-block::

    echo "SIG_TOOL_PATH = \"<path to cst package/spsdk binary>\"" >> conf/local.conf

4. (Optional) Add the keys and crts directories in local.conf.

.. note::

    The absolute location of the folder containing the keys and crts folders is required. If ``SIG_DATA_PATH`` is not provided, the ``SIG_TOOL_PATH`` env value is used.

.. code-block::

    echo "SIG_DATA_PATH = \"<keys and crts folder>\"" >> conf/local.conf
