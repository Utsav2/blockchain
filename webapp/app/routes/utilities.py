from app import app, util
from flask import request, abort, jsonify
import json

@app.route('/getHash')
def get_hash():
    address = request.args.get('address', None)
    if address is None:
        abort(400)
    try:
        return util.addr_to_pubkey(address), 200
    except:
        abort(400)

@app.route('/getAddress')
def get_address():
    hashstr = request.args.get('hashes', None)
    if hashstr is None:
        abort(400)
    try:
        hashes = json.loads(hashstr)
        ret = {'data' : []}
        for h in hashes:
            ret['data'].append(util.pubkey_to_addr(h))
        return jsonify(ret)
    except Exception as e:
        abort(400)
