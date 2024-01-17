from datetime import datetime
import json
import math
import os
import requests

from eth_account import Account, messages


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
    "chainId": 421613,
    "verifyingContract": "0xCcCCccccCCCCcCCCCCCcCcCccCcCCCcCcccccccC",
}

base_url = "https://testnet-api-evm.orderly.org"
broker_id = "woofi_dex"
chain_id = 421613

account: Account = Account.from_key(os.environ.get("PRIVATE_KEY"))

res = requests.get("%s/v1/registration_nonce" % base_url)
response = json.loads(res.text)
registration_nonce = response["data"]["registration_nonce"]

d = datetime.utcnow()
epoch = datetime(1970, 1, 1)
timestamp = math.trunc((d - epoch).total_seconds() * 1_000)

register_message = {
    "brokerId": broker_id,
    "chainId": chain_id,
    "timestamp": timestamp,
    "registrationNonce": registration_nonce,
}

encoded_data = messages.encode_typed_data(
    domain_data=OFF_CHAIN_DOMAIN,
    message_types={"Registration": MESSAGE_TYPES["Registration"]},
    message_data=register_message,
)
signed_message = account.sign_message(encoded_data)

res = requests.post(
    "%s/v1/register_account" % base_url,
    headers={"Content-Type": "application/json"},
    json={
        "message": register_message,
        "signature": signed_message.signature.hex(),
        "userAddress": account.address,
    },
)
response = json.loads(res.text)

orderly_account_id = response["data"]["account_id"]
print("orderly_account_id:", orderly_account_id)
