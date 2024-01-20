import requests
import time


class CircuitBreaker:
    def __init__(self):
        self.state = 'CLOSED'
        self.last_failure_time = None

    def request(self, url):
        if self.state == 'OPEN':
            raise Exception('Circuit breaker is open')
        try:
            response = requests.get(url)
            if response.status_code == 200:
                self.close()
            elif response.status_code == 500:
                self.open()
        except requests.exceptions.RequestException:
            self.open()
            raise

        return response

    def open(self):
        self.state = 'OPEN'
        self.last_failure_time = time.time()

    def close(self):
        self.state = 'CLOSED'
        self.last_failure_time = None

    def check_state(self, interval=10):
        if self.state == 'OPEN' and self.last_failure_time:
            if time.time() - self.last_failure_time >= interval:
                self.close()


circuit_breaker = CircuitBreaker()
url = 'http://127.0.0.1:5001/service'

for _ in range(100):
    try:
        circuit_breaker.check_state()

        response = circuit_breaker.request(url)
        print('Ответ сервера:', response.status_code)
    except Exception as e:
        print(e)
        print('Ошибка при отправке запроса')
    time.sleep(3)
