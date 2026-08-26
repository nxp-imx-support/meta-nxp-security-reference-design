Build & Deploy Process
======================

Layer Overview
--------------

The ``meta-imx-device-attest`` layer provides device attestation capabilities for i.MX platforms.

Prerequisites
-------------

Required Layers
^^^^^^^^^^^^^^^

The following layers must be present in your build environment:

* ``meta-imx-smw-apps``: Provides ``imx-smw-app`` application

See the `meta-imx-smw-apps <https://github.com/nxp-imx-support/meta-nxp-security-reference-design/meta-imx-smw-apps>`_ meta-layer's documentation for more details.

Required Packages
^^^^^^^^^^^^^^^^^

Runtime dependencies:

* ``smw``: Security Middleware runtime
* ``imx-smw-apps``: SMW applications
* ``imx-secure-enclave``: ELE firmware communication library (provides ``nvm_daemon``)
* ``python3``: Python 3 runtime
* ``python3-requests``: HTTP library (for client)
* ``python3-fastapi``: Web framework (for server)
* ``python3-uvicorn``: ASGI server (for server)

.. note::

   The Python packages (``python3-requests``, ``python3-fastapi``, ``python3-uvicorn``) are not installed on the target by default. You may need to add them to your image configuration when running the client and/or server scripts.

.. important::

   The ``nvm_daemon`` process (provided by ``imx-secure-enclave``) **must be running** on the target device before using any ``imx-smw-app`` attestation commands. Without it, ELE firmware services may fail. Start it with:

   .. code-block:: bash

      systemctl start nvm_daemon

   To make it persistent across reboots, add it to a startup service (see :ref:`attestation-client` for details).

Compatible Machines
^^^^^^^^^^^^^^^^^^^

The ``meta-imx-device-attest`` layer is compatible with:

- ``i.MX 9 Family``

Adding the Layer
----------------

Step 1: Add Layer to Build
^^^^^^^^^^^^^^^^^^^^^^^^^^^

From your build directory:

.. code-block:: bash

   bitbake-layers add-layer ../sources/meta-nxp-security-reference-design/meta-imx-device-attest

Step 2: Verify Layer Addition
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code-block:: bash

   bitbake-layers show-layers

You should see ``meta-imx-device-attest`` in the output.

Step 3: Add Package to Image
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Edit your ``conf/local.conf``:

.. code-block:: bash

   IMAGE_INSTALL:append = " device-attest-scripts"

Step 4: Build the package
^^^^^^^^^^^^^^^^^^^^^^^^^

Build only the ``device-attest-scripts`` package without rebuilding the entire image.

.. code-block:: bash

   bitbake device-attest-scripts

Then manually copy the files to your target.

Alternatively, you can use devtool for development and deployment:

.. code-block:: bash

   devtool build device-attest-scripts
   devtool deploy-target device-attest-scripts root@<target-ip>

Step 5: Build a Full Image (optional)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Build a complete image that includes the device attestation scripts.

.. code-block:: bash

   bitbake <imx-image-core/imx-image-full>
