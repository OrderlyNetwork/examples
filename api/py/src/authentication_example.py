import json
import os
from base58 import b58decode
from requests import Request, Session

from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey

from authentication_example_signer import Signer


base_url = "https://testnet-api-evm.orderly.org"
orderly_account_id = "<orderly-account-id>"

key = b58decode(os.environ.get("ORDERLY_SECRET"))
orderly_key = Ed25519PrivateKey.from_private_bytes(key)

session = Session()
signer = Signer(orderly_account_id, orderly_key)

req = signer.sign_request(
    Request(
        "POST",
        "%s/v1/order" % base_url,
        json={
            "symbol": "PERP_ETH_USDC",
            "order_type": "MARKET",
            "order_quantity": 0.01,
            "side": "BUY",
        },
    )
)
res = session.send(req)
response = json.loads(res.text)
