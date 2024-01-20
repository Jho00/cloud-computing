const dbName = process.env.MONGO_INITDB_DATABASE;
const rootUsername = process.env.MONGO_INITDB_ROOT_USERNAME;
const rootPassword = process.env.MONGO_INITDB_ROOT_PASSWORD;


db.getSiblingDB('admin').auth(
  process.env.MONGO_INITDB_ROOT_USERNAME,
  process.env.MONGO_INITDB_ROOT_PASSWORD
);


db = db.getSiblingDB(dbName);


// Пример создания коллекции
db.createCollection('student');

db.createUser({
  user: rootUsername,
  pwd: rootPassword,
  roles: [{ role: 'readWrite', db: dbName }],
});
