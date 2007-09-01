find_self() {
	SELF_DIR=`dirname $0`
}

setclasspath() {
	LIB_DIR="$SELF_DIR/lib"
	CLASSPATH="$SELF_DIR/out/webp2psim.jar"
	for lib in $LIB_DIR/*; do
        	CLASSPATH="$CLASSPATH:$lib"
	done
}

#MAIN
find_self && setclasspath

java -Xms512m -Xmx1024m -cp $CLASSPATH webp2p_sim.util.ResponseTimeMetricCalculator  $1
