meta-imx-smw-apps
=========
This layer builds the imx-smw-apps application and deploys it on the target.

Supported i.MX Devices
----------------

   * NXP i.MX 8ULP
   * NXP i.MX 9 Family

Releases
--------
Releases are tracked against the i.MX Linux software releases. Supported releases are listed below. 

* Walnascar

Quick Start Guide
-----------------
First add the meta-imx-smw-apps layer to your build environment
From your build directory, use the following bitbake command:

`bitbake-layers add-layer ../sources/meta-nxp-reference-design/meta-imx-smw-apps`

To install this image directly in RootFS, add the following line to your local.conf:
`IMAGE_INSTALL:append = " imx-smw-apps"`
