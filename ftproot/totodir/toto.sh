#!/bin/sh

# constantes
HOST=localhost 
LOGIN=Bobby
PASSWORD=123
PORT=1111


echo "====TESTS ACTIF===="

rm toto3.txt

ftp -d -i -n $HOST $PORT << END_SCRIPT_PORT
quote USER $LOGIN
quote PASS $PASSWORD
pwd
ls
cdup
pwd
cd totodir/
pwd
cd ..
pwd
get nimportequoi
get toto3.txt
put nimps
put toto3.txt totodir/exemple.txt
ls totodir/
quit
END_SCRIPT_PORT

cat toto3.txt

rm toto3.txt 

echo "====TESTS PASSIF===="

pftp -d -i -n -v $HOST $PORT << END_SCRIPT_PASV
quote USER $LOGIN
quote PASS $PASSWORD
pwd
ls
cdup
pwd
cd totodir/
pwd
cd ..
pwd
get nimps
get toto3.txt
put toto3.txt totodir/exemple2.txt
ls totodir/
quit
END_SCRIPT_PASV

cat toto3.txt 

rm toto3.txt

#Status API Training Shop Blog About Pricing

