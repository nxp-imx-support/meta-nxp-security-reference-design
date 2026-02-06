meta-imx-device-attest
=========
This layer deploys device attestation scripts to the target. The server script is used for attestation verification and the client script is used for attestation data collection on the device.

Supported boards
----------------

   * NXP i.MX 9 Family

Releases
--------
Releases are tracked against the i.MX Linux software releases. Supported releases are listed below. 

* Walnascar

Quick Start Guide
-----------------
Add the meta-imx-device-attest layer to your build environment
From your build directory, use the following bitbake command:

`bitbake-layers add-layer ../sources/meta-nxp-reference-design/meta-imx-device-attest`

To install device attestation scripts directly in RootFS, add the following line to your local.conf:

`IMAGE_INSTALL:append = " device-attest-scripts"`
