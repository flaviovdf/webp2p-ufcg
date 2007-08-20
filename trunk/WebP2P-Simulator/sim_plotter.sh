#!/bin/sh

find_self() {
	SELF=`dirname $0`
}

usage() {
	echo "usage $0 <sim.properties files> <sim type> <param to vary> < values[] || -seq <first> <last> <inc> >"
	exit 1
}



#MAIN
if [ $# -lt 3  ]; then
	usage
fi

find_self

FILE=$1
TYPE=$2
PARAM=$3
OPTION=$4


if [ ! -r $FILE ]; then
	echo "cannot read $FILE"
	exit 1
fi

DATA=sim_data.dat
NEWPROP=/tmp/sim.prop.tmp

rm -f $DATA

shift 3
VALUES=$*
if [ "$1" == "-seq" ]; then
	if [ $# -lt 4 ]; then
		usage
	fi

	FIRST=$2
	LAST=$3
	INC=$4

	VALUES=`seq $FIRST $INC $LAST`
fi

NRUNS=1000
for newparam in $VALUES; do
	
	sum_mean=0
	sum_sd=0

	for i in `seq $NRUNS`; do

		rm -f simulator.log
	
		sed "s/$PARAM  *=  *[0-9]*\.*[0-9]*/$PARAM = $newparam/" $FILE > $NEWPROP
		sh $SELF/run.sh $TYPE $NEWPROP | grep = | awk '{print $NF}'
		results=`sh $SELF/parse_log.sh simulator.log`
		mean=`echo $results | awk '{print $1}'`
		sd=`echo $results | awk '{print $2}'`

		if [ $mean != "NaN" ]; then
			sum_mean=`echo "scale = 5; $sum_mean + $mean" | bc`	
		fi

		if [ $sd != "NaN" ]; then
			sum_sd=`echo "scale = 5; $sum_sd + $sd" | bc`
		fi


		rm -f $NEWPROP

	done

	AVGMEAN=`echo "scale = 5; $sum_mean / $NRUNS" | bc`
	AVGSD=`echo "scale = 5; $sum_sd / $NRUNS" | bc`
	OUT="$newparam   $AVGMEAN   $AVGSD"
	echo $OUT >> $DATA
done
