import json
import os

from eth_account import Account
from web3 import Web3
from web3.middleware import construct_sign_and_send_raw_middleware


broker_id = "woofi_pro"
token_id = "USDC"
orderly_account_id = "0x..."

usdc_address = Web3.to_checksum_address("0x75faf114eafb1BDbe2F0316DF893fd58CE46AA4d")
vault_address = Web3.to_checksum_address("0x0EaC556c0C2321BA25b9DC01e4e3c95aD5CDCd2f")

account: Account = Account.from_key(os.environ.get("PRIVATE_KEY"))

w3 = Web3(Web3.HTTPProvider("https://arbitrum-sepolia.publicnode.com"))
w3.middleware_onion.add(construct_sign_and_send_raw_middleware(account))

with open("./src/abi/NativeUSDC.json") as f:
    abi = json.load(f)
usdc = w3.eth.contract(
    address=usdc_address,
    abi=abi,
)

deposit_amount = 100000

# approve USDC ERC-20 to be transferred to Vault contract
usdc.functions.approve(vault_address, deposit_amount).transact(
    {"from": account.address}
)

with open("./src/abi/Vault.json") as f:
    abi = json.load(f)
vault = w3.eth.contract(
    address=vault_address,
    abi=abi,
)

broker_hash = w3.keccak(text=broker_id)
token_hash = w3.keccak(text=token_id)

deposit_input = (
    bytes.fromhex(orderly_account_id[2:]),
    bytes.fromhex(broker_hash.hex()[2:]),
    bytes.fromhex(token_hash.hex()[2:]),
    deposit_amount,
)

# get wei deposit fee for `deposit` call
deposit_fee = vault.functions.getDepositFee(
    Web3.to_checksum_address(account.address),
    deposit_input,
).call()

# deposit USDC into Vault contract
vault.functions.deposit(deposit_input).transact(
    {"from": account.address, "value": deposit_fee}
)
