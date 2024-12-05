const amqp = require('amqplib');
const { Pool } = require('pg');
const express = require('express');
const app = express();
const port = 3001;
const pool = require('./db');


const mainQueue = 'main_queue';
const retryQueue = 'retry_queue';

async function handleMessage(content, channel, message) {
  try {
    const client = await pool.connect();
    await client.query('INSERT INTO messages (content) VALUES ($1)', [content]);
    client.release();
    channel.ack(message);
  } catch (error) {
    let retryCount = (message.properties.headers['x-retry-count'] || 0);
    console.error('Ошибка при вставке сообщения в базу данных:', error);
    while(retryCount < 3) {
      console.log(`Повторная попытка отправки сообщения (${retryCount + 1}):`, content);
      channel.publish('retry_exchange', mainQueue, message.content, {
        headers: {
          'x-retry-count': retryCount,
        },
      });
      retryCount++;
      await new Promise((resolve) => setTimeout(resolve, 5000));
    }
    if (retryCount >= 3)
    {
      console.log('Достигнуто максимальное количество попыток. Перемещение сообщения в очередь повторной отправки:', content);
      channel.sendToQueue(retryQueue, message.content, {
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

    channel.consume(mainQueue, async (message) => {
      const content = message.content.toString();
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
  console.log('App is running on port 3001');
});
