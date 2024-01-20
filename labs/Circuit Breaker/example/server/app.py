from flask import Flask, jsonify
import time
import threading

app = Flask(__name__)

DELAY = 5
fail_time = None
state = True


@app.route('/service')
def service():
    if check_time():
        return jsonify({"message": "Success"}), 200
    else:
        error_code = 500
        return jsonify({"error": f"Error with status code {error_code}"}), error_code


def check_time():
    global state
    state = not state
    return state
    threading.Timer(10.0, check_time()).start()


if __name__ == '__main__':
    app.run(port=5001)
    check_time()
