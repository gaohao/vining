- Redis:  
  1. Optimize the memory  
     - Reduce the length of json string  
     - Using 32 bit instances:
     Redis compiled with 32 bit target uses a lot less memory per key, since pointers are small, but such an instance will be limited to 4 GB of maximum memory usage. To compile Redis as 32 bit binary use make 32bit. RDB and AOF files are compatible between 32 bit and 64 bit instances (and between little and big endian of course) so you can switch from 32 to 64 bit, or the contrary, without problems.  
     - Using hashes to abstract a very memory efficient plain key-value store on top of Redis  
     If we store tweet obj as an json string:  
        1. we do not edit the string, then we will store some redundant fields  
        2. we edit the string, then we need to convert the obj to json obj first then delete some fields or write own code to construct the json string  
     So we can select the fields of a tweet which we want to store, and the store it in a hash. Use the id of tweet as key and store the tweet as hash table. Because the hash table is small, we can configure the redis to store the key-value as an plain text. Which is more memory efficent than a real hash table. The time to get a field is still near the constant time.  
  2. Use as web cache.  
  3. Use 2 databases one for read and one for write  
  4. Persistence use RDB. RDB is a very compact single-file point-in-time representation of Redis data.  
     AOF contains a log of all the operations one after the other in an easy to understand and parse format.AOF is usually bigger and maybe slower  
  5. Use sorted set to store last 2 minutes tweet. Use a string to store the key of current used sorted set. Delete old set and update current pointer to the new sorted set. Use transaction to ensure delete and update are performed at same time.  
  Memocached + MySQL is old solution  
