#if [ $# -ne 2 ]; then
#  echo "Usage: <out from ping command on linux> <tries>"
#  exit 1
#fi

#tries="-$2"
#soma=0
#for time in `grep time= $1 | awk -F'time=' '{print $2}' | awk -F' ' '{print $1}' | head $tries`; do
#	soma=`echo "scale=3; $soma+$time" | bc`
#done

#tries=$2
#echo "scale=3; $soma/$tries" | bc

grep "rtt min/" $1 | awk -F'/' '{print $5}'