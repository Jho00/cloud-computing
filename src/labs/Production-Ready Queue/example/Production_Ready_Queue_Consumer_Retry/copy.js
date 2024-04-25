const amqp = require('amqplib');
const { Pool } = require('pg');
const express = require('express');
const app = express();
const port = 3002;

const pool = new Pool({
  user: 'postgres',
  host: 'localhost',
  database: 'postgres',
  password: 'postgres',
  port: 5433,
});

const retryQueue = 'retry_queue';
const deadLetterQueue = 'dead_letter_queue';

async function handleMessage(content, channel, message) {
  try {
    const client = await pool.connect();
    await client.query('INSERT INTO messages (content) VALUES ($1)', [content]);
    client.release();
    channel.ack(message);
    console.log('Сообщение удалось отправить:', content);
  } catch (error) {
    let retryCount = message.properties.headers['x-retry-count'];
    console.error('Ошибка при вставке сообщения в базу данных:', error);
    while(retryCount < 5) {
      console.log(`Повторная попытка отправки сообщения (${retryCount + 1}):`, content);
      channel.publish('dead_letter_exchange', retryQueue, message.content, {
        headers: {
          'x-retry-count': retryCount,
        },
      });
      await new Promise((resolve) => setTimeout(resolve, 10000));
      retryCount++;
    }
    if (retryCount >= 5)
    {
      console.log('Достигнуто максимальное количество попыток. Перемещение сообщения в очередь мертвых сообщений:', content);
      channel.sendToQueue(deadLetterQueue, message.content, {
        headers: {
          'x-retry-count': retryCount,
        },
      });
      channel.ack(message);
    }
  }
}

pool.on('error', async (err) => {
  console.error('Ошибка подключения к базе данных:', err);

});

async function main() {
  try {
    const connection = await amqp.connect('amqp://localhost');
    const channel = await connection.createChannel();

    channel.consume(retryQueue, async (message) => {
      const content = message.content.toString();
      console.log(`Обработка сообщения в очереди повторной отправки:`, content);
      handleMessage(content, channel, message);
    });


    process.on('SIGINT', () => {
      channel.close();
      connection.close();
      pool.end();
    });

  } catch (error) {
    console.error('Ошибка:', error);
  }
}

(async () => {
  try {
    const client = await pool.connect();
    await client.query(`
      CREATE TABLE IF NOT EXISTS messages (
        id SERIAL PRIMARY KEY,
        content TEXT
      )
    `);
    client.release();
    main().catch(console.error);
  } catch (error) {
    console.error('Ошибка при создании таблицы:', error);
    main().catch(console.error);
  }
})();

app.listen(port, () => {
  console.log('App is running on port 3002');
});
