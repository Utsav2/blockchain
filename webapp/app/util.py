from Abe import base58

def pubkey_to_addr(h):
    return base58.hash_160_to_bc_address(h.decode('hex'))

def addr_to_pubkey(addr):
    return base58.bc_address_to_hash_160(addr).encode('hex')
