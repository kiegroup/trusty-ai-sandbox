# Random data generator

This flask application mocks the current trusty service, generating random data upon a request. 

## Requirements

- Python2 installed
- `virtualenv` installed
- `pip` installed

If you are using rhel probably you just need to (if you don't have `virtualenv` installed)

```bash
pip-2 install virtualenv
```

## Install 

Create a new virtual enviroment (in the current folder for example)
```bash 
virtualenv venv
```

Activate the virtualenv
```bash 
source venv/bin/activate
```

Install dependencies
```bash
pip install -r requirements.txt
```

## Run the application

The application will listen on `0.0.0.0` and the port `1337`. Start the server with

```bash
python2 server.py
```

## Query the application 

```bash
curl "http://0.0.0.0:8080/executions/decisions/08b60a3c-b048-4dd8-93d2-1833b85d0512/outcomes"
```
