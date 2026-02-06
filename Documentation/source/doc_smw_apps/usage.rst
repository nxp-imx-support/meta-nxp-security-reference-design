=====
Usage
=====

This guide explains how to use the imx-smw-apps CLI application on your i.MX target device.

Getting Started
===============

Prerequisites
-------------

Before running the application, ensure:

1. Image with imx-smw-apps is deployed to target
2. Target device is booted successfully
3. SMW library is properly installed
4. EdgeLock Secure Enclave (ELE) is enabled and functional

Verifying Installation
----------------------

Check if the application is installed:

.. code-block:: bash

   which imx-smw-apps

Expected output:

.. code-block:: text

   /usr/bin/imx-smw-apps

Verify SMW library is available:

.. code-block:: bash

   ldconfig -p | grep libsmw

Check ELE driver is loaded:

.. code-block:: bash

   dmesg | grep -i ele

Application Overview
====================

Command Structure
-----------------

The imx-smw-apps application uses a two-level command structure:

.. code-block:: text

   imx-smw-apps <interface> [options]

Where ``<interface>`` is one of:

* ``dev-mgmt`` - Device management operations
* ``key-mgmt`` - Key management operations

Getting Help
------------

Display general help:

.. code-block:: bash

   imx-smw-apps --help|-h

Common Options
==============

Output File Specification
--------------------------

All operations support the ``--output`` option:

.. code-block:: bash

   imx-smw-apps dev-mgmt --uuid --output /tmp/uuid.bin
   imx-smw-apps key-mgmt --die-attest-pub-key --output /tmp/pubkey.bin

Device Management Interface
============================

The ``dev-mgmt`` interface provides access to device-level security information.

- UUID
- Lifecycle State
- Device Attestation Certificate

Key Management Interface
=========================

The ``key-mgmt`` interface provides cryptographic key operations and management.

- DIE Attestation Public Key
