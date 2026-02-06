========
Overview
========

Project Purpose
===============

The i.MX Security Middleware Applications (imx-smw-apps) project provides a command-line interface for interacting with the NXP Security Middleware (SMW) framework on i.MX platforms. It enables developers and system administrators to perform security operations, manage cryptographic keys, and query device security information.

Key Objectives
--------------

* **CLI Access to SMW**: Provide command-line access to SMW security features
* **Device Management**: Query and manage device security attributes
* **Key Management**: Perform cryptographic key operations
* **Development Tool**: Aid in security application development and testing

Architecture
============

Application Structure
---------------------

.. code-block:: text

    ┌─────────────────────────────────────────┐
    │   imx-smw-apps CLI Application          │
    │                                         │
    │   ┌─────────────┐   ┌───────────────┐  │
    │   │ dev-mgmt    │   │  key-mgmt     │  │
    │   │ Interface   │   │  Interface    │  │
    │   └──────┬──────┘   └───────┬───────┘  │
    │          │                  │          │
    │          └──────────┬───────┘          │
    │                     │                  │
    └─────────────────────┼──────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────┐
    │   Security Middleware (SMW) Library     │
    │   - Device APIs                         │
    │   - Key Management APIs                 │
    │   - Cryptographic Operations            │
    └─────────────────────┼───────────────────┘
                          │
                          ▼
    ┌─────────────────────────────────────────┐
    │   i.MX Hardware Security                │
    │   - EdgeLock Secure Enclave (ELE)       │
    │   - Secure Storage                      │
    │   - Hardware Crypto Accelerators        │
    └─────────────────────────────────────────┘

Dependencies
------------

Runtime Dependencies
~~~~~~~~~~~~~~~~~~~~

* **smw**: Security Middleware library (required)
  
  - Provides core security APIs
  - Interfaces with hardware security modules
  - Manages secure key storage

Build Dependencies
~~~~~~~~~~~~~~~~~~

* **smw**: SMW development headers and libraries
* **cmake**: Build system (version 3.x or higher)
* **Cross-compilation toolchain**: For target i.MX platform

Interfaces
==========

The application provides two main interfaces:

- Device Management Interface (dev-mgmt)
- Key Management Interface (key-mgmt)

Platform Support
================

Compatible Machines
-------------------

The meta-imx-smw-apps layer supports:

- ``i.MX 8ULP``
- ``i.MX 9 Family``


