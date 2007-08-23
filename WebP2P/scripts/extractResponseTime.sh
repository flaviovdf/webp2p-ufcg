if [ $# -ne 1 ]; then
  echo "Usage: <out from ping command on linux>"
  exit 1
fi

soma=0
for time in `grep time= $1 | awk -F'time=' '{print $2}' | awk -F' ' '{print $1}' | head`; do
	soma=`echo "scale=5; $soma+$time" | bc`
done

echo "scale=5; $soma/10" | bc
