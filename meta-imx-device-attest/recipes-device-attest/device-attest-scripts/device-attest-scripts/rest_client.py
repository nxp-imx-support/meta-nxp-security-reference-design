#!/usr/bin/python3

# Copyright 2025 NXP
# SPDX-License-Identifier: BSD-3-Clause

import sys
import requests
import subprocess

# Function to convert binary file to hex string
def file_to_hex(file_path):
    with open(file_path, "rb") as f:
        return f.read().hex()

# Function to request nonce from server
def req_nonce(server_ip):
    url = f"http://{server_ip}:8000/get_nonce"
    try:
        response = requests.get(url)
        response.raise_for_status()
        nonce_hex = response.json().get("nonce", "")
        if not nonce_hex:
            print("[CLIENT] Error: 'nonce' not found in Server response.")
            return
        nonce_bin = bytes.fromhex(nonce_hex)
        with open("nonce.bin", "wb") as f:
            f.write(nonce_bin)
        print("[CLIENT] Nonce successfully saved to 'nonce.bin'.")
    except Exception as e:
        print(f"[CLIENT] Error requesting nonce: {e}")

# Function to send attestation data to server
def submit_attestation_el2go(server_ip):

    try:
        subprocess.run(["imx-smw-app", "dev-mgmt", "-u", "-o", "uuid.bin"], check=True)
        subprocess.run(["imx-smw-app", "dev-mgmt", "-a", "-n", "nonce.bin", "-o", "dev_attest_data.bin"], check=True)
    except subprocess.CalledProcessError as e:
        print(f"[CLIENT] Error running subprocess: {e}")
        return
    except Exception as e:
        print(f"[CLIENT] Unexpected error: {e}")
        return

    try:
        attest_data_hex = file_to_hex("dev_attest_data.bin")
        uuid_hex = file_to_hex("uuid.bin")
        post_url = f"http://{server_ip}:8000/attest_el2go"
        payload = {
            "device_attest_data": attest_data_hex,
            "device_uuid" : uuid_hex
        }
        post_response = requests.post(post_url, json=payload)
        post_response.raise_for_status()
        response_data = post_response.json()
        print(f"[CLIENT] Server response: {response_data['status']} - {response_data['message']}")
    except Exception as e:
        print(f"[CLIENT] Error submitting attestation: {e}")

# Function to send attestation data to server
def submit_attestation_pubkey(server_ip):

    try:
        subprocess.run(["imx-smw-app", "dev-mgmt", "-a", "-n", "nonce.bin", "-o", "dev_attest_data.bin"], check=True)
        subprocess.run(["imx-smw-app", "key-mgmt", "-a", "-o", "die_attest_pub_key.bin"], check=True)
    except subprocess.CalledProcessError as e:
        print(f"[CLIENT] Error running subprocess: {e}")
        return
    except Exception as e:
        print(f"[CLIENT] Unexpected error: {e}")
        return
    
    try:
        attest_data_hex = file_to_hex("dev_attest_data.bin")
        attest_key_hex = file_to_hex("die_attest_pub_key.bin")
        post_url = f"http://{server_ip}:8000/attest_pubkey"
        payload = {
            "device_attest_data": attest_data_hex,
            "die_attest_public_key": attest_key_hex
        }
        post_response = requests.post(post_url, json=payload)
        post_response.raise_for_status()
        response_data = post_response.json()
        print(f"[CLIENT] Server response: {response_data['status']} - {response_data['message']}")
    except Exception as e:
        print(f"[CLIENT] Error submitting attestation: {e}")

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print(f"[CLIENT] Usage: python3 {sys.argv[0]} <server_ip> <endpoint>")
    else:
        server_ip = sys.argv[1]
        endpoint = sys.argv[2].lower()
        if endpoint == "nonce":
            req_nonce(server_ip)
        elif endpoint == "attest_pubkey":
            submit_attestation_pubkey(server_ip)
        elif endpoint == "attest_el2go":
            submit_attestation_el2go(server_ip)
        else:
            print("[CLIENT] Invalid endpoint. Use 'nonce'/'attest_pubkey'/'attest_el2go'")
