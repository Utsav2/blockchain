## Setting up the blockchain

Download the bitcoin binary from [the official website](https://bitcoin.org/en/download)
Open the bitcoin-qt binary from the bin folder, and let it catch up with the entire blockchain.
Once you are satisfied with the amount of blockchain downloaded, close it.

This takes a while.

## How to set up Abe on Arch Linux from scratch
#### I would imagine the steps would be extremely similar for Ubuntu

Install postgresql via a package manager. I used

    yaourt -S postgresql

then
    sudo chown -R postgres:postgres /var/lib/postgres/
    sudo passwd postgres

This will prompt you to set a password for the postgres user. Set whatever you want.

    su - postgres -c "initdb --locale en_US.UTF-8 -D '/var/lib/postgres/data'"

Start the postgres service
    
    systemctl start postgresql.service    

Then login as the postgres user
    
    su - postgres

    createuser --interactive
    Enter name of role to add: { Your actual username }
    Shall the new role be a superuser? (y/n) n
    Shall the new role be allowed to create databases? (y/n) y
    Shall the new role be allowed to create more new roles? (y/n) n

    createdb abe

Stay logged into this user and edit /var/lib/postgres/data/pg_hba.conf by adding this line to the end

    local { Your username } abe ident

Go back to your regular user and do

    sudo systemctl reload postgresql

Then install Abe:

    cd /tmp
    cd bitcoin-abe
    sudo python2 setup.py install

Now go to the externals folder in this repo, and run

    python2 -m Abe.abe --config abe-pg.conf --commit-bytes 100000 --no-serve

This will takes lots (days) of time.

It will create a database named abe. You can use it through:

    su - postgres
    psql
    \c abe
    
These are some example commands:

    select first.pubkey_hash from txin_detail first join txin_detail second on (first.tx_id = second.tx_id) and (first.txin_id <> second.txin_id) and (first.txin_value < 100000) and (second.txin_value < 100000) and (first.pubkey_hash <> second.pubkey_hash) group by first.pubkey_hash limit 40;

This creates an edge list of public key hashes that are possibly related to other public key hashes.
They might be related because they appear as inputs in the same transaction, so they might be from the same wallet.

    copy(select first.pubkey_hash, second.pubkey_hash from txin_detail first join txin_detail second on (first.tx_id = second.tx_id) and (first.txin_id <> second.txin_id) and (first.txin_value < 100000) and (second.txin_value < 100000) and (first.pubkey_hash <> second.pubkey_hash) limit 1000000) to '/var/lib/postgres/edgelistbig.csv' with csv; 

This is the exact same command, but just writes it to a csv file.

## Website

Now that you have a database full of the blockchain data, you can set up a website.

The website runs on 3 servers, a Java server, a Python server, and a Javascript server. The Javascript server is not needed for production, it's only there during development to make development easier (it automatically reloads the html and css when you change it).

The java server constructs a graph from the edge list, and the Python server provides utilities for quick front end development.

To run the java server, open the project in Intellij, and click on Run -> Edit configurations. Set the program arguments as
    
    3000  /var/lib/postgres/edgelistbig.csv

Basically, the first argument is the port of the server, and the second is the edgelist from which the graph is constructed.

To run the python server (that also serves the website), go to the webapp folder, and run ./install.sh

This should set the project up. 

    flask/bin/python run.py

And you should have a webserver running on 127.0.0.1:9000/
