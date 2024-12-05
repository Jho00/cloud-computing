const amqp = require('amqplib');
const data = require('./data');
const express = require('express');
const app = express();
const port = 3000;

async function main() {
  const connection = await amqp.connect('amqp://localhost');
  const channel = await connection.createChannel();

  const mainQueue = 'main_queue';
  const retryQueue = 'retry_queue';
  const deadLetterQueue = 'dead_letter_queue';
  const retryExchange = 'retry_exchange';
  const deadLetterExchange = 'dead_letter_exchange';

  await channel.assertQueue(mainQueue, { durable: true });

  await channel.assertExchange(retryExchange, 'direct', { durable: true });

  await channel.assertExchange(deadLetterExchange, 'direct', { durable: true });

  await channel.assertExchange(deadLetterQueue, 'direct', { durable: true });

  await channel.assertQueue(retryQueue, { durable: true });

  await channel.bindQueue(retryQueue, retryExchange, '');

  await channel.assertQueue(deadLetterQueue, { durable: true });

  await channel.bindQueue(deadLetterQueue, deadLetterExchange, '');

  await channel.bindQueue(mainQueue, deadLetterQueue, '');

  const timer = setTimeout(async () => {
    await channel.close();
    await connection.close();
    process.exit(0); 
  }, 240000);

  for (let i = 0; i <= data.length; i++) {
    if (i === data.length) i = 0;
    const message = data[i];
    channel.sendToQueue(mainQueue, Buffer.from(message));
    await new Promise((resolve) => setTimeout(resolve, 1000));
  }
}

main().catch(console.error);

app.listen(port, () => {
  console.log('App is running on port 3000');
});
