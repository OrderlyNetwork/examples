from base58 import b58encode
from base64 import urlsafe_b64encode
from datetime import datetime
import json
import math
from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey
from requests import PreparedRequest, Request
import urllib


class Signer(object):
    def __init__(
        self,
        account_id: str,
        private_key: Ed25519PrivateKey,
    ) -> None:
        self._account_id = account_id
        self._private_key = private_key

    def sign_request(self, req: Request) -> PreparedRequest:
        d = datetime.utcnow()
        epoch = datetime(1970, 1, 1)
        timestamp = math.trunc((d - epoch).total_seconds() * 1_000)

        json_str = ""
        if req.json is not None:
            json_str = json.dumps(req.json)

        path = urllib.parse.urlparse(req.url).path
        message = str(timestamp) + req.method + path + json_str
        orderly_signature = urlsafe_b64encode(
            self._private_key.sign(message.encode())
        ).decode("utf-8")

        req.headers = {
            "orderly-timestamp": str(timestamp),
            "orderly-account-id": self._account_id,
            "orderly-key": encode_key(
                self._private_key.public_key().public_bytes_raw()
            ),
            "orderly-signature": orderly_signature,
        }
        print(req.headers)
        if req.method == "GET":
            req.headers["Content-Type"] = "application/x-www-form-urlencoded"
        elif req.method == "POST":
            req.headers["Content-Type"] = "application/json"

        return req.prepare()


def encode_key(key: bytes):
    return "ed25519:%s" % b58encode(key).decode("utf-8")
