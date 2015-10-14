# Tugas 5 IF4031: Aplikasi IRC sederhana berbasis Publisher-Subscriber dengan Apache Kafka.

Anggota Kelompok:

- Willy – 13512070
- Ahmad Zaky – 13512076

## Petunjuk Instalasi

Aplikasi dapat dibangun dengan menggunakan ant build tools. Jalankan `ant`, dan jar akan terbentuk pada `build/simple-irc-kafka.jar`.

## Cara Menjalankan Program

Jalankan `java –jar build/simple-irc-client.jar`.

## Konfigurasi Kafka

Terdapat file `config.properties` yang menyatakan konfigurasi ZooKeeper dan Broker Kafka. Berikut adalah isi file tersebut secara default.

	# The zookeeper that client will connect to. Defaults to "localhost:2181"
	zookeeper=localhost:2181

	# The broker to which client will send messages. Defaults to "localhost:9092".
	# Multiple brokers can be put as comma-separated string.
	broker=localhost:9092

Jika file tersebut tidak ada, maka kedua nilai tersebut akan dipakai secara default.

## Tes yang Dilakukan

### Testcase 1

- Pertama server dijalankan
- Client 1 dan Client 2 dijalankan.
- Command yang dimasukkan berurut seperti berikut:


	Client 1:
	/NICK Willy
	/JOIN grup

	Client 2:
	/NICK Zaky
	/JOIN grup
	@grup Halo

- Client 1 akan menerima pesan:


	[grup] (Zaky) Halo

- Client 2 akan menerima pesan:


	[grup] (Zaky) HaloWilly – 13512070
	Ahmad Zaky – 13512076

### Testcase 2

- Pertama server dijalankan
- Client 1 dan Client 2 dijalankan.
- Command yang dimasukkan berurut seperti berikut:


	Client 1:
	/NICK Willy
	/JOIN grup
	
	Client 2:
	/NICK Zaky
	/JOIN grup
	
	Client 1:
	/LEAVE grup
	
	Client 2:
	@grup Halo

- Client 1 akan menerima pesan:


	<tidak ada>

- Client 2 akan menerima pesan:


	[grup] (Zaky) Halo

### Testcase 3

- Pertama server dijalankan
- Client 1 dan Client 2 dijalankan.
- Command yang dimasukkan berurut seperti berikut:


	Client 1:
	Halo

	Client 2:
	Halo

- Client 1 akan menerima pesan:


	<tidak ada>

- Client 2 akan menerima pesan:


	<tidak ada>
