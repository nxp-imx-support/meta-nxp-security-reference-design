#!/usr/bin/python3

# Copyright 2025 NXP
# SPDX-License-Identifier: BSD-3-Clause

# FastAPI framework for building REST APIs
from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
# Requests for HTTP API calls
import requests
# JSON module for encoding/decoding
import json

# Cryptography library for certificate and signature handling
from cryptography.hazmat.primitives.asymmetric import ec
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric.utils import encode_dss_signature
from cryptography.exceptions import InvalidSignature
from cryptography.hazmat.backends import default_backend
from cryptography import x509
from cryptography.hazmat.primitives import serialization

# Pydantic for data validation
from pydantic import BaseModel
# OS and Subprocess to run shell commands and file operations
import os
import subprocess

app = FastAPI()

# Endpoint to generate a 16-byte nonce
@app.get("/get_nonce")
async def get_nonce():
    # Creates random numbers
    nonce = os.urandom(16)
    # Create a nonce data binary file created by server
    with open("server_nonce.bin", "wb") as f:
        f.write(nonce)
    print("[SERVER] Server created server_nonce.bin successfully")
    return JSONResponse(content={"nonce": nonce.hex()})

# Define the expected structure of attestation data for EL2GO process
class AttestationDataEL2GO(BaseModel):
    device_attest_data: str  # hex string
    device_uuid : str # hex string

# Define the expected structure of attestation data for DIE Attest Public Key process
class AttestationDataPubKey(BaseModel):
    device_attest_data: str  # hex string
    die_attest_public_key: str  # hex string

# Add PEM format outline to PEM certificates
def pem_format(cert_str):
    return f"-----BEGIN CERTIFICATE-----\n{cert_str}\n-----END CERTIFICATE-----"

# Fetch certificate chain from EL2GO server and save to files
def get_cert_chain_el2go(uuid):
    # Check for environment variable
    el2g_api_key = os.getenv("EL2GOAPIKEY")
    if not el2g_api_key:
        print("[SERVER] ❌ EL2GOAPIKEY environment variable not set.")
        raise RuntimeError

    # Define the EL2GO URL and headers to fetch certificates
    ###################################
    # Only iMX9 is supported currently
    ###################################
    url = "https://api.edgelock2go.com/api/v1/kds/products/download-device-certificates/IMX9"
    headers = {
        "accept": "application/json",
        "EL2G-API-Key": el2g_api_key,
        "Content-Type": "application/json"
    }

    # Prepare the payload for EL2GO server
    payload = {
        "deviceIds": [uuid],
        "searchKey": "CERT",
        "includeIntermediateCa": "true"
    }

    print("[SERVER] Downloading certificate chain....")
    print(".\n.\n.\n.\n")
    # Send the POST request to EL2GO server
    response = requests.post(url, headers=headers, data=json.dumps(payload))
    if not response.ok:
        print(f"[SERVER] ❌ Failed to fetch certificates from EL2GO server: {response.status_code} {response.text}")
        raise RuntimeError
    cert_chain = response.json()
    # print(json.dumps(response_data, indent=4))

    device_cert = None
    intermediate_ca_cert = None

    # Loop through the cert chain to find NXP DIE Attest Auth Cert and NXP 12NC Attest CA Cert
    for certs in cert_chain:
        for cert in certs.get("deviceCertificates", []):
            if cert["certificateLabel"] == "NXP_DIE_Attest_AUTH_CERT":
                device_cert = cert["certificate"]
                break
        
        for ca in certs.get("intermediateCAs", []):
            if ca["certificateLabel"] == "NXP_12NC_Attest_CA_CERT":
                intermediate_ca_cert = ca["certificate"]
                break

    if not device_cert or not intermediate_ca_cert:
        print("[SERVER] ❌ Device or intermediate CA certificate not found.")
        raise RuntimeError
    # Output the results
    # print("Device Certificate (NXP_DIE_Attest_AUTH_CERT):", device_cert)
    # print("Intermediate CA Certificate (NXP_12NC_Attest_CA_CERT):", intermediate_ca_cert)

    # Convert to PEM format
    device_cert_pem = pem_format(device_cert)
    intermediate_ca_cert_pem = pem_format(intermediate_ca_cert)

    # Output the PEM-formatted certificates
    # print("Device Certificate PEM:\n", device_cert_pem)
    # print("\nIntermediate CA Certificate PEM:\n", intermediate_ca_cert_pem)

    # Save certificates in file
    with open("nxp_die_attest_auth_cert.pem", "w") as f:
        f.write(device_cert_pem)
    with open("nxp_12nc_attest_ca_cert.pem", "w") as f:
        f.write(intermediate_ca_cert_pem)

    print("[SERVER] Downloading Attestation Root certificate....")
    print(".\n.\n.\n.\n")
    # Download the DER-encoded NXP TP Attestation Root certificate
    cert_url = "https://www.gp-ca.nxp.com/CA/getCA?caid=63709320070004"
    cert_response = requests.get(cert_url)
    if not cert_response.ok:
        print(f"[SERVER] ❌ Failed to download certificate from NXP: {cert_response.status_code} {cert_response.text}")
        raise RuntimeError
    
    # Save NXP TP Attestation Root certificate
    with open("nxp_tp_attestation_root.der", "wb") as cert_file:
        cert_file.write(cert_response.content)
    # print("Downloaded 'nxp_tp_attestation_root.der' successfully.")

    conv_der_to_pem_command = [
        "openssl", "x509",
        "-inform", "der",
        "-in", "nxp_tp_attestation_root.der",
        "-out", "nxp_tp_attestation_root.pem",
        "-outform", "pem"
    ]

    try:
        subprocess.run(conv_der_to_pem_command, check=True)
    except subprocess.CalledProcessError as e:
        print(f"[SERVER] ❌ OpenSSL command to convert cert format from DER TO PEM, failed: {e}") 
        raise RuntimeError
    # print("Successfully converted nxp_tp_attestation_root certificate format from DER to PEM using openssl.")

# Verify certificate chain using OpenSSL
# NXP TP Attestation Root CA cert
#         |
#         v
# NXP 12NC Attestation CA cert
#         |
#         v
# NXP DIE Attestation Authentication Cert (device specific)
def verify_cert_chain():

    print("[SERVER] Verifying certificate chain...")
    print("[SERVER] NXP TP Attestation Root CA cert")
    print("[SERVER]          |")
    print("[SERVER]          |")
    print("[SERVER]          v")
    print("[SERVER] NXP 12NC Attestation CA cert")
    print("[SERVER]          |")
    print("[SERVER]          |")
    print("[SERVER]          v")
    print("[SERVER] NXP DIE Attestation Authentication Cert (device specific)")
    print(".\n.\n.\n.\n")    
    cert_chain_verify_command = [
        "openssl", "verify", "-verbose",
        "-CAfile", "nxp_tp_attestation_root.pem",
        "-untrusted", "nxp_12nc_attest_ca_cert.pem",
        "nxp_die_attest_auth_cert.pem"
    ]

    # Execute the command and capture output
    try:
        result = subprocess.run(cert_chain_verify_command, check=True, capture_output=True, text=True)
    except subprocess.CalledProcessError as e:
        print(e.stderr)
        print("[SERVER] ❌ Certificate chain verification failed:")
        raise RuntimeError
    print("[SERVER] ✅ Certificate chain verification successful:")
    print(result.stdout)


def verify_device_attestation_signature(attest_data_bin, public_key):

    print("[SERVER] Extracting message and signature from Attestation Data")
    print(".\n.\n.\n.\n")
    # Extract signature and message
    message = attest_data_bin[:-96]
    signature = attest_data_bin[-96:]
    # print("message: " + str(message))
    # print("signature: " + str(signature))
    # Save to files
    with open("message.bin", "wb") as f:
        f.write(message)
    with open("signature.bin", "wb") as f:
        f.write(signature)

    # Split raw signature into r and s
    r = int.from_bytes(signature[:48], byteorder='big')
    s = int.from_bytes(signature[48:], byteorder='big')

    # Encode to DER format
    der_signature = encode_dss_signature(r, s)
    # print("der_signature: " + str(der_signature))

    print("[SERVER] Verifying signature using public key...")
    print("[SERVER] DIE Attestation Public Key")
    print("[SERVER]          +")
    print("[SERVER] Data Attestation Message ")
    print("[SERVER]          |")
    print("[SERVER]        Verify")
    print("[SERVER]          |")
    print("[SERVER]          v")
    print("[SERVER] Data Attestation Signature")
    
    print(".\n.\n.\n.\n")
    # Verify signature
    try:
        public_key.verify(der_signature, message, ec.ECDSA(hashes.SHA384()))
        print("[SERVER] ✅ Signature verified successfully for received attestation request.")
        return {"status": "success", "message": "Signature verified successfully."}
    except InvalidSignature:
        print("[SERVER] ❌ Signature verification failed.")
        return {"status": "error", "message": "Signature verification failed."}

# Modify message for negative testing
# def negative_testing(attest_data_bin):
    ###### START NEGATIVE TESTING ######
    # #Convert message to mutable bytearray
    # message_bytes = bytearray(attest_data_bin[:-96])

    # # Modify the last byte (e.g., flip all bits)
    # message_bytes[-1] ^= 0xFF

    # # Convert back to bytes if needed for verification
    # message = bytes(message_bytes)
    ###### END NEGATIVE TESTING ######

#######################################################################
#
#
# Endpoint to receive attestation request from client using EL2GO
#
#
#######################################################################
@app.post("/attest_el2go")
async def attestation_el2go(data: AttestationDataEL2GO):

    print("[SERVER] Begin Device Attestation Verification via EdgeLock 2GO....")
    print(".\n.\n.\n.\n")
    
    # Convert UUID from base 16 to base 10 string
    try:
        uuid = str(int(data.device_uuid, 16))
    except ValueError:
        return JSONResponse(status_code=400, content={"status": "error", "message": "Invalid hex string in device_uuid"})
    # print("Converted device_uuid from base 16 to base 10:", uuid)

    print("[SERVER] Start Downloading the certificate chain to verify Device Attestation Certificate")
    print(".\n.\n.\n.\n")
    try:
        # Fetch device certificate and root certificate from EL2GO server using UUID and EL2GO API Key
        get_cert_chain_el2go(uuid)
    except RuntimeError as e:
        return JSONResponse(status_code=400, content={"status": "error", "message": str(e)})

    print("[SERVER] Verify certificate chain")
    print(".\n.\n.\n.\n")    
    try:
        # Verify certificate chain
        verify_cert_chain()
    except RuntimeError as e:
        return JSONResponse(status_code=500, content={"status": "error", "message": str(e)})

    print("[SERVER] Extract DIE Attestation Public Key....")
    print("[SERVER] NXP DIE Attestation Authentication Cert")
    print("[SERVER]          |")
    print("[SERVER]       Extract")
    print("[SERVER]          |")
    print("[SERVER]          v")
    print("[SERVER] NXP DIE Attestation Public Key")
    print(".\n.\n.\n.\n")    
    # Load the NXP DIE Attest Auth PEM certificate to extract public key
    with open("nxp_die_attest_auth_cert.pem", "rb") as cert_file:
        cert_data = cert_file.read()
        die_attest_auth_cert = x509.load_pem_x509_certificate(cert_data, default_backend())

    # Extract the public key
    public_key = die_attest_auth_cert.public_key()

    # Serialize the public key to PEM format
    public_key_pem = public_key.public_bytes(
        encoding=serialization.Encoding.PEM,
        format=serialization.PublicFormat.SubjectPublicKeyInfo
    )
    # Print the public key in PEM format
    # print(public_key_pem.decode())

    # Convert hex strings to binary
    attest_data_bin = bytes.fromhex(data.device_attest_data)

    # Save to files
    with open("client_device_attest_data.bin", "wb") as f:
        f.write(attest_data_bin)

    # Validate lengths
    if len(attest_data_bin) < 96:
        return JSONResponse(status_code=400, content={"status": "error", "message": "Invalid attestation data length."})

    result = verify_device_attestation_signature(attest_data_bin, public_key)
    if result["status"] == "success":
        return JSONResponse(content=result)
    else:
        return JSONResponse(status_code=400, content=result)

#######################################################################
#
#
# Endpoint to receive attestation request from client using Public Key
#
#
#######################################################################
@app.post("/attest_pubkey")
async def attestation_pubkey(data: AttestationDataPubKey):

    print("[SERVER] Begin Device Attestation Verification using DIE attestation public key....")
    print(".\n.\n.\n.\n")    

    # Convert hex strings to binary
    attest_data_bin = bytes.fromhex(data.device_attest_data)
    attest_key_bin = bytes.fromhex(data.die_attest_public_key)

    # Save to files
    with open("client_device_attest_data.bin", "wb") as f:
        f.write(attest_data_bin)
    with open("client_die_attest_public_key.bin", "wb") as f:
        f.write(attest_key_bin)

    # Validate lengths
    if len(attest_data_bin) < 96 or len(attest_key_bin) != 96:
        return JSONResponse(status_code=400, content={"status": "error", "message": "Invalid device attestation data or die attest pubic key length."})

    # Split into X and Y coordinates
    x = int.from_bytes(attest_key_bin[:48], byteorder='big')
    y = int.from_bytes(attest_key_bin[48:], byteorder='big')

    try:
        # Construct public key
        public_numbers = ec.EllipticCurvePublicNumbers(x, y, ec.SECP384R1())
        # print("public numbers: " + str(public_numbers))
        public_key = public_numbers.public_key(default_backend())
        # print("public key: " + str(public_key))
    except Exception as e:
        return JSONResponse(status_code=400, content={"status": "error", "message": f"Failed to load public key: {str(e)}"})

    result = verify_device_attestation_signature(attest_data_bin, public_key)
    if result["status"] == "success":
        return JSONResponse(content=result)
    else:
        return JSONResponse(status_code=400, content=result)
