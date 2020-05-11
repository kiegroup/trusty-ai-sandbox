use myMongoDb
db.createUser({user : "user", pwd : "12345", roles : [{role: "readWrite", db : "myMongoDb"}], passwordDigestor : "server"})

