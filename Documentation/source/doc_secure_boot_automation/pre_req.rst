Prerequisites for preparing a signed image
==========================================

The prerequisites for preparing a signed image are as follows:

1. Download the `Code Signing Tool <https://www.nxp.com/webapp/Download?colCode=IMX_CST_TOOL_NEW&appType=license>`_ or `Secure Provisioning SDK <https://github.com/nxp-mcuxpresso/spsdk>`_ delivered by NXP, which is required for this automation to work.

2. Prepare the keys and certificates using CST or SPSDK.

By default, the NXP IMX Signer Tool uses standard keys of type `ECC P256-SHA256` for i.MX 8/8x/8ULP/9 Family and `RSA 2048-SHA256` for i.MX 6/7/8M Family, to be available in the download location (keys and crts directories) of CST or SPSDK. Follow the CST User Guide available in the CST package or `SPSDK documentation <https://spsdk.readthedocs.io/en/latest/examples/ahab/index.html>`_ to generate the keys, certificates, SRK table/fuses and for more information.

.. note::
    
    (Optional) Create and populate `csf_hab4.cfg`, `csf_hab4_pkcs11.cfg`, or `spsdk_ahab.yaml` with your preferred key type and PKI tree location. The default configuration files are located in the NXP IMX Signer work directory inside the Yocto build.

.. note:: 

    The default keys being used are SRK keys with CA flag set. This means the `flag_ca` is set to `true` in SPSDK YAML configuration file `spsdk_ahab.yaml.sample` (CST automatically parses the CA flag). When choosing to use your own keys, please carefully modify the corresponding configuration files and set the `SIG_DATA_PATH` accordingly.


3. For file-based signing, the private key password must be stored in a file named `key_pass.txt` inside the `keys` folder. This applies to both CST and SPSDK.

HSM-based Signing
-----------------

For Hardware Security Module (HSM) based signing, the signing process uses the PKCS#11 interface to access private keys stored securely in the HSM. The configuration files `csf_hab4_pkcs11.cfg` for HAB4-based devices (i.MX 6/7/8M Family) or `spsdk_ahab.yaml` for AHAB-based devices (i.MX 8/8x/8ULP/9 Family) must be properly configured to enable HSM signing.

When preparing the configuration files for HSM signing:

1. For HAB4 devices, use `csf_hab4_pkcs11.cfg` and specify the PKCS#11 library path, token label, and key identifiers in the HSM.

2. For AHAB devices, use `spsdk_ahab.yaml` and configure the signing provider section to use PKCS#11, including the PKCS#11 module path, token slot, and key labels.

3. Ensure the HSM is properly initialized and the required keys and certificates are provisioned in the HSM before attempting to sign images.
