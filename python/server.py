import os
from flask import Flask, request

from function.main import oracle_connection

app = Flask(__name__)

@app.route('/', methods=['GET'])
def call_oracle_connection():
    return oracle_connection(request)

if __name__ == "__main__":
    app.run(host='0.0.0.0',port=int(os.environ.get('PORT',8080)))