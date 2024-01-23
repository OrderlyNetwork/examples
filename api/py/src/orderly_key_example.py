from datetime import datetime
import json
import math
import os
import requests

from base58 import b58encode
from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from eth_account import Account, messages


def encode_key(key: bytes):
    return "ed25519:%s" % b58encode(key).decode("utf-8")


MESSAGE_TYPES = {
    "EIP712Domain": [
        {"name": "name", "type": "string"},
        {"name": "version", "type": "string"},
        {"name": "chainId", "type": "uint256"},
        {"name": "verifyingContract", "type": "address"},
    ],
    "Registration": [
        {"name": "brokerId", "type": "string"},
        {"name": "chainId", "type": "uint256"},
        {"name": "timestamp", "type": "uint64"},
        {"name": "registrationNonce", "type": "uint256"},
    ],
    "AddOrderlyKey": [
        {"name": "brokerId", "type": "string"},
        {"name": "chainId", "type": "uint256"},
        {"name": "orderlyKey", "type": "string"},
        {"name": "scope", "type": "string"},
        {"name": "timestamp", "type": "uint64"},
        {"name": "expiration", "type": "uint64"},
    ],
    "Withdraw": [
        {"name": "brokerId", "type": "string"},
        {"name": "chainId", "type": "uint256"},
        {"name": "receiver", "type": "address"},
        {"name": "token", "type": "string"},
        {"name": "amount", "type": "uint256"},
        {"name": "withdrawNonce", "type": "uint64"},
        {"name": "timestamp", "type": "uint64"},
    ],
    "SettlePnl": [
        {"name": "brokerId", "type": "string"},
        {"name": "chainId", "type": "uint256"},
        {"name": "settleNonce", "type": "uint64"},
        {"name": "timestamp", "type": "uint64"},
    ],
}

OFF_CHAIN_DOMAIN = {
    "name": "Orderly",
    "version": "1",
    "chainId": 421614,
    "verifyingContract": "0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC",
}

base_url = "https://testnet-api-evm.orderly.org"
broker_id = "woofi_dex"
chain_id = 421614

account: Account = Account.from_key(os.environ.get("PRIVATE_KEY"))

orderly_key = Ed25519PrivateKey.generate()

d = datetime.utcnow()
epoch = datetime(1970, 1, 1)
timestamp = math.trunc((d - epoch).total_seconds() * 1_000)

add_key_message = {
    "brokerId": broker_id,
    "chainId": chain_id,
    "orderlyKey": encode_key(orderly_key.public_key().public_bytes_raw()),
    "scope": "read,trading",
    "timestamp": timestamp,
    "expiration": timestamp + 1_000 * 60 * 60 * 24 * 365,  # 1 year
}

encoded_data = messages.encode_typed_data(
    domain_data=OFF_CHAIN_DOMAIN,
    message_types={"AddOrderlyKey": MESSAGE_TYPES["AddOrderlyKey"]},
    message_data=add_key_message,
)
signed_message = account.sign_message(encoded_data)

res = requests.post(
    "%s/v1/orderly_key" % base_url,
    headers={"Content-Type": "application/json"},
    json={
        "message": add_key_message,
        "signature": signed_message.signature.hex(),
        "userAddress": account.address,
    },
)
response = json.loads(res.text)
print("add_access_key:", response)
