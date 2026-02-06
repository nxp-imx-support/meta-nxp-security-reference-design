.. _attestation-client:

Client Usage
============

Overview
--------

The ``rest_client.py`` script runs on the i.MX device and communicates with the attestation server to prove device authenticity.

Prerequisites
-------------

On the Device
^^^^^^^^^^^^^

* ``imx-smw-app`` installed and in PATH
* ``python3`` with ``requests`` module
* Network connectivity to attestation server

Client Script Location
----------------------

After installing the ``device-attest-scripts`` package:

.. code-block:: bash

   cd /root/device_attestation/

Command Syntax
--------------

.. code-block:: bash

   python3 rest_client.py <server_ip> <endpoint>

Parameters
^^^^^^^^^^

* ``server_ip``: IP address of the attestation server
* ``endpoint``: Operation to perform (``nonce``, ``attest_el2go``, or ``attest_pubkey``)

Attestation Workflow
--------------------

Step 1: Request Nonce
^^^^^^^^^^^^^^^^^^^^^

Request a fresh nonce from the server:

.. code-block:: bash

   python3 rest_client.py 192.168.1.100 nonce

**Output**:

.. code-block:: text

   [CLIENT] Nonce successfully saved to 'nonce.bin'.

**What happens**:

1. Client sends GET request to ``http://192.168.1.100:8000/get_nonce``
2. Server generates cryptographic random nonce
3. Client saves nonce to ``nonce.bin``

Step 2: Perform Attestation (EL2GO Mode)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Attest using EdgeLock 2GO certificates:

.. code-block:: bash

   python3 rest_client.py 192.168.1.100 attest_el2go

**What happens**:

1. Retrieves device UUID
2. Generates attestation data
3. Sends attestation data and UUID to server
4. Server retrieves certificates from EdgeLock 2GO
5. Server verifies attestation

**Output (Success)**:

.. code-block:: text

   [CLIENT] Server response: success - Device attestation verified successfully

**Output (Failure)**:

.. code-block:: text

   [CLIENT] Server response: error - Attestation verification failed

Step 3: Perform Attestation (Public Key Mode)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Attest using device public key:

.. code-block:: bash

   python3 rest_client.py 192.168.1.100 attest_pubkey

**What happens**:

1. Generates attestation data
2. Exports attestation public key
3. Sends attestation data and public key to server
4. Server verifies signature using provided public key

Generated Files
---------------

After running attestation commands, the following files are created:

.. code-block:: text

   /root/device_attestation/
   ├── nonce.bin                  # Server-provided nonce
   ├── uuid.bin                   # Device UUID (EL2GO mode)
   ├── dev_attest_data.bin        # Signed attestation data
   └── die_attest_pub_key.bin     # Device public key (pubkey mode)

Usage Examples
--------------

Complete Attestation Flow
^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code-block:: bash

   # Navigate to scripts directory
   cd /root/device_attestation/

   # Step 1: Get nonce
   python3 rest_client.py 192.168.1.100 nonce

   # Step 2: Perform attestation (choose one)
   # Option A: EL2GO mode
   python3 rest_client.py 192.168.1.100 attest_el2go

   # Option B: Public key mode
   python3 rest_client.py 192.168.1.100 attest_pubkey

Troubleshooting
---------------

Connection Errors
^^^^^^^^^^^^^^^^^

**Error**: ``[CLIENT] Error requesting nonce: Connection refused``

**Causes**:

* Server not running
* Incorrect server IP
* Firewall blocking port 8000
* Network connectivity issues

**Solutions**:

.. code-block:: bash

   # Test connectivity
   ping 192.168.1.100

   # Check if server port is open
   nc -zv 192.168.1.100 8000

Missing Python Modules
^^^^^^^^^^^^^^^^^^^^^^^

**Error**: ``ModuleNotFoundError: No module named 'requests'``

**Solution**:

.. code-block:: bash

   # Install requests module
   pip3 install requests

   # Or add to image recipe
   IMAGE_INSTALL:append = " python3-requests"

Server Response Errors
^^^^^^^^^^^^^^^^^^^^^^

**Error**: ``[CLIENT] Server response: error - Certificate chain verification failed``

**Causes**:

* Device not properly provisioned
* Invalid certificates in EdgeLock 2GO
* Certificate chain incomplete

**Solutions**:

* Verify device provisioning status
* Check EdgeLock 2GO console for device certificates
* Contact NXP support for provisioning issues

Best Practices
--------------

Security
^^^^^^^^

* Always request fresh nonce before attestation
* Don't reuse nonce files
* Secure network communication (use HTTPS in production)
* Protect generated attestation files
