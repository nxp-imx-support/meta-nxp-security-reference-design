======================
Build & Deploy Process
======================

This guide covers how to integrate and build the meta-imx-smw-apps layer in your Yocto Project build environment to compile the imx-smw-apps CLI application and deploy it to the target.

Prerequisites
=============

Before building, ensure you have:

Required Layers
---------------

1. **meta-imx-smw**: Security Middleware layer (provides SMW library)

Layer Integration
=================

Step 1: Clone the Repository
-----------------------------

Clone the meta-nxp-security-reference-design repository containing the meta-imx-smw-apps layer:

.. code-block:: bash

   cd /path/to/yocto/sources
   git clone https://github.com/nxp-imx-support/meta-nxp-security-reference-design.git
   cd meta-nxp-security-reference-design

The meta-imx-smw-apps layer is located at:

.. code-block:: text

   meta-nxp-security-reference-design/meta-imx-smw-apps/

Step 2: Add Layer to bblayers.conf
-----------------------------------

Add the meta-imx-smw-apps layer to your ``conf/bblayers.conf``:

Using bitbake-layers command:

.. code-block:: bash

   cd /path/to/yocto/build
   bitbake-layers add-layer /path/to/meta-nxp-security-reference-design/meta-imx-smw-apps

.. important::
   The meta-imx-smw layer is, by default, built in imx-image-core and imx-image-full image targets on which this layer (meta-imx-smw-apps) depends on.

Step 3: Verify Layer Configuration
-----------------------------------

Verify the layer is properly added:

.. code-block:: bash

   bitbake-layers show-layers

Expected output should include:

.. code-block:: text

   meta-imx-smw-apps     /path/to/meta-imx-smw-apps

Step 4: Add Package to Image
----------------------------

Add the imx-smw-apps package to your image in ``conf/local.conf``:

.. code-block:: bash

   IMAGE_INSTALL:append = " imx-smw-apps"

Building & Deployment
=====================

Build the Package
-----------------

To build only the imx-smw-apps package:

.. code-block:: bash

   bitbake imx-smw-apps

This will:

1. Fetch source from GitHub repository
2. Configure CMake build
3. Compile the application
4. Package the binary

Build Using devtool
-------------------

Alternatively, you can build using devtool for development:

First, add the layer using bitbake-layers:

.. code-block:: bash

   bitbake-layers add-layer meta-nxp-security-reference-design/meta-imx-smw-apps

Then use devtool to build and modify the package:

.. code-block:: bash

   devtool modify imx-smw-apps
   devtool build imx-smw-apps

This approach is useful for:

* Active development and testing
* Making local modifications
* Iterative debugging
* Creating patches

Deploying to Target
-------------------

After building the package, deploy it to your target device:

Using devtool deploy:

.. code-block:: bash

   devtool deploy-target imx-smw-apps root@<target-ip-address>

Or manually copy the binary to the target:

.. code-block:: bash

   scp -r tmp/work/<machine>/imx-smw-apps/<version>/image/* root@<target-ip-address>:/

To undeploy when using devtool:

.. code-block:: bash

   devtool undeploy-target imx-smw-apps root@<target-ip-address>

Build Complete Image
--------------------

To build a complete image with imx-smw-apps included:

.. code-block:: bash

   bitbake <imx-image-core/imx-image-full>

