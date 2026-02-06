.. _attestation-overview:

Overview
========

Introduction
------------

Device attestation is a critical security mechanism that allows a remote server to verify the authenticity and integrity of an i.MX device. The attestation process uses cryptographic operations performed within the EdgeLock Enclave (ELE) to prove device identity.

What is Device Attestation?
----------------------------

Device attestation is the process of cryptographically proving that:

1. The device is a genuine NXP i.MX device
2. The device has not been tampered with
3. The device's security state is trustworthy
4. The device possesses valid cryptographic credentials

Attestation Flow
----------------

The attestation process follows these steps:

.. code-block:: text

   ┌────────┐                                    ┌────────┐
   │ Client │                                    │ Server │
   │ Device │                                    │        │
   └───┬────┘                                    └───┬────┘
       │                                             │
       │  1. Request Nonce                           │
       │────────────────────────────────────────────>│
       │                                             │
       │  2. Return Random Nonce                     │
       │<────────────────────────────────────────────│
       │                                             │
       │  3. Generate Attestation Data (ELE)         │
       │     - Sign nonce with device key            │
       │     - Include device UUID/public key        │
       │                                             │
       │  4. Submit Attestation Data                 │
       │────────────────────────────────────────────>│
       │                                             │
       │                                             │
       │  5. Verify Attestation                      │
       │     - Validate certificate chain            │
       │     - Verify signature                      │
       │     - Check nonce freshness                 │
       │                                             │
       │  6. Return Verification Result              │
       │<────────────────────────────────────────────│
       │                                             │

Security Components
-------------------

EdgeLock Enclave (ELE)
^^^^^^^^^^^^^^^^^^^^^^

The EdgeLock Enclave is a dedicated security subsystem that:

* Stores device-unique cryptographic keys
* Performs cryptographic operations in a secure environment
* Generates attestation signatures
* Protects against physical and logical attacks

Security Middleware (SMW)
^^^^^^^^^^^^^^^^^^^^^^^^^^

The Security Middleware provides:

* Unified API for security operations
* Abstraction layer for ELE operations
* Key management services
* Device management capabilities

Attestation Modes
-----------------

EdgeLock 2GO Mode
^^^^^^^^^^^^^^^^^

Uses NXP's EdgeLock 2GO cloud service for certificate provisioning:

* Device certificates stored in EdgeLock 2GO
* Server retrieves certificates using device UUID
* Full certificate chain validation
* Requires EdgeLock 2GO API key

Public Key Mode
^^^^^^^^^^^^^^^

Uses device-specific public key for verification:

* Public key extracted from device
* Server verifies signature using public key
* Suitable for offline or custom PKI scenarios
* No cloud dependency

Use Cases
---------

* **IoT Device Onboarding**: Verify device authenticity before provisioning
* **Secure Boot Verification**: Confirm device security state
* **Zero-Touch Provisioning**: Automated device enrollment
* **Compliance Auditing**: Prove device integrity for regulatory requirements
* **Supply Chain Security**: Verify devices haven't been compromised
