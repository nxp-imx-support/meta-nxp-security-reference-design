.. _attestation-server:

Server Usage
============

Overview
--------

The ``rest_server.py`` script provides a FastAPI-based REST server for verifying device attestation. It can run on a separate machine or cloud instance.

Prerequisites
-------------

Server Requirements
^^^^^^^^^^^^^^^^^^^

* Python 3.7 or later
* Required Python packages:

  * ``fastapi``
  * ``uvicorn``
  * ``requests``
  * ``cryptography``
  * ``pydantic``

* OpenSSL command-line tools
* Network connectivity to client devices
* (Optional) EdgeLock 2GO API key for EL2GO mode

Installation
------------

Install Dependencies
^^^^^^^^^^^^^^^^^^^^

.. code-block:: bash

   pip3 install fastapi uvicorn requests cryptography pydantic

Verify OpenSSL
^^^^^^^^^^^^^^

.. code-block:: bash

   openssl version

Expected output:

.. code-block:: text

   OpenSSL 3.0.x or later

EdgeLock 2GO Setup
^^^^^^^^^^^^^^^^^^

For EL2GO attestation mode, obtain an API key:

1. Register at https://www.edgelock2go.com
2. Create a product configuration for your i.MX device
3. Generate an API key
4. Set environment variable:

.. code-block:: bash

   export EL2GOAPIKEY="your-api-key-here"

Server Configuration
--------------------

Server Script Location
^^^^^^^^^^^^^^^^^^^^^^

The server script can run on any machine with Python 3:

.. code-block:: bash

   # Copy from device or repository
   scp root@<device-ip>:/root/device_attestation/rest_server.py .

   # Or use from repository
   cd meta-nxp-security-reference-design/meta-imx-device-attest/recipes-device-attest/device-attest-scripts/device-attest-scripts/

Starting the Server
-------------------

Basic Server Start
^^^^^^^^^^^^^^^^^^

.. code-block:: bash

   # Use uvicorn directly
   uvicorn rest_server:app --host 192.168.1.100 --port 8000

The server starts on ``http://0.0.0.0:8000``

**Output**:

.. code-block:: text

   INFO:     Started server process [12345]
   INFO:     Waiting for application startup.
   INFO:     Application startup complete.
   INFO:     Uvicorn running on http://0.0.0.0:8000 (Press CTRL+C to quit)

API Endpoints
-------------

GET /get_nonce
^^^^^^^^^^^^^^

Generates and returns a cryptographic nonce.

**Request**:

.. code-block:: bash

   curl http://192.168.1.100:8000/get_nonce

**Response**:

.. code-block:: json

   {
       "nonce": "a1b2c3d4e5f6..."
   }

**Server Output**:

.. code-block:: text

   [SERVER] Nonce generated and sent to client.

POST /attest_el2go
^^^^^^^^^^^^^^^^^^

Verifies device attestation using EdgeLock 2GO certificates.

**Request**:

.. code-block:: bash

   curl -X POST http://192.168.1.100:8000/attest_el2go \
       -H "Content-Type: application/json" \
       -d '{
           "device_attest_data": "hexstring...",
           "device_uuid": "hexstring..."
       }'

**Response (Success)**:

.. code-block:: json

   {
       "status": "success",
       "message": "Device attestation verified successfully"
   }

**Response (Failure)**:

.. code-block:: json

   {
       "status": "error",
       "message": "Attestation verification failed"
   }

POST /attest_pubkey
^^^^^^^^^^^^^^^^^^^

Verifies device attestation using provided public key.

**Request**:

.. code-block:: bash

   curl -X POST http://192.168.1.100:8000/attest_pubkey \
       -H "Content-Type: application/json" \
       -d '{
           "device_attest_data": "hexstring...",
           "die_attest_public_key": "hexstring..."
       }'

**Response (Success)**:

.. code-block:: json

   {
       "status": "success",
       "message": "Device attestation verified successfully"
   }

**Response (Failure)**:

.. code-block:: json

   {
       "status": "error",
       "message": "Attestation verification failed"
   }

Usage Examples
--------------

Running Server Locally
^^^^^^^^^^^^^^^^^^^^^^

.. code-block:: bash

   # Set EL2GO API key
   export EL2GOAPIKEY="your-api-key"

   # Start server
   uvicorn rest_server:app --host 192.168.1.100 --port 8000

   # Server is now listening on http://0.0.0.0:8000

Troubleshooting
---------------

EL2GO API Errors
^^^^^^^^^^^^^^^^

**Error**: ``[SERVER] ❌ EL2GOAPIKEY environment variable not set.``

**Solution**:

.. code-block:: bash

   export EL2GOAPIKEY="your-api-key"
   # Verify
   echo $EL2GOAPIKEY

**Error**: ``[SERVER] ❌ Failed to fetch certificates from EL2GO server: 401``

**Causes**:

* Invalid API key
* API key expired
* Incorrect permissions

**Solutions**:

* Verify API key in EdgeLock 2GO console
* Regenerate API key if necessary
* Check API key permissions

**Error**: ``[SERVER] ❌ Failed to fetch certificates from EL2GO server: 404``

**Causes**:

* Device not provisioned in EdgeLock 2GO
* Incorrect device UUID
* Wrong product configuration

**Solutions**:

* Verify device is provisioned in EL2GO console
* Check device UUID matches provisioned device
* Ensure correct product type (IMX9)

Certificate Verification Errors
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

**Error**: ``[SERVER] ❌ Certificate chain verification failed``

**Causes**:

* Missing root CA certificate
* Invalid certificate chain
* Certificate expired

**Solutions**:

.. code-block:: bash

   # Verify root CA certificate exists
   ls -la nxp_tp_attestation_root.pem

   # Check certificate validity
   openssl x509 -in nxp_tp_attestation_root.pem -text -noout

   # Verify certificate chain manually
   openssl verify -CAfile nxp_tp_attestation_root.pem \
       -untrusted nxp_12nc_attest_ca_cert.pem \
       nxp_die_attest_auth_cert.pem

**Error**: ``[SERVER] ❌ Device or intermediate CA certificate not found.``

**Causes**:

* Incomplete certificate chain from EL2GO
* Device not properly provisioned
* Certificate labels don't match

**Solutions**:

* Check EL2GO console for complete certificate chain
* Verify device provisioning status
* Contact NXP support

Port Binding Errors
^^^^^^^^^^^^^^^^^^^

**Error**: ``OSError: [Errno 98] Address already in use``

**Causes**:

* Port 8000 already in use
* Previous server instance still running

**Solutions**:

.. code-block:: bash

   # Find process using port 8000
   sudo lsof -i :8000

   # Kill the process
   sudo kill -9 <PID>
