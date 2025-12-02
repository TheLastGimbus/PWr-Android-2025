from flask import Flask
from flask import Response

app = Flask(__name__)

hasPing = False


@app.route("/enable")
def enable():
    global hasPing
    hasPing = True
    return "Ok"


@app.route("/receive")
def receive():
    global hasPing
    if hasPing:
        hasPing = False
        return Response("true", 200)
    else:
        return Response("false", 403)


@app.route("/")
def hello_world():
    return "<p>Hello, World!</p>"
