#!/bin/bash

echo "Build core"
cd secret-santa
mvn package
cp target/secret-santa*.jar ..

ip_regex='^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$'
port_regex='^([0-9]{1,5})$'

echo "------------------------------------------------------------------------"
echo "Setup Config (this can be changed later in config.json)" 
echo "------------------------------------------------------------------------"
while true; do
    read -p "Hostname? [127.0.0.1] " host
    host=${host:-127.0.0.1}
    if [[ $host =~ $ip_regex ]]; then
        break
    fi
done

while true; do
    read -p "HTTP port? [8080] " httpPort
    httpPort=${httpPort:-8080}
    if [[ $httpPort =~ $port_regex ]]; then
        break
    fi
done


ssl=null

while true; do
    read -p "Enable SSL? [y/n]" ssl
    if [ "$ssl" = "y" ] || [ "$ssl" = "n" ]; then
        break
    fi
done

if [ "$ssl" = "y" ]; then
    ssl="true"
    while true; do
        read -p "SSL Port? [4430]" secPort
        secPort=${secPort:-4430}
        if [[ $secPort =~ $port_regex ]]; then
            break
        fi
    done

    read -p "Certificate Path?" cert

else
    ssl="false"
    secPort="4430"
    cert=""
fi

cd ..
cat > config.json <<- EOM
{
    "hostname" : "$host",
    "http_port" : $httpPort,
    "ssl_enabled" : $ssl,
    "ssl_port" : $secPort,
    "ssl_cert_dir" : "$cert"
}
EOM
