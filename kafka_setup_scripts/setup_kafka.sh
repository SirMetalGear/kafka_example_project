curl -O https://apache-mirror.rbc.ru/pub/apache/kafka/2.8.0/kafka_2.12-2.8.0.tgz
tar xf kafka_2.12-2.8.0.tgz
mv kafka_2.12-2.8.0 kafka
mkdir kafka-logs
rm -rf kafka_2.12-2.8.0.tgz
sed -i '' "s|/tmp/zookeeper|$PWD/zookeeper_data|g" kafka/config/zookeeper.properties
sed -i '' "s|/tmp/kafka-logs|$PWD/kafka-logs|g" kafka/config/server.properties
echo -e "\033[37;1;41m KAFKA HAS BEEN INSTALLED \033[0m"
echo -e "\033[37;1;41m 1. RUN zookeeper_start.sh WITH NEW TERMINAL \033[0m"
echo -e "\033[37;1;41m 2. RUN kafka_start.sh WITH NEW TERMINAL \033[0m"
echo -e "\033[37;1;41m 3. RUN init_topic.sh TO SET NEW TOPIC \033[0m"
