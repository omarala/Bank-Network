#!/bin/sh

#!/bin/sh

echo "Please select the type of network you would like"
echo " 1 - Linear one with 3 nodes"
echo " 2 - Circular one with 4 nodes"
echo " 3 - Random one with 7 nodes"

read nb
echo $nb
if [ $nb -ne "1" ] && [ $nb -ne "2" ] && [ $nb -ne "3" ]
  then
    echo "Select a valid number"
else
  java -jar ../out/artifacts/launchServer_jar/bank_network.jar &

  gnome-terminal -x java -jar ../out/artifacts/launchServer_jar/bank_network.jar &
  echo "nombre : "
  echo $nb
  if [ $nb -eq "1" ]
  then
    i=1
    while [ $i -le 3 ]
    do
    gnome-terminal -x java -jar ../out/artifacts/createBankNode_jar/bank_network.jar &
    ((i++))
    done
    sleep 1
    gnome-terminal -x java -jar ../out/artifacts/LinkLinear_jar/bank_network.jar &
  else
    if [ $nb -eq "2" ]
      then
        i=1
        while [ $i -le 4 ]
        do
        gnome-terminal -x java -jar ../out/artifacts/createBankNode_jar/bank_network.jar &
        ((i++))
        done
        sleep 1
        gnome-terminal -x java -jar ../out/artifacts/LinkCircular_jar/bank_network.jar &
      else
        if [ $nb -eq "3" ]
        then
          i=1
          while [ $i -le 7 ]
          do
          gnome-terminal -x java -jar ../out/artifacts/createBankNode_jar/bank_network.jar &
          ((i++))
          done
          sleep 1
          gnome-terminal -x java -jar ../out/artifacts/LinkRandom_jar/bank_network.jar &
        fi
    fi
  fi
  sleep 1
  gnome-terminal -x java -jar ../out/artifacts/createNetworkjar/bank_network.jar &
fi
