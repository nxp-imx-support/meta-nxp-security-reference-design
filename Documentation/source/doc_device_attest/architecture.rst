.. _attestation-architecture:

Architecture
============

System Architecture
-------------------

The device attestation system consists of three main components:

.. code-block:: text

   ┌────────────────────────────────────────────────────────┐
   │                    Client Device                       │
   │  ┌──────────────────────────────────────────────────┐  │
   │  │           rest_client.py                         │  │
   │  │  - Request nonce                                 │  │
   │  │  - Invoke imx-smw-app                            │  │
   │  │  - Submit attestation data                       │  │
   │  └──────────────┬───────────────────────────────────┘  │
   │                 │                                      │
   │  ┌──────────────▼───────────────────────────────────┐  │
   │  │           imx-smw-app                            │  │
   │  │  - Device management operations                  │  │
   │  │  - Key management operations                     │  │
   │  │  - Interface to SMW                              │  │
   │  └──────────────┬───────────────────────────────────┘  │
   │                 │                                      │
   │  ┌──────────────▼───────────────────────────────────┐  │
   │  │    Security Middleware (SMW)                     │  │
   │  │  - Abstraction layer for security operations     │  │
   │  └──────────────┬───────────────────────────────────┘  │
   │                 │                                      │
   │  ┌──────────────▼───────────────────────────────────┐  │
   │  │      EdgeLock Enclave (ELE)                      │  │
   │  │  - Secure key storage                            │  │
   │  │  - Cryptographic operations                      │  │
   │  │  - Attestation signature generation              │  │
   │  └──────────────────────────────────────────────────┘  │
   └────────────────────────────────────────────────────────┘
                             │
                             │ HTTPS/REST
                             │
   ┌─────────────────────────▼─────────────────────────────┐
   │                 Attestation Server                    │
   │  ┌──────────────────────────────────────────────────┐ │
   │  │           rest_server.py                         │ │
   │  │  - Generate nonces                               │ │
   │  │  - Retrieve certificates (EL2GO)                 │ │
   │  │  - Verify attestation data                       │ │
   │  │  - Validate certificate chains                   │ │
   │  └──────────────────────────────────────────────────┘ │
   └───────────────────────────────────────────────────────┘
                             │
                             │ HTTPS API
                             │
   ┌─────────────────────────▼─────────────────────────────┐
   │            EdgeLock 2GO Cloud Service                 │
   │  - Certificate provisioning                           │
   │  - Device certificate storage                         │
   │  - Certificate chain management                       │
   └───────────────────────────────────────────────────────┘

Component Details
-----------------

Client Components
^^^^^^^^^^^^^^^^^

**rest_client.py**

Python script running on the i.MX device that:

* Communicates with the attestation server via REST API
* Invokes ``imx-smw-app`` for cryptographic operations
* Handles three main operations:

  * ``nonce``: Request fresh nonce from server
  * ``attest_el2go``: Perform attestation with EL2GO integration
  * ``attest_pubkey``: Perform attestation with public key

**imx-smw-app**

Command-line application that interfaces with SMW:

* ``dev-mgmt -u``: Get device UUID
* ``dev-mgmt -a``: Generate attestation data
* ``key-mgmt -a``: Export attestation public key

Server Components
^^^^^^^^^^^^^^^^^

**rest_server.py**

FastAPI-based server that:

* Provides REST endpoints for attestation
* Generates cryptographic nonces
* Retrieves certificates from EdgeLock 2GO
* Verifies attestation signatures
* Validates certificate chains using OpenSSL

Certificate Hierarchy (EdgeLock 2GO method)
-------------------------------------------

The attestation uses a three-level certificate chain:

.. code-block:: text

   ┌─────────────────────────────────────────────────┐
   │  NXP TP Attestation Root CA Certificate         │
   │  (Root of Trust)                                │
   └──────────────────┬──────────────────────────────┘
                      │
                      │ Signs
                      │
   ┌──────────────────▼──────────────────────────────┐
   │  NXP 12NC Attestation CA Certificate            │
   │  (Product-specific Intermediate CA)             │
   └──────────────────┬──────────────────────────────┘
                      │
                      │ Signs
                      │
   ┌──────────────────▼──────────────────────────────┐
   │  NXP DIE Attestation Authentication Cert        │
   │  (Device-specific Leaf Certificate)             │
   │  - Unique per device                            │
   │  - Contains device public key                   │
   └─────────────────────────────────────────────────┘

Data Flow
---------

Attestation Data Structure
^^^^^^^^^^^^^^^^^^^^^^^^^^

The attestation data generated by ELE contains:

* **Signature**: Cryptographic signature over the nonce
* **Device Information**: UUID or public key
* **Metadata**: Additional device-specific information

All data is transmitted as hexadecimal strings via JSON payloads.

Network Communication
^^^^^^^^^^^^^^^^^^^^^

REST API Endpoints:

* ``GET /get_nonce``: Request fresh nonce
* ``POST /attest_el2go``: Submit attestation with UUID
* ``POST /attest_pubkey``: Submit attestation with public key

Security Considerations
-----------------------

Nonce Management
^^^^^^^^^^^^^^^^

* Nonces are cryptographically random
* Single-use to prevent replay attacks
* Server validates nonce freshness

Key Protection
^^^^^^^^^^^^^^

* Private keys never leave ELE
* All signing operations performed in secure enclave
* Public keys can be safely exported

Certificate Validation
^^^^^^^^^^^^^^^^^^^^^^

* Full chain validation from root CA
* Proper OpenSSL verification flags used